package com.sal.fm.model.league;

import com.sal.fm.model.team.Team;

public class LeagueTableEntry {
    private Team team;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int goalsScored;
    private int goalsAgainst;
    private int points;
    private int averageSkill;

    public LeagueTableEntry(Team team) {
        this.team = team;
        this.averageSkill = team.getAverageSkill();
    }

    public void recordResult(int homeScore, int awayScore, boolean isHomeTeam) {
        played++;

        int scored = isHomeTeam ? homeScore : awayScore;
        int conceded = isHomeTeam ? awayScore : homeScore;

        goalsScored += scored;
        goalsAgainst += conceded;

        if (scored == conceded) {
            draws++;
            points += 1;
        } else if (scored > conceded) {
            wins++;
            points += 3;
        } else {
            losses++;
        }
    }

    public Team getTeam() { return team; }
    public int getPlayed() { return played; }
    public int getWins() { return wins; }
    public int getDraws() { return draws; }
    public int getLosses() { return losses; }
    public int getGoalsScored() { return goalsScored; }
    public int getGoalsAgainst() { return goalsAgainst; }
    public int getPoints() { return points; }

    public int getGoalDifference() {
        return goalsScored - goalsAgainst;
    }
}
