package com.sal.fm.model.player;

/**
 * Represents hidden or personality-related attributes for a player.
 * These traits influence behavior under pressure, leadership, and long-term potential.
 */
public class HiddenAttributes {

    // === Attribute Fields ===

    private int determination;       // How mentally driven the player is to improve and succeed
    private int leadership;          // Ability to lead and influence teammates
    private int ambition;            // Drive to reach a higher level or push past limits
    private int teamwork;            // Willingness to support tactical shape and coordinate with teammates
    private int pressureHandling;    // Composure and performance under stressful conditions
    private int professionalism;     // Work ethic, discipline, and training behavior

    /**
     * Default constructor required for Jackson deserialization.
     */
    public HiddenAttributes() {
    }

    /**
     * Constructs a fully initialized set of hidden attributes.
     *
     * @param determination     mental strength and motivation
     * @param leadership        ability to inspire and guide
     * @param ambition          hunger to improve and win
     * @param teamwork          cooperative ability
     * @param pressureHandling  how well the player performs under pressure
     * @param professionalism   work ethic and training behavior
     */
    public HiddenAttributes(int determination, int leadership, int ambition, int teamwork,
                            int pressureHandling, int professionalism) {
        this.determination = determination;
        this.leadership = leadership;
        this.ambition = ambition;
        this.teamwork = teamwork;
        this.pressureHandling = pressureHandling;
        this.professionalism = professionalism;
    }

    // === Getters ===

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
