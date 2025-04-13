package com.sal.fm.model;

import java.util.ArrayList;
import java.util.List;

import com.sal.fm.enums.Tactic;

public class Team {
    private String name;
    private List<Player> players;
    private List<Player> startingLineup;
    private List<Player> substitutes;
    private Tactic tactic;

    public Team(String name, Tactic tactic) {
        this.name = name;
        this.tactic = tactic;
        this.players = new ArrayList<>();
        this.startingLineup = new ArrayList<>();
        this.substitutes = new ArrayList<>();
    }

    public void addPlayer(Player player) { players.add(player); }

    // Getters and Setters
    public String getName() { return name; }

    public List<Player> getPlayers() { return players; }

    public List<Player> getStartingLineup() { return startingLineup; }

    public List<Player> getSubstitutes() { return substitutes; }

    public Tactic getTactic() { return tactic; }

    public int getTeamStrength() { return players.stream().mapToInt(Player::getOverallRating).sum(); }

    public void setStartingLineup(List<Player> startingLineup) { this.startingLineup = startingLineup; }

    public void setSubstitutes(List<Player> substitutes) { this.substitutes = substitutes; }
}
