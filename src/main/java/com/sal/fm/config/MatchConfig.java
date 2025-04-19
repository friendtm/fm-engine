package com.sal.fm.config;

public class MatchConfig {

    public static final int DAYS_BETWEEN_MATCHDAYS = 2;

    // Shot chance weights
    public static final double BASE_SHOT_ACCURACY = 25.0; // base chance boost

    public static final double SHOOTING_WEIGHT = 0.6;
    public static final double SKILL_WEIGHT = 0.25;

    // Goal chance weights
    public static final double BASE_GOAL_CHANCE = 0.10;
    public static final double SHOT_POWER_WEIGHT = 0.7;
    public static final double PACE_WEIGHT = 0.3;
    public static final double GK_DEFENSE_WEIGHT = 0.80;

    // Positional modifiers
    public static final double MODIFIER_PIVOT = 1.10;
    public static final double MODIFIER_WINGER = 1.15;
    public static final double MODIFIER_FIXO = 0.80;
    public static final double MODIFIER_GOALKEEPER = 0.65;

    // Other bonuses
    public static final double HIGH_WORKRATE_BONUS = 0.06;
    public static final int HIGH_WORKRATE_THRESHOLD = 85;

    // Momentum
    public static final int MOMENTUM_SHOT_WEIGHT = 60;
    public static final int MOMENTUM_PASS_WEIGHT = 40;

    // Normal event distribution
    public static final int NORMAL_PASS_WEIGHT = 85;
    public static final int NORMAL_SHOT_WEIGHT = 15;
}
