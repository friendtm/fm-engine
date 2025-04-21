package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class GoalkeepingAttributes {
    public int aerialReach;
    public int commandOfArea;
    public int communication;
    public int handling;
    public int kicking;
    public int oneOnOnes;
    public int passing;
    public int reflexes;
    public int rushingOut;
    public int throwing;

    public GoalkeepingAttributes() {
        // required for Jackson
    }

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
