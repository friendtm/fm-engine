package com.sal.fm.engine;

import com.sal.fm.config.MatchConfig;
import com.sal.fm.enums.Position;
import com.sal.fm.model.player.Player;
import com.sal.fm.util.DiceUtil;

public class DuelResolver {

    public static boolean isShotOnTarget(Player shooter) {
        int shooting = shooter.getStats().getTechnical().getShooting();
        int skill = shooter.getStats().getTechnical().getDribbling();

        double baseAccuracy = shooting * MatchConfig.SHOOTING_WEIGHT;
        double bonus = skill * MatchConfig.SKILL_WEIGHT;
        double result = (baseAccuracy + bonus + MatchConfig.BASE_SHOT_ACCURACY) / 100.0;

        return DiceUtil.chance(result);
    }

    public static boolean isGoal(Player shooter, Player goalkeeper) {
        int shotPower = shooter.getStats().getTechnical().getShooting();
        int pace = shooter.getStats().getPhysical().getPace();
        int gkSkill = goalkeeper.getStats().getGoalkeeping().getAverage();

        double baseChance = MatchConfig.BASE_GOAL_CHANCE;
        double offensiveFactor = (shotPower * MatchConfig.SHOT_POWER_WEIGHT + pace * MatchConfig.PACE_WEIGHT) / 100.0;
        double defensiveFactor = (gkSkill * MatchConfig.GK_DEFENSE_WEIGHT) / 100.0;

        double rawChance = baseChance + offensiveFactor - defensiveFactor;

        double positionModifier = switch (shooter.getPosition()) {
            case PIVOT -> MatchConfig.MODIFIER_PIVOT;
            case WINGER -> MatchConfig.MODIFIER_WINGER;
            case FIXO -> MatchConfig.MODIFIER_FIXO;
            case GOALKEEPER -> MatchConfig.MODIFIER_GOALKEEPER;
            default -> 1.0;
        };

        double finalChance = rawChance * positionModifier;

        if (shooter.getPosition() == Position.FIXO && shooter.getStats().getMental().getWorkRate() >= MatchConfig.HIGH_WORKRATE_THRESHOLD) {
            finalChance += MatchConfig.HIGH_WORKRATE_BONUS;
        }

        return DiceUtil.chance(finalChance);
    }
}