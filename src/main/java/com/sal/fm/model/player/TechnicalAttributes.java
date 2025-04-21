package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class TechnicalAttributes {
    public int corners;
    public int penaltyTaking;
    public int freeKicks;
    public int shooting;
    public int longShots;
    public int heading;
    public int passing;
    public int dribbling;
    public int tackling;
    public int technique;

    public TechnicalAttributes() {
        // required for Jackson
    }

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

    @JsonIgnore
    public int getAverage() {
        int sum =
                corners +
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
