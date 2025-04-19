package com.sal.fm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sal.fm.engine.MatchEngine;
import com.sal.fm.model.player.Player;
import com.sal.fm.model.team.Team;
import com.sal.fm.util.MatchLogger;

import java.util.UUID;

public class Match {
    private Team homeTeam;
    private Team awayTeam;
    private int teamAScore = 0;
    private int teamBScore = 0;

    private int round;
    @JsonProperty("played")
    private boolean isPlayed;
    private boolean silentMode = false;

    private int currentMinute = 0;

    private final String matchId = UUID.randomUUID().toString();
    private MatchLogger logger;
    private MatchEngine engine;
    private static final int MATCH_DURATION_MINUTES = 40;
    private boolean debugMode = true;

    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.isPlayed = false;
    }

    public Match() {
        this.isPlayed = false;
        this.round = 0;
    }

    public void startMatch() {
        logger = new MatchLogger(matchId);
        engine = new MatchEngine(this, logger, debugMode);

        currentMinute = 0;
        logger.log(currentMinute, "Kick-off! " + homeTeam.getName() + " vs " + awayTeam.getName(), silentMode);

        for (int i = 0; i < 20; i++) {
            engine.simulateMinute(currentMinute, silentMode);
            currentMinute++;
        }

        logger.log(currentMinute, "⏸ Halftime: " + homeTeam.getName() + " " + teamAScore + " - " + teamBScore + " " + awayTeam.getName(), silentMode);
        logger.log(currentMinute, "▶ Second Half Begins!", silentMode);

        for (int i = 0; i < 20; i++) {
            engine.simulateMinute(currentMinute, silentMode);
            currentMinute++;
        }

        displayFinalScore();
        logger.close();
    }

    public void scoreGoal(Player shooter, Team team) {
        if (team == homeTeam) {
            teamAScore++;
        } else {
            teamBScore++;
        }
    }

    private void displayFinalScore() {
        logger.log(currentMinute, "🔚 Full-time!", silentMode);
        logger.log(currentMinute, "Final Score: " + homeTeam.getName() + " " + teamAScore +
                " - " + teamBScore + " " + awayTeam.getName(), silentMode);
    }

    public void enableSilentMode() {
        this.silentMode = true;
    }

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

    @Override
    public String toString() {
        String run = (round <= 11) ? "Run 1" : "Run 2";
        return String.format("Matchday %02d [%s]: %s vs %s", round, run, homeTeam.getName(), awayTeam.getName());
    }
}