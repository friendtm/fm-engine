package com.sal.fm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sal.fm.engine.MatchEngine;
import com.sal.fm.model.player.Player;
import com.sal.fm.model.team.Team;
import com.sal.fm.util.MatchLogger;

import java.util.UUID;

/**
 * Represents a match between two teams, including simulation logic.
 * Handles match progression, goal scoring, and logging.
 */
public class Match {

    private Team homeTeam;
    private Team awayTeam;

    private int teamAScore = 0;
    private int teamBScore = 0;

    private int round; // Matchday number (1–22)

    @JsonProperty("played")
    private boolean isPlayed; // Whether the match has already been simulated

    private boolean silentMode = false; // Controls logging verbosity
    private int currentMinute = 0;

    private final String matchId = UUID.randomUUID().toString(); // Unique ID used for log file naming
    private MatchLogger logger;
    private MatchEngine engine;

    private static final int MATCH_DURATION_MINUTES = 40; // Total match time (2x20 mins)

    private boolean debugMode = true;

    /**
     * Constructs a new match between two teams.
     *
     * @param homeTeam the team playing at home
     * @param awayTeam the team playing away
     */
    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.isPlayed = false;
    }

    /**
     * Default constructor (for serialization).
     */
    public Match() {
        this.isPlayed = false;
        this.round = 0;
    }

    /**
     * Starts and simulates the match in two halves.
     * Logs match events via MatchLogger and resolves events using MatchEngine.
     */
    public void startMatch() {
        logger = new MatchLogger(matchId);
        engine = new MatchEngine(this, logger, debugMode);

        currentMinute = 0;
        logger.log(currentMinute, "Kick-off! " + homeTeam.getName() + " vs " + awayTeam.getName(), silentMode);

        // First Half (20 simulated minutes)
        for (int i = 0; i < 20; i++) {
            engine.simulateMinute(currentMinute, silentMode);
            currentMinute++;
        }

        logger.log(currentMinute, "⏸ Halftime: " + homeTeam.getName() + " " + teamAScore + " - " + teamBScore + " " + awayTeam.getName(), silentMode);
        logger.log(currentMinute, "▶ Second Half Begins!", silentMode);

        // Second Half (20 simulated minutes)
        for (int i = 0; i < 20; i++) {
            engine.simulateMinute(currentMinute, silentMode);
            currentMinute++;
        }

        displayFinalScore();
        logger.close();
    }

    /**
     * Increments the score for the appropriate team when a goal is scored.
     *
     * @param shooter the player who scored (not used here, but logged elsewhere)
     * @param team    the team to assign the goal to
     */
    public void scoreGoal(Player shooter, Team team) {
        if (team == homeTeam) {
            teamAScore++;
        } else {
            teamBScore++;
        }
    }

    /**
     * Logs the final score and match end.
     */
    private void displayFinalScore() {
        logger.log(currentMinute, "🔚 Full-time!", silentMode);
        logger.log(currentMinute, "Final Score: " + homeTeam.getName() + " " + teamAScore +
                " - " + teamBScore + " " + awayTeam.getName(), silentMode);
    }

    /**
     * Enables debug mode with verbose logging (overrides silent mode).
     */
    public void enableDebugMode() {
        this.silentMode = false;
        this.debugMode = true;
    }

    /**
     * Enables silent mode — used in bulk simulations.
     */
    public void enableSilentMode() {
        this.silentMode = true;
    }

    // === Getters and Setters ===

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public int getRound() {
        return round;
    }

    public int getTeamAScore() {
        return teamAScore;
    }

    public int getTeamBScore() {
        return teamBScore;
    }

    public void setRound(int round) {
        this.round = round;
    }

    public boolean isPlayed() {
        return isPlayed;
    }

    public void markAsPlayed() {
        this.isPlayed = true;
    }

    public void setDebugMode(boolean debugMode) {
        this.debugMode = debugMode;
    }

    /**
     * Returns a string description used in league listings and logs.
     */
    @Override
    public String toString() {
        String run = (round <= 11) ? "Run 1" : "Run 2";
        return String.format("Matchday %02d [%s]: %s vs %s", round, run, homeTeam.getName(), awayTeam.getName());
    }
}
