package com.sal.fm.config;

/**
 * MatchConfig contains all tunable parameters that affect gameplay mechanics
 * such as match flow, player actions, and duel resolutions.
 *
 * This class is purely static and should not be instantiated.
 */
public class MatchConfig {

    // == MATCH STRUCTURE ==
    /** Days between each matchday in the simulation */
    public static final int DAYS_BETWEEN_MATCHDAYS = 2;

    // == EVENT WEIGHTS ==
    /** Weight chance for shooting events during momentum */
    public static final int MOMENTUM_SHOT_WEIGHT = 60;
    /** Weight chance for passing events during momentum */
    public static final int MOMENTUM_PASS_WEIGHT = 40;
    /** Weight chance for passing events during normal play */
    public static final int NORMAL_PASS_WEIGHT = 85;
    /** Weight chance for shooting events during normal play */
    public static final int NORMAL_SHOT_WEIGHT = 15;

    // == WORK RATE ==
    /** Threshold value for defining "high" work rate */
    public static final int HIGH_WORKRATE_THRESHOLD = 85;
    /** Bonus added to scores if work rate is high */
    public static final double HIGH_WORKRATE_BONUS = 0.06;

    // == POSITION MODIFIERS ==
    /** Modifiers scale how impactful player roles are in outcomes */
    public static final double MODIFIER_PIVOT = 1.10;
    public static final double MODIFIER_WINGER = 1.15;
    public static final double MODIFIER_FIXO = 0.80;
    public static final double MODIFIER_GOALKEEPER = 0.65;

    // == SHOT ACCURACY (isShotOnTarget) ==
    public static final double BASE_SHOT_ACCURACY = 25.0;
    public static final double SHOOTING_WEIGHT = 0.6;
    public static final double SKILL_WEIGHT = 0.25;

    // == GOAL CONVERSION (isGoal) ==
    public static final double BASE_GOAL_CHANCE = 0.10;
    public static final double SHOT_POWER_WEIGHT = 0.7;
    public static final double PACE_WEIGHT = 0.3;
    public static final double GK_DEFENSE_WEIGHT = 0.80;

    // == SHOOTING UNDER PRESSURE ==
    public static final double PRESSURE_STRENGTH_BONUS = 0.10;
    public static final double PRESSURE_BALANCE_BONUS = 0.10;
    public static final double PRESSURE_BALANCE_PENALTY = 0.10;

    // == PASSING QUALITY ==
    public static final double PASSING_WEIGHT = 0.5;
    public static final double TECHNIQUE_WEIGHT = 0.3;
    public static final double VISION_WEIGHT = 0.2;

    // == PASS DUEL ==
    public static final double DEFENDING_WEIGHT = 0.6;
    public static final double ANTICIPATION_WEIGHT = 0.4;
    public static final double BASE_PASS_CHANCE = 40.0;

    // == SHOT DUEL (offensive) ==
    public static final double COMPOSURE_WEIGHT = 0.25;
    public static final double LONG_SHOTS_WEIGHT = 0.1;
    public static final double BALANCE_WEIGHT = 0.1;
    public static final double STRENGTH_WEIGHT = 0.1;

    // == SHOT DUEL (defensive) ==
    public static final double REFLEXES_WEIGHT = 0.5;
    public static final double POSITIONING_WEIGHT = 0.3;
    public static final double ONE_ON_ONE_WEIGHT = 0.2;

    // == COMPOSURE BONUS ==
    public static final double COMPOSURE_SHOOTING_BONUS = 0.05;

    /**
     * Private constructor to prevent instantiation.
     */
    private MatchConfig() {
    }
}