package com.sal.fm.model;

public class GameState {
    private League league;
    private int currentDay;
    private int currentMatchday;
    private int lastMatchdayDay = 0;

    // Required by Jackson
    public GameState() {
    }

    public GameState(League league, int currentDay, int currentMatchday) {
        this.league = league;
        this.currentDay = currentDay;
        this.currentMatchday = currentMatchday;
    }

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
