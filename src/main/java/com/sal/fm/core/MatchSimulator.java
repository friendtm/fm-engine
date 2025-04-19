package com.sal.fm.core;

import com.sal.fm.model.GameState;
import com.sal.fm.model.Match;
import com.sal.fm.model.league.League;
import com.sal.fm.model.league.LeagueTableEntry;
import com.sal.fm.model.team.Team;

import java.util.*;

public class MatchSimulator {

    public static void simulateMatchday(GameState state) {
        int matchday = state.getCurrentMatchday();
        League league = state.getLeague();

        if (matchday > 22) {
            System.out.println("The season is over. No more matchdays.");
            return;
        }

        System.out.println("\n=== Simulating Matchday " + matchday + " ===");

        List<Match> matches = league.getMatches().stream()
                .filter(m -> m.getRound() == matchday && !m.isPlayed())
                .toList();

        if (matches.isEmpty()) {
            System.out.println("No matches found for this matchday.");
            return;
        }

        for (Match match : matches) {
            match.enableSilentMode();
            match.startMatch();
            match.markAsPlayed();

            System.out.printf("%s %d - %d %s%n",
                    match.getHomeTeam().getName(),
                    match.getTeamAScore(),
                    match.getTeamBScore(),
                    match.getAwayTeam().getName());
        }

        displayLeagueTable(league);

        state.setCurrentMatchday(matchday + 1);
        state.setLastMatchdayDay(state.getCurrentDay());
    }

    public static void showMatchdaySchedule(GameState state) {
        int matchday = state.getCurrentMatchday();
        League league = state.getLeague();

        System.out.println("\n=== Matchday " + matchday + " Schedule ===");
        league.getMatches().stream()
                .filter(m -> m.getRound() == matchday)
                .forEach(m -> {
                    String home = "[H] " + m.getHomeTeam().getAverageSkill() + " " + m.getHomeTeam().getName();
                    String away = "[A] " + m.getAwayTeam().getAverageSkill() + " " + m.getAwayTeam().getName();

                    if (m.isPlayed()) {
                        System.out.printf("%-25s %d - %d %-25s%n", home, m.getTeamAScore(), m.getTeamBScore(), away);
                    } else {
                        System.out.printf("%-25s vs %-25s%n", home, away);
                    }
                });
    }

    public static void displayLeagueTable(League league) {
        List<LeagueTableEntry> table = buildLeagueTable(league);
        UIPrinter.displayLeagueTable(table);
    }

    public static List<LeagueTableEntry> buildLeagueTable(League league) {
        Map<String, LeagueTableEntry> table = new HashMap<>();

        for (Team team : league.getTeams()) {
            table.put(team.getName(), new LeagueTableEntry(team));
        }

        for (Match match : league.getMatches()) {
            if (!match.isPlayed()) continue;

            Team home = match.getHomeTeam();
            Team away = match.getAwayTeam();
            int homeScore = match.getTeamAScore();
            int awayScore = match.getTeamBScore();

            table.get(home.getName()).recordResult(homeScore, awayScore, true);
            table.get(away.getName()).recordResult(homeScore, awayScore, false);
        }

        return table.values().stream()
                .sorted(Comparator.comparingInt(LeagueTableEntry::getPoints).reversed())
                .toList();
    }
}