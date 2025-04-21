package com.sal.fm.core;

import com.sal.fm.model.GameState;
import com.sal.fm.model.Match;
import com.sal.fm.model.league.League;
import com.sal.fm.model.league.LeagueTableEntry;
import com.sal.fm.model.team.Team;

import java.util.*;

/**
 * Handles simulation of an entire matchday, including score generation,
 * table updates, and result display.
 */
public class MatchSimulator {

    /**
     * Simulates all matches scheduled for the current matchday in the game state.
     *
     * @param state the current GameState object
     */
    public static void simulateMatchday(GameState state) {
        int matchday = state.getCurrentMatchday();
        League league = state.getLeague();

        if (matchday > 22) {
            System.out.println("The season is over. No more matchdays.");
            return;
        }

        System.out.println("\n=== Simulating Matchday " + matchday + " ===");

        // Find all unplayed matches for this matchday
        List<Match> matches = league.getMatches().stream()
                .filter(m -> m.getRound() == matchday && !m.isPlayed())
                .toList();

        if (matches.isEmpty()) {
            System.out.println("No matches found for this matchday.");
            return;
        }

        for (Match match : matches) {
            match.enableSilentMode();  // hides logs if not in debug mode
            match.startMatch();
            match.markAsPlayed();

            // Display result
            System.out.printf("%s %d - %d %s%n",
                    match.getHomeTeam().getName(),
                    match.getTeamAScore(),
                    match.getTeamBScore(),
                    match.getAwayTeam().getName());
        }

        // Show updated standings
        displayLeagueTable(league);

        // Advance to next matchday
        state.setCurrentMatchday(matchday + 1);
        state.setLastMatchdayDay(state.getCurrentDay());
    }

    /**
     * Prints the schedule and results (if available) for the current matchday.
     *
     * @param state the current GameState
     */
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

    /**
     * Rebuilds and displays the league standings table.
     *
     * @param league the league to update
     */
    public static void displayLeagueTable(League league) {
        List<LeagueTableEntry> table = buildLeagueTable(league);
        UIPrinter.displayLeagueTable(table);
    }

    /**
     * Builds the league table from all played matches.
     *
     * @param league the league to evaluate
     * @return sorted list of league table entries
     */
    public static List<LeagueTableEntry> buildLeagueTable(League league) {
        Map<String, LeagueTableEntry> table = new HashMap<>();

        // Create table entries for each team
        for (Team team : league.getTeams()) {
            table.put(team.getName(), new LeagueTableEntry(team));
        }

        // Apply match results
        for (Match match : league.getMatches()) {
            if (!match.isPlayed()) continue;

            Team home = match.getHomeTeam();
            Team away = match.getAwayTeam();
            int homeScore = match.getTeamAScore();
            int awayScore = match.getTeamBScore();

            table.get(home.getName()).recordResult(homeScore, awayScore, true);
            table.get(away.getName()).recordResult(homeScore, awayScore, false);
        }

        // Sort by points descending
        return table.values().stream()
                .sorted(Comparator.comparingInt(LeagueTableEntry::getPoints).reversed())
                .toList();
    }
}
