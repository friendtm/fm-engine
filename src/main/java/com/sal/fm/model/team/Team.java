package com.sal.fm.model.team;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sal.fm.enums.Tactic;
import com.sal.fm.model.player.Player;

/**
 * Represents a football team, including its name, players,
 * tactical formation, and match lineup.
 */
public class Team {

    private String name;
    private List<Player> players;
    private List<Player> startingLineup;
    private List<Player> substitutes;
    private Tactic tactic;

    /**
     * Constructs a new team with the given name and tactic.
     * Initializes empty player and lineup lists.
     *
     * @param name   name of the team (e.g., "Ajax", "Tigers FC")
     * @param tactic the preferred formation (e.g., DIAMOND, SQUARE)
     */
    public Team(String name, Tactic tactic) {
        this.name = name;
        this.tactic = tactic;
        this.players = new ArrayList<>();
        this.startingLineup = new ArrayList<>();
        this.substitutes = new ArrayList<>();
    }

    /**
     * Default constructor required for Jackson (de)serialization.
     */
    public Team() {
        this.players = new ArrayList<>();
        this.startingLineup = new ArrayList<>();
        this.substitutes = new ArrayList<>();
    }

    /**
     * Adds a player to the full roster.
     *
     * @param player the Player to add to this team
     */
    public void addPlayer(Player player) {
        players.add(player);
    }

    // === Getters ===

    public String getName() {
        return name;
    }

    public List<Player> getPlayers() {
        return players;
    }

    public List<Player> getStartingLineup() {
        return startingLineup;
    }

    public List<Player> getSubstitutes() {
        return substitutes;
    }

    public Tactic getTactic() {
        return tactic;
    }

    /**
     * Computes the average skill rating of all players on the team.
     * Used for UI, match scheduling, and simulation.
     *
     * @return average player rating (rounded)
     */
    @JsonIgnore
    public int getAverageSkill() {
        return (int) players.stream()
                .mapToInt(p -> p.getOverallRating(p.getPosition()))
                .average()
                .orElse(0);
    }

    /**
     * Computes the sum of all players' ratings.
     * Can be used for strength comparisons during matchmaking.
     *
     * @return total team rating
     */
    @JsonIgnore
    public int getTeamStrength() {
        return players.stream()
                .mapToInt(p -> p.getOverallRating(p.getPosition()))
                .sum();
    }

    // === Setters ===

    public void setStartingLineup(List<Player> startingLineup) {
        this.startingLineup = startingLineup;
    }

    public void setSubstitutes(List<Player> substitutes) {
        this.substitutes = substitutes;
    }
}
