package com.sal.fm.engine;

import com.sal.fm.model.player.Player;
import com.sal.fm.config.MatchConfig;
import com.sal.fm.util.DiceUtil;

/**
 * Handles outcome calculations for duels between players during passing and shooting.
 * Includes utility methods for determining outcomes and generating debug logs.
 */
public class DuelResolver {

    private static double lastGoalChance = 0.0;

    /**
     * Determines whether a shot is on target based on shooter's attributes.
     */
    public static boolean isShotOnTarget(Player shooter) {
        double accuracyChance = AttributeCalculator.calculateShootingAccuracy(shooter);
        return DiceUtil.chance(accuracyChance);
    }

    /**
     * Determines whether a shot results in a goal, factoring in pressure.
     */
    public static boolean isGoal(Player shooter, Player goalkeeper, boolean underPressure) {
        double chance = AttributeCalculator.calculateShotSuccessChance(shooter, goalkeeper);

        if (underPressure) {
            chance -= MatchConfig.PRESSURE_BALANCE_PENALTY;
        }

        lastGoalChance = chance;
        return DiceUtil.chance(chance);
    }

    public static double getLastGoalChance() {
        return lastGoalChance;
    }

    /**
     * Builds a detailed debug string showing attributes and chance calculation for a shot.
     */
    public static String buildShootingDebugLog(Player shooter, Player goalkeeper, double chance, boolean goal, boolean underPressure) {
        return String.format(
                "SHOT DUEL: %s [SH: %d, TEC: %d, CMP: %d, BAL: %d, STR: %d] vs GK %s [GK AVG: %d] | Chance: %.2f%% | %s %s",
                shooter.getName(),
                shooter.getStats().getTechnical().getShooting(),
                shooter.getStats().getTechnical().getTechnique(),
                shooter.getStats().getMental().getComposure(),
                shooter.getStats().getPhysical().getBalance(),
                shooter.getStats().getPhysical().getStrength(),
                goalkeeper.getName(),
                goalkeeper.getStats().getGoalkeeping().getAverage(),
                chance * 100,
                goal ? "\u2705 GOAL" : "\uD83E\uDDE4 NO GOAL",
                underPressure ? "(under pressure)" : ""
        );
    }

    /**
     * Builds a detailed debug string showing attributes and result of a passing duel.
     */
    public static String buildPassingDebugLog(Player attacker, Player defender, boolean success, double attackerScore, double defenderScore) {
        return String.format(
                "PASS DUEL: %s [PASS: %d, TEC: %d, VIS: %d] vs %s [TACK: %d] | A: %.1f vs D: %.1f → %s",
                attacker.getName(),
                attacker.getStats().getTechnical().getPassing(),
                attacker.getStats().getTechnical().getTechnique(),
                attacker.getStats().getMental().getVision(),
                defender.getName(),
                defender.getStats().getTechnical().getTackling(),
                attackerScore, defenderScore,
                success ? "\u2705 SUCCESS" : "\u274C INTERCEPTED"
        );
    }
}