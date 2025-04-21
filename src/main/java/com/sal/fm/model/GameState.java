package com.sal.fm.model;

import com.sal.fm.model.league.League;

/**
 * Represents the persistent state of the game simulation.
 * This object tracks the league, simulation time (day/matchday),
 * and when the last matchday occurred — used to control match timing.
 */
public class GameState {

    private League league;              // The active league and its teams/matches
    private int currentDay;             // Current in-game day (increments over time)
    private int currentMatchday;        // The next matchday to be simulated (1–22)
    private int lastMatchdayDay = 0;    // The day on which the last matchday was played

    /**
     * Default constructor required for Jackson deserialization.
     */
    public GameState() {
    }

    /**
     * Constructs a fully initialized GameState.
     *
     * @param league           the active league
     * @param currentDay       the current simulation day
     * @param currentMatchday  the next scheduled matchday
     */
    public GameState(League league, int currentDay, int currentMatchday) {
        this.league = league;
        this.currentDay = currentDay;
        this.currentMatchday = currentMatchday;
    }

    // === Getters ===

    public League getLeague() {
        return league;
    }

    public int getCurrentDay() {
        return currentDay;
    }

    public int getCurrentMatchday() {
        return currentMatchday;
    }

    public int getLastMatchdayDay() {
        return lastMatchdayDay;
    }

    // === Setters ===

    public void setLeague(League league) {
        this.league = league;
    }

    public void setCurrentDay(int currentDay) {
        this.currentDay = currentDay;
    }

    public void setCurrentMatchday(int currentMatchday) {
        this.currentMatchday = currentMatchday;
    }

    public void setLastMatchdayDay(int lastMatchdayDay) {
        this.lastMatchdayDay = lastMatchdayDay;
    }
}
