package com.sal.fm.core;

import com.sal.fm.model.Match;
import com.sal.fm.model.team.Team;
import com.sal.fm.model.league.LeagueTableEntry;

import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class UIPrinter {

    public static void showMatchdaySchedule(List<Match> matches, int matchday) {
        System.out.println("\n=== Matchday " + matchday + " Schedule ===");

        matches.stream()
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
