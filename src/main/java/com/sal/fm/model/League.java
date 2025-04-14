package com.sal.fm.model;

import java.util.*;

/**
 * Represents a League containing a set of teams and a generated season schedule.
 */
public class League {
    private List<Team> teams;
    private List<Match> matches;

    // Required by Jackson
    public League() {
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
    }

    public League(List<Team> teams) {
        if (teams.size() != 12) {
            throw new IllegalArgumentException("League must have exactly 12 teams.");
        }
        this.teams = new ArrayList<>(teams);
        this.matches = new ArrayList<>();
        generateCalendar();
    }

    public List<Match> getMatches() {
        return matches;
    }

    public List<Team> getTeams() {
        return teams;
    }

    /**
     * Generates the full season calendar (22 rounds):
     * - Run 1: each team plays all others once (random home/away)
     * - Run 2: same matchups, reverse venues
     */
    private void generateCalendar() {
        int numTeams = teams.size();

        if (numTeams % 2 != 0) {
            throw new IllegalArgumentException("Number of teams must be even.");
        }

        // Clone teams for rotation and fix the first team
        List<Team> rotation = new ArrayList<>(teams);
        Team fixedTeam = rotation.remove(0); // Fixed position
        int totalRounds = numTeams - 1;
        int matchesPerRound = numTeams / 2;

        matches.clear();

        for (int round = 0; round < totalRounds; round++) {
            Set<Team> usedInRound = new HashSet<>();

            for (int matchIndex = 0; matchIndex < matchesPerRound; matchIndex++) {
                Team home, away;

                if (matchIndex == 0) {
                    home = (round % 2 == 0) ? fixedTeam : rotation.get(round % rotation.size());
                    away = (round % 2 == 0) ? rotation.get(round % rotation.size()) : fixedTeam;
                } else {
                    int firstIndex = (round + matchIndex) % rotation.size();
                    int secondIndex = (round + rotation.size() - matchIndex) % rotation.size();

                    home = rotation.get(firstIndex);
                    away = rotation.get(secondIndex);
                }

                // Create match for run 1
                Match match = new Match(home, away);
                match.setRound(round + 1);
                matches.add(match);
            }
        }

        // Mirror matches to create run 2
        List<Match> run2 = matches.stream()
                .map(m -> {
                    Match reversed = new Match(m.getAwayTeam(), m.getHomeTeam());
                    reversed.setRound(m.getRound() + totalRounds);
                    return reversed;
                }).toList();

        matches.addAll(run2);

        // ✅ Final check for duplicate teams in any round
        for (int r = 1; r <= totalRounds * 2; r++) {
            final int roundNumber = r;
            Set<Team> used = new HashSet<>();
            List<Match> matchesInRound = matches.stream()
                    .filter(m -> m.getRound() == roundNumber)
                    .toList();

            for (Match m : matchesInRound) {
                if (!used.add(m.getHomeTeam()) || !used.add(m.getAwayTeam())) {
                    System.out.println("❌ Duplicate team in round " + roundNumber + ": " +
                            m.getHomeTeam().getName() + " vs " + m.getAwayTeam().getName());
                }
            }
        }
    }
}
