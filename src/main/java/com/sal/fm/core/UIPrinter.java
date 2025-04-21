package com.sal.fm.core;

import com.sal.fm.model.Match;
import com.sal.fm.model.league.LeagueTableEntry;

import java.util.Comparator;
import java.util.List;

/**
 * Handles console display logic for matches and league standings.
 */
public class UIPrinter {

    /**
     * Displays the scheduled or played matches for a given matchday.
     *
     * @param matches  list of all league matches
     * @param matchday the matchday to display
     */
    public static void showMatchdaySchedule(List<Match> matches, int matchday) {
        System.out.println("\n=== Matchday " + matchday + " Schedule ===");

        matches.stream()
                .filter(m -> m.getRound() == matchday)
                .forEach(m -> {
                    String home = "[H] " + m.getHomeTeam().getAverageSkill() + " " + m.getHomeTeam().getName();
                    String away = "[A] " + m.getAwayTeam().getAverageSkill() + " " + m.getAwayTeam().getName();

                    if (m.isPlayed()) {
                        // Show result
                        System.out.printf("%-25s %d - %d %-25s%n", home, m.getTeamAScore(), m.getTeamBScore(), away);
                    } else {
                        // Show fixture
                        System.out.printf("%-25s vs %-25s%n", home, away);
                    }
                });
    }

    /**
     * Displays the current league standings in table format.
     *
     * @param table the list of league entries to display
     */
    public static void displayLeagueTable(List<LeagueTableEntry> table) {
        List<LeagueTableEntry> sorted = table.stream()
                .sorted(Comparator.comparingInt(LeagueTableEntry::getPoints).reversed())
                .toList();

        System.out.println("\n=== League Table ===");
        System.out.printf("%-20s %2s %2s %2s %2s %3s %3s %3s%n", "Team", "P", "W", "D", "L", "GS", "GA", "Pts");

        for (LeagueTableEntry entry : sorted) {
            System.out.printf("%-20s %2d %2d %2d %2d %3d %3d %3d%n",
                    entry.getTeam().getName(),
                    entry.getPlayed(),
                    entry.getWins(),
                    entry.getDraws(),
                    entry.getLosses(),
                    entry.getGoalsScored(),
                    entry.getGoalsAgainst(),
                    entry.getPoints());
        }
    }
}
