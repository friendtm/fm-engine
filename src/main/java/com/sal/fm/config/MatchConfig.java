package com.sal.fm.config;

public class MatchConfig {

    // == MATCH STRUCTURE ==
    public static final int DAYS_BETWEEN_MATCHDAYS = 2;

    // == EVENT WEIGHTS ==
    public static final int MOMENTUM_SHOT_WEIGHT = 60;
    public static final int MOMENTUM_PASS_WEIGHT = 40;
    public static final int NORMAL_PASS_WEIGHT = 85;
    public static final int NORMAL_SHOT_WEIGHT = 15;

    // == WORK RATE ==
    public static final int HIGH_WORKRATE_THRESHOLD = 85;
    public static final double HIGH_WORKRATE_BONUS = 0.06;

    // == POSITION MODIFIERS ==
    public static final double MODIFIER_PIVOT = 1.10;
    public static final double MODIFIER_WINGER = 1.15;
    public static final double MODIFIER_FIXO = 0.80;
    public static final double MODIFIER_GOALKEEPER = 0.65;

    // == SHOT ACCURACY (isShotOnTarget) ==
    public static final double BASE_SHOT_ACCURACY = 25.0; // Base chance boost
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

    // Pass duel weights
    public static final double DEFENDING_WEIGHT = 0.6;
    public static final double ANTICIPATION_WEIGHT = 0.4;
    public static final double BASE_PASS_CHANCE = 40.0; // Flat bonus to help weaker players

    // Shot Duel weights (offensive)
    public static final double COMPOSURE_WEIGHT = 0.25;
    public static final double LONG_SHOTS_WEIGHT = 0.1;
    public static final double BALANCE_WEIGHT = 0.1;
    public static final double STRENGTH_WEIGHT = 0.1;

    // Shot Duel weights (defensive)
    public static final double REFLEXES_WEIGHT = 0.5;
    public static final double POSITIONING_WEIGHT = 0.3;
    public static final double ONE_ON_ONE_WEIGHT = 0.2;

    // == COMPOSURE BONUS ==
    public static final double COMPOSURE_SHOOTING_BONUS = 0.05;

    private MatchConfig() {
        // Prevent instantiation
    }
}