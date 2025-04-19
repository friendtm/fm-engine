package com.sal.fm.model.player;

public class MentalAttributes {
    public int vision;
    public int composure;
    public int concentration;
    public int positioning;
    public int decisions;
    public int offTheBall;
    public int aggression;
    public int anticipation;
    public int flair;
    public int workRate;

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

    public int getVision() {
        return vision;
    }

    public int getComposure() {
        return composure;
    }

    public int getConcentration() {
        return concentration;
    }

    public int getPositioning() {
        return positioning;
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
