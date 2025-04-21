package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a set of goalkeeping-specific attributes for a player.
 * Used to evaluate goalkeeper performance in shot resolution and other duel logic.
 */
public class GoalkeepingAttributes {

    // === Raw Attribute Values ===
    private int aerialReach;
    private int commandOfArea;
    private int communication;
    private int handling;
    private int kicking;
    private int oneOnOnes;
    private int passing;
    private int reflexes;
    private int rushingOut;
    private int throwing;

    /**
     * Default constructor required for Jackson deserialization.
     */
    public GoalkeepingAttributes() {
    }

    /**
     * Creates a fully initialized set of goalkeeper attributes.
     *
     * @param aerialReach     ability to intercept high balls and crosses
     * @param commandOfArea   how well the keeper controls their area
     * @param communication   ability to organize defenders
     * @param handling        ability to cleanly catch or secure shots
     * @param kicking         accuracy and power when kicking the ball
     * @param oneOnOnes       ability to perform in 1v1 situations
     * @param passing         distribution skill with the ball
     * @param reflexes        reaction speed to shots
     * @param rushingOut      timing and speed when leaving the goal line
     * @param throwing        accuracy and strength in throws
     */
    public GoalkeepingAttributes(int aerialReach, int commandOfArea, int communication, int handling,
                                 int kicking, int oneOnOnes, int passing, int reflexes,
                                 int rushingOut, int throwing) {
        this.aerialReach = aerialReach;
        this.commandOfArea = commandOfArea;
        this.communication = communication;
        this.handling = handling;
        this.kicking = kicking;
        this.oneOnOnes = oneOnOnes;
        this.passing = passing;
        this.reflexes = reflexes;
        this.rushingOut = rushingOut;
        this.throwing = throwing;
    }

    /**
     * Computes a simplified average rating of the goalkeeper's most relevant attributes.
     *
     * @return average rating (0–100 scale) used in match simulations
     */
    @JsonIgnore
    public int getAverage() {
        int sum =
                aerialReach +
                        ((commandOfArea + communication) / 2) +
                        ((handling + kicking + passing + throwing) / 4) +
                        oneOnOnes +
                        reflexes;

        return sum / 5;
    }

    // === Getters ===

    public int getAerialReach() {
        return aerialReach;
    }

    public int getCommandOfArea() {
        return commandOfArea;
    }

    public int getCommunication() {
        return communication;
    }

    public int getHandling() {
        return handling;
    }

    public int getKicking() {
        return kicking;
    }

    public int getOneOnOnes() {
        return oneOnOnes;
    }

    public int getPassing() {
        return passing;
    }

    public int getReflexes() {
        return reflexes;
    }

    public int getRushingOut() {
        return rushingOut;
    }

    public int getThrowing() {
        return throwing;
    }
}
