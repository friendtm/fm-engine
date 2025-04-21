package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a player's technical ability with the ball.
 * These attributes define skill in set pieces, shooting, passing,
 * and on-ball control — especially important for outfield players.
 */
public class TechnicalAttributes {

    // === Core Technical Skills ===

    private int corners;          // Accuracy and power when delivering corner kicks
    private int penaltyTaking;    // Effectiveness from the penalty spot
    private int freeKicks;        // Ability to strike accurate free kicks
    private int shooting;         // Finishing skill in general play
    private int longShots;        // Shooting power and accuracy from distance
    private int heading;          // Control and power when heading the ball
    private int passing;          // Short and long-range pass accuracy
    private int dribbling;        // Ball control while moving past defenders
    private int tackling;         // Ability to dispossess opponents cleanly
    private int technique;        // Overall technical fluency and skill

    /**
     * Default constructor required for serialization (e.g. Jackson).
     */
    public TechnicalAttributes() {
    }

    /**
     * Constructs a full technical profile for a player.
     *
     * @param corners        corner kick quality
     * @param penaltyTaking  penalty kick quality
     * @param freeKicks      free kick accuracy
     * @param shooting       general finishing ability
     * @param longShots      distance shooting skill
     * @param heading        aerial finishing skill
     * @param passing        passing skill
     * @param dribbling      close control and agility
     * @param tackling       defensive dispossession skill
     * @param technique      general technical quality and fluidity
     */
    public TechnicalAttributes(int corners, int penaltyTaking, int freeKicks, int shooting, int longShots,
                               int heading, int passing, int dribbling, int tackling, int technique) {
        this.corners = corners;
        this.penaltyTaking = penaltyTaking;
        this.freeKicks = freeKicks;
        this.shooting = shooting;
        this.longShots = longShots;
        this.heading = heading;
        this.passing = passing;
        this.dribbling = dribbling;
        this.tackling = tackling;
        this.technique = technique;
    }

    // === Getters ===

    public int getCorners() {
        return corners;
    }

    public int getPenaltyTaking() {
        return penaltyTaking;
    }

    public int getFreeKicks() {
        return freeKicks;
    }

    public int getShooting() {
        return shooting;
    }

    public int getLongShots() {
        return longShots;
    }

    public int getHeading() {
        return heading;
    }

    public int getPassing() {
        return passing;
    }

    public int getDribbling() {
        return dribbling;
    }

    public int getTackling() {
        return tackling;
    }

    public int getTechnique() {
        return technique;
    }

    /**
     * Computes an overall technical rating based on key attributes.
     * Note: Set-piece stats (corners, penalties, free kicks) are excluded.
     *
     * @return rounded average technical skill score
     */
    @JsonIgnore
    public int getAverage() {
        int sum =
                corners +       // optionally exclude for a pure gameplay average
                        shooting +
                        longShots +
                        heading +
                        passing +
                        dribbling +
                        tackling +
                        technique;

        return sum / 8;
    }
}
