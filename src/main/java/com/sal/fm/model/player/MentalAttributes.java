package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a player's mental skillset, affecting decision-making,
 * creativity, awareness, and behavior on the pitch.
 */
public class MentalAttributes {

    // === Core Mental Traits ===

    private int vision;           // Ability to spot passes and see opportunities
    private int composure;        // Calmness under pressure, especially in duels or shooting
    private int concentration;    // Focus level throughout the match
    private int positioning;      // Defensive awareness, ability to be in the right place
    private int decisions;        // Quality of decision-making under various circumstances
    private int offTheBall;       // Intelligence in movement and space exploitation
    private int aggression;       // Assertiveness in duels and pressing
    private int anticipation;     // Ability to predict opponent actions
    private int flair;            // Willingness to attempt creative or risky moves
    private int workRate;         // Effort level throughout the match (affects fitness/momentum bonuses)

    /**
     * Default constructor (required by Jackson for JSON deserialization).
     */
    public MentalAttributes() {
    }

    /**
     * Constructs a fully initialized set of mental attributes.
     *
     * @param vision        ability to see creative or tactical opportunities
     * @param composure     staying calm under pressure
     * @param concentration mental focus across the match
     * @param positioning   awareness and movement for defensive shape
     * @param decisions     smart choices under game scenarios
     * @param offTheBall    intelligence when not in possession
     * @param aggression    assertiveness and intensity
     * @param anticipation  reading the game and intercepting
     * @param flair         risk-taking and creative expression
     * @param workRate      energy and willingness to contribute to both ends
     */
    public MentalAttributes(int vision, int composure, int concentration, int positioning, int decisions,
                            int offTheBall, int aggression, int anticipation, int flair, int workRate) {
        this.vision = vision;
        this.composure = composure;
        this.concentration = concentration;
        this.positioning = positioning;
        this.decisions = decisions;
        this.offTheBall = offTheBall;
        this.aggression = aggression;
        this.anticipation = anticipation;
        this.flair = flair;
        this.workRate = workRate;
    }

    // === Getters ===

    public int getVision() {
        return vision;
    }

    public int getComposure() {
        return composure;
    }

    public int getConcentration() {
        return concentration;
    }

    public int getDecisions() {
        return decisions;
    }

    public int getOffTheBall() {
        return offTheBall;
    }

    public int getAggression() {
        return aggression;
    }

    public int getAnticipation() {
        return anticipation;
    }

    public int getFlair() {
        return flair;
    }

    public int getWorkRate() {
        return workRate;
    }

    public int getPositioning() {
        return positioning;
    }

    /**
     * Computes the average of all mental attributes.
     *
     * @return rounded average on a 0–100 scale
     */
    @JsonIgnore
    public int getAverage() {
        int sum =
                vision +
                        composure +
                        concentration +
                        positioning +
                        decisions +
                        offTheBall +
                        aggression +
                        anticipation +
                        flair +
                        workRate;

        return sum / 10;
    }
}
