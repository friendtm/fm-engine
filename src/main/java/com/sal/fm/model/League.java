package com.sal.fm.model;

import java.util.*;

/**
 * Represents a League containing a set of teams and a generated season schedule.
 */
public class League {
    private final List<Team> teams;
    private final List<Match> matches;

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
        List<Match> run1 = new ArrayList<>();
        List<Match> run2 = new ArrayList<>();

        // 1. Generate all unique matchups
        for (int i = 0; i < teams.size(); i++) {
            for (int j = i + 1; j < teams.size(); j++) {
                Team team1 = teams.get(i);
                Team team2 = teams.get(j);

                boolean homeFirst = Math.random() < 0.5;

                Team home = homeFirst ? team1 : team2;
                Team away = homeFirst ? team2 : team1;

                run1.add(new Match(home, away));
                run2.add(new Match(away, home));
            }
        }

        // Shuffle for randomness
        Collections.shuffle(run1);
        Collections.shuffle(run2);

        // 2. Assign rounds (22 total = 11 for each run, 6 matches per round)
        for (int round = 1; round <= 11; round++) {
            for (int i = 0; i < 6; i++) {
                int matchIndex = (round - 1) * 6 + i;

                Match match1 = run1.get(matchIndex);
                match1.setRound(round);
                matches.add(match1);

                Match match2 = run2.get(matchIndex);
                match2.setRound(round + 11); // run 2 starts at round 12
                matches.add(match2);
            }
        }
    }
}
