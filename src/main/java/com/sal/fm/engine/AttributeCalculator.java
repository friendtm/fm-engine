package com.sal.fm.engine;

import com.sal.fm.config.MatchConfig;
import com.sal.fm.model.player.Player;
import com.sal.fm.enums.Position;

/**
 * The AttributeCalculator class contains core logic for calculating
 * stat-based chances in key match events such as shooting, passing, and goal scoring.
 */
public class AttributeCalculator {

    /**
     * Calculates the chance a shot is on target based on shooting and technique.
     *
     * @param shooter the player attempting the shot
     * @return probability (0.0 to 1.0) the shot is on target
     */
    public static double calculateShootingAccuracy(Player shooter) {
        int shooting = shooter.getStats().getTechnical().getShooting();
        int skill = shooter.getStats().getTechnical().getTechnique();

        // Weighted sum plus base accuracy
        double base = MatchConfig.BASE_SHOT_ACCURACY;
        double weightedSkill = (shooting * MatchConfig.SHOOTING_WEIGHT) +
                (skill * MatchConfig.SKILL_WEIGHT);

        return (base + weightedSkill) / 100.0;
    }

    /**
     * Calculates the chance a shot results in a goal, considering both attacker and goalkeeper attributes.
     *
     * @param shooter    the attacking player
     * @param goalkeeper the defending goalkeeper
     * @return final goal probability (0.0 to 1.0)
     */
    public static double calculateShotSuccessChance(Player shooter, Player goalkeeper) {
        // Offensive stats
        int shooting = shooter.getStats().getTechnical().getShooting();
        int technique = shooter.getStats().getTechnical().getTechnique();
        int composure = shooter.getStats().getMental().getComposure();
        int longShots = shooter.getStats().getTechnical().getLongShots();
        int balance = shooter.getStats().getPhysical().getBalance();
        int strength = shooter.getStats().getPhysical().getStrength();

        // Defensive stats
        int reflexes = goalkeeper.getStats().getGoalkeeping().getReflexes();
        int positioning = goalkeeper.getStats().getMental().getPositioning();
        int oneOnOnes = goalkeeper.getStats().getGoalkeeping().getOneOnOnes();

        // Attacker contribution (weighted sum)
        double attackerScore = shooting * MatchConfig.SHOOTING_WEIGHT +
                technique * MatchConfig.TECHNIQUE_WEIGHT +
                composure * MatchConfig.COMPOSURE_WEIGHT +
                longShots * MatchConfig.LONG_SHOTS_WEIGHT +
                balance * MatchConfig.BALANCE_WEIGHT +
                strength * MatchConfig.STRENGTH_WEIGHT;

        // Defender contribution (weighted sum)
        double defenderScore = reflexes * MatchConfig.REFLEXES_WEIGHT +
                positioning * MatchConfig.POSITIONING_WEIGHT +
                oneOnOnes * MatchConfig.ONE_ON_ONE_WEIGHT;

        // Position modifier (e.g., PIVOTs are stronger finishers than FIXOs)
        double positionModifier = switch (shooter.getPosition()) {
            case WINGER -> MatchConfig.MODIFIER_WINGER;
            case PIVOT -> MatchConfig.MODIFIER_PIVOT;
            case FIXO -> MatchConfig.MODIFIER_FIXO;
            case GOALKEEPER -> MatchConfig.MODIFIER_GOALKEEPER;
            default -> 1.0;
        };

        // Bonus for high work rate players
        if (shooter.getStats().getMental().getWorkRate() >= MatchConfig.HIGH_WORKRATE_THRESHOLD) {
            attackerScore += MatchConfig.HIGH_WORKRATE_BONUS * 100;
        }

        // Compute raw and adjusted chance
        double rawChance = MatchConfig.BASE_GOAL_CHANCE + (attackerScore - defenderScore) / 100.0;
        double finalChance = clampChance(rawChance * positionModifier);

        return finalChance;
    }

    /**
     * Alternative method for calculating goal chance (legacy).
     *
     * @param shooter       the attacker
     * @param goalkeeper    the keeper
     * @param underPressure whether the shooter is under pressure
     * @return adjusted goal chance
     */
    public static double calculateGoalChance(Player shooter, Player goalkeeper, boolean underPressure) {
        int shotPower = shooter.getStats().getTechnical().getShooting();
        int pace = shooter.getStats().getPhysical().getPace();
        int gkSkill = goalkeeper.getStats().getGoalkeeping().getReflexes();

        double base = MatchConfig.BASE_GOAL_CHANCE;

        // Attacker and goalkeeper contributions
        double attackerFactor = (shotPower * MatchConfig.SHOT_POWER_WEIGHT + pace * MatchConfig.PACE_WEIGHT) / 100.0;
        double keeperFactor = gkSkill * MatchConfig.GK_DEFENSE_WEIGHT / 100.0;

        double rawChance = base + attackerFactor - keeperFactor;

        // Apply position-based multiplier
        double modifier = switch (shooter.getPosition()) {
            case PIVOT -> MatchConfig.MODIFIER_PIVOT;
            case WINGER -> MatchConfig.MODIFIER_WINGER;
            case FIXO -> MatchConfig.MODIFIER_FIXO;
            case GOALKEEPER -> MatchConfig.MODIFIER_GOALKEEPER;
            default -> 1.0;
        };

        // Work rate bonus
        if (shooter.getStats().getMental().getWorkRate() >= MatchConfig.HIGH_WORKRATE_THRESHOLD) {
            rawChance += MatchConfig.HIGH_WORKRATE_BONUS;
        }

        // Pressure penalty
        if (underPressure) {
            int pressure = shooter.getStats().getHidden().getPressureHandling();
            rawChance -= pressure * MatchConfig.PRESSURE_BALANCE_PENALTY;
        }

        return Math.max(0, rawChance * modifier);
    }

    /**
     * Calculates chance of a successful pass based on attacker and defender stats.
     *
     * @param passer   the passing player
     * @param defender the opposing tackler
     * @return chance of a successful pass (0.0 to 1.0)
     */
    public static double calculatePassSuccessChance(Player passer, Player defender) {
        int passing = passer.getStats().getTechnical().getPassing();
        int technique = passer.getStats().getTechnical().getTechnique();
        int vision = passer.getStats().getMental().getVision();

        int tackling = defender.getStats().getTechnical().getTackling();
        int anticipation = defender.getStats().getMental().getAnticipation();

        // Weighted duel scores
        double attackerScore = passing * MatchConfig.PASSING_WEIGHT +
                technique * MatchConfig.TECHNIQUE_WEIGHT +
                vision * MatchConfig.VISION_WEIGHT;

        double defenderScore = tackling * MatchConfig.DEFENDING_WEIGHT +
                anticipation * MatchConfig.ANTICIPATION_WEIGHT;

        // Add baseline pass chance to give weaker players some help
        double chance = (attackerScore - defenderScore + MatchConfig.BASE_PASS_CHANCE) / 100.0;

        return clampChance(chance);
    }

    /**
     * Ensures the chance value stays within [0.01, 0.99] bounds.
     *
     * @param chance raw probability
     * @return clamped chance
     */
    public static double clampChance(double chance) {
        return Math.max(0.01, Math.min(0.99, chance));
    }
}
