package com.sal.fm.model.league;

import com.sal.fm.model.Match;
import com.sal.fm.model.team.Team;

import java.util.*;

/**
 * Represents a League containing exactly 12 teams and a generated
 * season schedule of 22 rounds (double round-robin).
 *
 * Each team plays every other team twice: once home, once away.
 */
public class League {

    private List<Team> teams;
    private List<Match> matches;

    /**
     * Default constructor required by Jackson for deserialization.
     */
    public League() {
        this.teams = new ArrayList<>();
        this.matches = new ArrayList<>();
    }

    /**
     * Constructs a League with a given set of 12 teams.
     * Automatically generates the full season calendar (22 matchdays).
     *
     * @param teams List of exactly 12 teams
     * @throws IllegalArgumentException if the team count is not 12
     */
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
     * Generates a 22-round double round-robin schedule using a round-robin algorithm.
     *
     * - Run 1: each team plays all others once (home/away randomized)
     * - Run 2: same matchups with reversed home/away
     */
    private void generateCalendar() {
        int numTeams = teams.size();

        if (numTeams % 2 != 0) {
            throw new IllegalArgumentException("Number of teams must be even.");
        }

        List<Team> rotation = new ArrayList<>(teams);
        Team fixedTeam = rotation.remove(0); // One team is fixed in the rotation
        int totalRounds = numTeams - 1;
        int matchesPerRound = numTeams / 2;

        matches.clear();

        // == First Run ==
        for (int round = 0; round < totalRounds; round++) {
            Set<Team> usedInRound = new HashSet<>();

            for (int matchIndex = 0; matchIndex < matchesPerRound; matchIndex++) {
                Team home, away;

                if (matchIndex == 0) {
                    // Alternate fixed team as home/away to balance
                    home = (round % 2 == 0) ? fixedTeam : rotation.get(round % rotation.size());
                    away = (round % 2 == 0) ? rotation.get(round % rotation.size()) : fixedTeam;
                } else {
                    int firstIndex = (round + matchIndex) % rotation.size();
                    int secondIndex = (round + rotation.size() - matchIndex) % rotation.size();

                    home = rotation.get(firstIndex);
                    away = rotation.get(secondIndex);
                }

                Match match = new Match(home, away);
                match.setRound(round + 1);
                matches.add(match);
            }
        }

        // == Second Run: Mirror fixtures (swap home/away) ==
        List<Match> run2 = matches.stream()
                .map(m -> {
                    Match reversed = new Match(m.getAwayTeam(), m.getHomeTeam());
                    reversed.setRound(m.getRound() + totalRounds);
                    return reversed;
                })
                .toList();

        matches.addAll(run2);

        // == Validation: Ensure no duplicate team per round ==
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
