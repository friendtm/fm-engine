package com.sal.fm.model.player;

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

    public int getAverage() {
        int sum =
                aerialReach +
                ((commandOfArea + communication) / 2) +
                ((handling + kicking + passing + throwing) / 4) +
                oneOnOnes +
                reflexes;

        return sum / 5;
    }
}
