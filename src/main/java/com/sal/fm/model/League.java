package com.sal.fm.model;

import java.util.ArrayList;
import java.util.List;

public class League {
    private String name;
    private List<Team> teams;
    private List<Match> matches;

    public League(String name) {
        this.name = name;
        this.teams = new ArrayList<Team>();
        this.matches = new ArrayList<Match>();
    }

    public void addTeam(Team team) {
        teams.add(team);
    }

    public void addMatch(Match match) {
        matches.add(match);
    }

    // Getters, Setters, maybe a method to sort league table...
}
