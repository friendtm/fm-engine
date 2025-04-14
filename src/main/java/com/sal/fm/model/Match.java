package com.sal.fm.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.sal.fm.enums.Position;
import com.sal.fm.enums.MatchEvent;
import com.sal.fm.util.MatchLogger;

import java.util.List;
import java.util.Random;
import java.util.stream.Collectors;

/**
 * Simulates a football match between two teams with simplified rules.
 * This class manages the match flow, simulates game events (like shots and passes),
 * and tracks the score of the match.
 */
public class Match {
    private Team homeTeam; // First team in the match
    private Team awayTeam; // Second team in the match
    private int teamAScore = 0; // Score for team A
    private int teamBScore = 0; // Score for team B

    private int round;
    @JsonProperty("played")
    private boolean isPlayed;
    private boolean silentMode = false;

    private int currentMinute = 0;

    private final Random random = new Random(); // Random object to simulate events in the game
    private final MatchLogger logger = new MatchLogger(); // Logger object to control logs during a Match
    private static final int MATCH_DURATION_MINUTES = 40; // Total duration of the match in minutes

    /**
     * Constructor for Match. Initializes the two teams participating in the match.
     *
     * @param homeTeam The first team
     * @param awayTeam The second team
     */
    public Match(Team homeTeam, Team awayTeam) {
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.isPlayed = false;
    }

    public Match() {
        // required for Jackson
        this.isPlayed = false;
        this.round = 0;
    }

    /**
     * Starts the match simulation. Runs the match for a set duration and displays the final score.
     */
    public void startMatch() {
        currentMinute = 0; // Counter to track the current minute of the match

        // Simulate the match until the match duration has passed
        while (currentMinute < MATCH_DURATION_MINUTES) {
            simulateMinute(); // Simulate one minute of gameplay
            currentMinute++; // Increment the current minute
        }

        // After the match is over, display the final score
        displayFinalScore();
    }

    /**
     * Simulates one minute of gameplay by randomly selecting an event (pass/tackle or shot on goal).
     */
    private void simulateMinute() {
        // Randomly select an event: 50% chance for a pass/tackle, 50% for a shot on goal
        MatchEvent event = random.nextInt(100) < 85 ? MatchEvent.PASS_OR_TACKLE : MatchEvent.SHOT_ON_GOAL;

        // Call appropriate method based on the selected event
        switch (event) {
            case PASS_OR_TACKLE -> simulatePassOrTackle(); // Simulate a pass or tackle event
            case SHOT_ON_GOAL -> simulateShotOnGoal(); // Simulate a shot on goal event
        }
    }

    /**
     * Simulates a pass or tackle event. This method currently just prints a placeholder message.
     */
    private void simulatePassOrTackle() {
        if (!silentMode) logger.log(currentMinute, "Pass or tackle event occurs.");
    }

    /**
     * Simulates a shot on goal event. It randomly selects an attacking and defending team and simulates the shot.
     */
    private void simulateShotOnGoal() {
        // Randomly select attacking and defending teams
        Team attackingTeam = random.nextInt(2) == 0 ? homeTeam : awayTeam;
        Team defendingTeam = attackingTeam == homeTeam ? awayTeam : homeTeam;

        // Filter the starting lineup to get players who are not goalkeepers for the attacking team
        List<Player> shooters = attackingTeam.getStartingLineup().stream()
                .filter(p -> p.getPosition() != Position.GOALKEEPER)
                .collect(Collectors.toList());

        // If there are no shooters (i.e., all players are goalkeepers), return early
        if (shooters.isEmpty()) return;

        // Randomly select a player from the attackers to take the shot
        Player shooter = shooters.get(random.nextInt(shooters.size()));
        // Find the goalkeeper in the defending team (assuming goalkeeper is always at index 0)
        Player goalkeeper = defendingTeam.getStartingLineup().stream()
                .filter(p -> p.getPosition() == Position.GOALKEEPER)
                .findFirst()
                .orElse(null);

        // If no goalkeeper is found, log an error message and return early
        if (goalkeeper == null) {
            if (!silentMode) logger.log(currentMinute, "No goalkeeper found in " + defendingTeam.getName());
            return;
        }

        // Check if the shot is on target
        if (isShotOnTarget(shooter)) {
            // If the shot is on target, check if it results in a goal
            if (isGoal(shooter, goalkeeper)) {
                // If it's a goal, score it for the attacking team
                scoreGoal(shooter, attackingTeam);
            } else {
                // If the shot is saved, log the goalkeeper's save
                if (!silentMode) logger.log(currentMinute, formatPlayer(shooter) + " makes a save for " + defendingTeam.getName());
            }
        } else {
            // If the shot misses, log the miss
            if (!silentMode) logger.log(currentMinute, formatPlayer(shooter) + " misses the shot.");
        }
    }

    /**
     * Updates the score for the team that scored a goal.
     *
     * @param shooter The player who took the shot
     * @param team The team that scored the goal
     */
    private void scoreGoal(Player shooter, Team team) {
        // Increment the appropriate team's score
        if (team == homeTeam) {
            teamAScore++;
        } else {
            teamBScore++;
        }
        // Log the goal-scoring event
        if (!silentMode) logger.log(currentMinute, formatPlayer(shooter) + " scores for " + team.getName());
    }

    /**
     * Determines if the shot taken by the player is on target based on the player's skill.
     *
     * @param shooter The player who took the shot
     * @return true if the shot is on target, false otherwise
     */
    private boolean isShotOnTarget(Player shooter) {
        // The shot is on target if the player's skill is greater than a random value
        return random.nextDouble() < (shooter.getSkill() * 0.6 / 100.0);
    }

    /**
     * Determines if the shot results in a goal based on the player's skill and the goalkeeper's skill.
     *
     * @param shooter The player who took the shot
     * @param goalkeeper The goalkeeper trying to stop the shot
     * @return true if the shot results in a goal, false otherwise
     */
    private boolean isGoal(Player shooter, Player goalkeeper) {
        double baseChance = 0.06; // 6% base
        double skillDiff = (shooter.getSkill() - goalkeeper.getSkill()) / 100.0;

        return random.nextDouble() < (baseChance + skillDiff * 0.8);
    }

    /**
     * Displays the final score of the match at the end.
     */
    private void displayFinalScore() {
        if (!silentMode) logger.log(currentMinute, "Match Ended!"); // Log that the match has ended
        // Log the final score for both teams
        if (!silentMode) logger.log(currentMinute, homeTeam.getName() + ": " + teamAScore + " - " + teamBScore + " : " + awayTeam.getName());
    }

    /**
     * Helper method to format a player's information (skill, position, name).
     *
     * @param p The player to format
     * @return A formatted string representing the player's skill, position, and name
     */
    private String formatPlayer(Player p) {
        return p.getSkill() + "|" + p.getPosition() + "|" + p.getName();
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

    @Override
    public String toString() {
        String run = (round <= 11) ? "Run 1" : "Run 2";
        return String.format(
                "Matchday %02d [%s]: %s vs %s",
                round,
                run,
                homeTeam.getName(),
                awayTeam.getName()
        );
    }
}
