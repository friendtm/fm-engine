package com.sal.fm.data;

import com.sal.fm.enums.Position;
import com.sal.fm.enums.Tactic;
import com.sal.fm.model.Player;
import com.sal.fm.model.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Utility class responsible for generating the starting lineup and substitutes
 * based on a team's tactic and player ratings.
 */
public class LineupBuilder {

    /**
     * Generates a starting lineup and substitutes for the given team based on its tactic.
     *
     * @param team The team for which to generate the lineup.
     */
    public static void generateLineup(Team team) {
        Tactic tactic = team.getTactic();
        List<Player> players = new ArrayList<>(team.getPlayers());

        validateTeamRoster(players, tactic); // 👈 Run validations

        List<Player> gks = filterAndSort(players, Position.GOALKEEPER);
        List<Player> fixos = filterAndSort(players, Position.FIXO);
        List<Player> wingers = filterAndSort(players, Position.WINGER);
        List<Player> pivots = filterAndSort(players, Position.PIVOT);

        List<Player> startingLineup = new ArrayList<>();
        List<Player> substitutes = new ArrayList<>();

        Player gk = gks.remove(0);
        startingLineup.add(gk);

        switch (tactic) {
            case DIAMOND:
                startingLineup.add(extractTop(fixos));   // Defender
                startingLineup.add(extractTop(wingers)); // Midfielder 1
                startingLineup.add(extractTop(wingers)); // Midfielder 2
                startingLineup.add(extractTop(pivots));  // Forward
                break;

            case SQUARE:
                startingLineup.add(getBestFrom(fixos, wingers));  // Back 1
                startingLineup.add(getBestFrom(fixos, wingers));  // Back 2
                startingLineup.add(getBestFrom(wingers, pivots)); // Front 1
                startingLineup.add(getBestFrom(wingers, pivots)); // Front 2
                break;
        }

        substitutes.addAll(gks);
        substitutes.addAll(fixos);
        substitutes.addAll(wingers);
        substitutes.addAll(pivots);

        team.setStartingLineup(startingLineup);
        team.setSubstitutes(substitutes);
    }

    /**
     * Filters players by a given position and sorts them by overall rating in descending order.
     *
     * @param players The list of players to filter.
     * @param pos     The position to filter by.
     * @return A sorted list of players matching the position.
     */
    private static List<Player> filterAndSort(List<Player> players, Position pos) {
        return players.stream()
                .filter(p -> p.getPosition() == pos)
                .sorted(Comparator.comparingInt(Player::getOverallRating).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Removes and returns the top-rated player from the list.
     *
     * @param list The list to extract the player from.
     * @return The highest-rated player in the list.
     */
    private static Player extractTop(List<Player> list) {
        if (list.isEmpty()) throw new IllegalStateException("Not enough players for formation.");
        return list.remove(0);
    }

    /**
     * Selects the best player from two combined position lists and removes them from the original list.
     *
     * @param list1 First list of players (e.g., FIXO or WINGER).
     * @param list2 Second list of players (e.g., WINGER or PIVOT).
     * @return The highest-rated player from both lists.
     */
    private static Player getBestFrom(List<Player> list1, List<Player> list2) {
        List<Player> combined = new ArrayList<>(list1);
        combined.addAll(list2);

        if (combined.isEmpty()) throw new IllegalStateException("No players available for this role.");

        // Sort combined list by rating
        combined.sort(Comparator.comparingInt(Player::getOverallRating).reversed());

        // Get the best-rated player
        Player best = combined.get(0);

        // Remove the player from whichever original list they belong to
        if (!list1.remove(best)) list2.remove(best);

        return best;
    }

    private static void validateTeamRoster(List<Player> players, Tactic tactic) {
        if (players.size() < 5) {
            throw new IllegalArgumentException("A team must have at least 5 players.");
        }

        // Check for duplicates
        Set<String> uniqueIds = new HashSet<>();
        for (Player p : players) {
            if (!uniqueIds.add(p.getId())) {
                throw new IllegalArgumentException("Duplicate player detected: " + p.getName());
            }
        }

        long gkCount = players.stream().filter(p -> p.getPosition() == Position.GOALKEEPER).count();
        if (gkCount == 0) {
            throw new IllegalArgumentException("Team must have at least one goalkeeper.");
        }

        long fieldPlayers = players.size() - gkCount;
        if (fieldPlayers < 4) {
            throw new IllegalArgumentException("Team must have at least 4 field players.");
        }

        // Optional warning
        Map<Position, Long> countByPosition = players.stream()
                .collect(Collectors.groupingBy(Player::getPosition, Collectors.counting()));

        for (Position pos : Position.values()) {
            countByPosition.putIfAbsent(pos, 0L);
        }

        if (countByPosition.get(Position.PIVOT) == 0 || countByPosition.get(Position.WINGER) < 2) {
            System.out.println("[Warning] Unbalanced squad: consider adding more PIVOTS or WINGERS.");
        }
    }
}
