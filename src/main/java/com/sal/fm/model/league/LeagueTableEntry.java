package com.sal.fm.model.league;

import com.sal.fm.model.team.Team;

/**
 * Represents a row in the league table (standings).
 * Tracks performance stats like wins, losses, goals, and points.
 */
public class LeagueTableEntry {

    private final Team team;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int goalsScored;
    private int goalsAgainst;

    /**
     * Creates a new entry for the given team with zeroed stats.
     *
     * @param team the team being tracked
     */
    public LeagueTableEntry(Team team) {
        this.team = team;
    }

    /**
     * Updates the table entry based on the outcome of a single match.
     *
     * @param scored      number of goals this team scored
     * @param conceded    number of goals this team conceded
     * @param isHomeTeam  whether this team was home (true) or away (false)
     */
    public void recordResult(int scored, int conceded, boolean isHomeTeam) {
        played++;
        goalsScored += isHomeTeam ? scored : conceded;
        goalsAgainst += isHomeTeam ? conceded : scored;

        if (scored > conceded) {
            wins++;
        } else if (scored == conceded) {
            draws++;
        } else {
            losses++;
        }
    }

    public Team getTeam() {
        return team;
    }

    public int getPlayed() {
        return played;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public int getGoalsScored() {
        return goalsScored;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    /**
     * Computes and returns the points earned by this team.
     * 3 points per win, 1 per draw.
     *
     * @return total points
     */
    public int getPoints() {
        return (wins * 3) + draws;
    }
}
