package com.sal.fm.engine;

import com.sal.fm.config.MatchConfig;
import com.sal.fm.model.player.Player;
import com.sal.fm.enums.Position;

public class AttributeCalculator {

    public static double calculateShootingAccuracy(Player shooter) {
        int shooting = shooter.getStats().getTechnical().getShooting();
        int skill = shooter.getStats().getTechnical().getTechnique();

        double base = MatchConfig.BASE_SHOT_ACCURACY;
        double weightedSkill = (shooting * MatchConfig.SHOOTING_WEIGHT) +
                (skill * MatchConfig.SKILL_WEIGHT);

        return (base + weightedSkill) / 100.0;
    }

    public static double calculateShotSuccessChance(Player shooter, Player goalkeeper) {
        int shooting = shooter.getStats().getTechnical().getShooting();
        int technique = shooter.getStats().getTechnical().getTechnique();
        int composure = shooter.getStats().getMental().getComposure();

        int longShots = shooter.getStats().getTechnical().getLongShots();
        int balance = shooter.getStats().getPhysical().getBalance();
        int strength = shooter.getStats().getPhysical().getStrength();

        int reflexes = goalkeeper.getStats().getGoalkeeping().getReflexes();
        int positioning = goalkeeper.getStats().getMental().getPositioning();
        int oneOnOnes = goalkeeper.getStats().getGoalkeeping().getOneOnOnes();

        // Offensive contribution
        double attackerScore =
                shooting * MatchConfig.SHOOTING_WEIGHT +
                        technique * MatchConfig.TECHNIQUE_WEIGHT +
                        composure * MatchConfig.COMPOSURE_WEIGHT +
                        longShots * MatchConfig.LONG_SHOTS_WEIGHT +
                        balance * MatchConfig.BALANCE_WEIGHT +
                        strength * MatchConfig.STRENGTH_WEIGHT;

        // Defensive contribution
        double defenderScore =
                reflexes * MatchConfig.REFLEXES_WEIGHT +
                        positioning * MatchConfig.POSITIONING_WEIGHT +
                        oneOnOnes * MatchConfig.ONE_ON_ONE_WEIGHT;

        // Position-based modifier
        double positionModifier = switch (shooter.getPosition()) {
            case WINGER -> MatchConfig.MODIFIER_WINGER;
            case PIVOT -> MatchConfig.MODIFIER_PIVOT;
            case FIXO -> MatchConfig.MODIFIER_FIXO;
            case GOALKEEPER -> MatchConfig.MODIFIER_GOALKEEPER;
            default -> 1.0;
        };

        if (shooter.getStats().getMental().getWorkRate() >= MatchConfig.HIGH_WORKRATE_THRESHOLD) {
            attackerScore += MatchConfig.HIGH_WORKRATE_BONUS * 100; // converted to same scale
        }

        double rawChance = MatchConfig.BASE_GOAL_CHANCE + (attackerScore - defenderScore) / 100.0;
        double finalChance = clampChance(rawChance * positionModifier);

        return finalChance;
    }

    public static double calculateGoalChance(Player shooter, Player goalkeeper, boolean underPressure) {
        int shotPower = shooter.getStats().getTechnical().getShooting();
        int pace = shooter.getStats().getPhysical().getPace();
        int gkSkill = goalkeeper.getStats().getGoalkeeping().getReflexes();

        double base = MatchConfig.BASE_GOAL_CHANCE;

        double attackerFactor = (shotPower * MatchConfig.SHOT_POWER_WEIGHT +
                pace * MatchConfig.PACE_WEIGHT) / 100.0;

        double keeperFactor = gkSkill * MatchConfig.GK_DEFENSE_WEIGHT / 100.0;

        double rawChance = base + attackerFactor - keeperFactor;

        // Apply position modifier
        double modifier = switch (shooter.getPosition()) {
            case PIVOT -> MatchConfig.MODIFIER_PIVOT;
            case WINGER -> MatchConfig.MODIFIER_WINGER;
            case FIXO -> MatchConfig.MODIFIER_FIXO;
            case GOALKEEPER -> MatchConfig.MODIFIER_GOALKEEPER;
            default -> 1.0;
        };

        // High work rate bonus
        if (shooter.getStats().getMental().getWorkRate() >= MatchConfig.HIGH_WORKRATE_THRESHOLD) {
            rawChance += MatchConfig.HIGH_WORKRATE_BONUS;
        }

        // Pressure handling adjustment
        if (underPressure) {
            int pressure = shooter.getStats().getHidden().getPressureHandling();
            rawChance -= pressure * MatchConfig.PRESSURE_BALANCE_PENALTY;
        }

        return Math.max(0, rawChance * modifier); // Ensure chance is never negative
    }

    public static double calculatePassSuccessChance(Player passer, Player defender) {
        int passing = passer.getStats().getTechnical().getPassing();
        int technique = passer.getStats().getTechnical().getTechnique();
        int vision = passer.getStats().getMental().getVision();

        int tackling = defender.getStats().getTechnical().getTackling();
        int anticipation = defender.getStats().getMental().getAnticipation();

        double attackerScore =
                passing * MatchConfig.PASSING_WEIGHT +
                        technique * MatchConfig.TECHNIQUE_WEIGHT +
                        vision * MatchConfig.VISION_WEIGHT;

        double defenderScore =
                tackling * MatchConfig.DEFENDING_WEIGHT +
                        anticipation * MatchConfig.ANTICIPATION_WEIGHT;

        double chance = (attackerScore - defenderScore + MatchConfig.BASE_PASS_CHANCE) / 100.0;

        return clampChance(chance);
    }

    public static double clampChance(double chance) {
        return Math.max(0.01, Math.min(0.99, chance));
    }
}