package com.sal.fm.model.player;

public class HiddenAttributes {
    public int determination;
    public int leadership;
    public int ambition;
    public int teamwork;
    public int pressureHandling;
    public int professionalism;

    public HiddenAttributes() {
        // required for Jackson
    }

    public HiddenAttributes(int determination, int leadership, int ambition, int teamwork,
                            int pressureHandling, int professionalism) {
        this.determination = determination;
        this.leadership = leadership;
        this.ambition = ambition;
        this.teamwork = teamwork;
        this.pressureHandling = pressureHandling;
        this.professionalism = professionalism;
    }

    public int getDetermination() {
        return determination;
    }

    public int getLeadership() {
        return leadership;
    }

    public int getAmbition() {
        return ambition;
    }

    public int getTeamwork() {
        return teamwork;
    }

    public int getPressureHandling() {
        return pressureHandling;
    }

    public int getProfessionalism() {
        return professionalism;
    }
}