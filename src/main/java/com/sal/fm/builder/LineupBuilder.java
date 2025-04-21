package com.sal.fm.builder;

import com.sal.fm.enums.Position;
import com.sal.fm.enums.Tactic;
import com.sal.fm.model.player.Player;
import com.sal.fm.model.team.Team;

import java.util.*;
import java.util.stream.Collectors;

/**
 * LineupBuilder is responsible for generating a valid starting lineup and substitutes
 * for a team based on its tactic (DIAMOND or SQUARE).
 * It ensures positional constraints are satisfied and players are selected based on their ratings.
 */
public class LineupBuilder {

    /**
     * Generates a starting lineup and substitutes for the given team.
     * It follows the tactic (DIAMOND or SQUARE) and ensures all positions are filled.
     */
    public static void generateLineup(Team team) {
        Tactic tactic = team.getTactic();
        List<Player> allPlayers = new ArrayList<>(team.getPlayers());

        validateTeamRoster(allPlayers);

        List<Player> startingLineup = new ArrayList<>();
        List<Player> substitutes;

        // Filter players by position and sort them by rating (descending)
        List<Player> gks = filterAndSort(allPlayers, Position.GOALKEEPER);
        List<Player> fixos = filterAndSort(allPlayers, Position.FIXO);
        List<Player> wingers = filterAndSort(allPlayers, Position.WINGER);
        List<Player> pivots = filterAndSort(allPlayers, Position.PIVOT);

        Set<String> selectedIds = new HashSet<>();

        // Always pick the top-rated goalkeeper
        Player gk = extractTop(gks);
        startingLineup.add(gk);
        selectedIds.add(gk.getId());

        // Build rest of the lineup based on tactic
        List<Player> rolePlayers = switch (tactic) {
            case DIAMOND -> buildDiamondLineup(fixos, wingers, pivots, selectedIds);
            case SQUARE -> buildSquareLineup(fixos, wingers, pivots, selectedIds);
        };

        startingLineup.addAll(rolePlayers);
        selectedIds.addAll(rolePlayers.stream().map(Player::getId).toList());

        // Remaining players become substitutes
        substitutes = allPlayers.stream()
                .filter(p -> !selectedIds.contains(p.getId()))
                .collect(Collectors.toList());

        team.setStartingLineup(startingLineup);
        team.setSubstitutes(substitutes);
    }

    /**
     * Builds a DIAMOND formation lineup.
     * Picks 1 FIXO, 2 WINGERS (or similar), and 1 PIVOT (or similar).
     */
    private static List<Player> buildDiamondLineup(List<Player> fixos, List<Player> wingers, List<Player> pivots, Set<String> selectedIds) {
        List<Player> lineup = new ArrayList<>();

        Player fixo = fixos.stream()
                .filter(p -> !selectedIds.contains(p.getId()))
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("No FIXO available"));

        lineup.add(fixo);
        selectedIds.add(fixo.getId());

        // Candidates for wings can be WINGERS or PIVOTS
        List<Player> wingCandidates = new ArrayList<>();
        wingers.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(wingCandidates::add);
        pivots.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(wingCandidates::add);

        wingCandidates.sort(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed());

        Player w1 = wingCandidates.get(0);
        Player w2 = wingCandidates.get(1);

        // Enforce at least one WINGER on the wing
        if (!(w1.getPosition() == Position.WINGER || w2.getPosition() == Position.WINGER)) {
            List<Player> availableWingers = wingers.stream()
                    .filter(p -> !selectedIds.contains(p.getId()))
                    .sorted(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed())
                    .toList();

            w1 = availableWingers.get(0);
            final String w1Id = w1.getId();
            w2 = wingCandidates.stream()
                    .filter(p -> !p.getId().equals(w1Id))
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("Not enough candidates for winger roles."));
        }

        lineup.add(w1);
        lineup.add(w2);
        selectedIds.addAll(List.of(w1.getId(), w2.getId()));

        // Choose one player for pivot role
        List<Player> pivotCandidates = new ArrayList<>();
        pivots.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(pivotCandidates::add);
        wingers.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(pivotCandidates::add);

        pivotCandidates.sort(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed());

        if (pivotCandidates.isEmpty()) {
            throw new IllegalStateException("No valid player available for pivot.");
        }

        Player pivot = pivotCandidates.get(0);
        lineup.add(pivot);
        selectedIds.add(pivot.getId());

        return lineup;
    }

    /**
     * Builds a SQUARE formation lineup.
     * Picks 2 backs (FIXO or PIVOT), 2 fronts (WINGER or PIVOT).
     * Ensures at least one WINGER is present in the front.
     */
    private static List<Player> buildSquareLineup(List<Player> fixos, List<Player> wingers, List<Player> pivots, Set<String> selectedIds) {
        List<Player> lineup = new ArrayList<>();

        // Back candidates: FIXO or PIVOT
        List<Player> backCandidates = new ArrayList<>();
        fixos.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(backCandidates::add);
        pivots.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(backCandidates::add);

        backCandidates.sort(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed());

        Player b1 = backCandidates.get(0);
        Player b2 = backCandidates.get(1);

        lineup.addAll(List.of(b1, b2));
        selectedIds.addAll(List.of(b1.getId(), b2.getId()));

        // Front candidates: WINGER or PIVOT
        List<Player> frontCandidates = new ArrayList<>();
        wingers.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(frontCandidates::add);
        pivots.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(frontCandidates::add);

        frontCandidates.sort(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed());

        Player f1 = frontCandidates.get(0);
        Player f2 = frontCandidates.get(1);

        // Ensure at least one WINGER
        if (!(f1.getPosition() == Position.WINGER || f2.getPosition() == Position.WINGER)) {
            List<Player> availableWingers = wingers.stream()
                    .filter(p -> !selectedIds.contains(p.getId()))
                    .sorted(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed())
                    .toList();

            if (availableWingers.isEmpty()) {
                throw new IllegalStateException("Square formation requires at least one WINGER.");
            }

            Player forcedWinger = availableWingers.get(0);
            if (f1.getOverallRating(f1.getPosition()) < f2.getOverallRating(f2.getPosition())) {
                f1 = forcedWinger;
            } else {
                f2 = forcedWinger;
            }
        }

        lineup.addAll(List.of(f1, f2));
        selectedIds.addAll(List.of(f1.getId(), f2.getId()));

        return lineup;
    }

    /**
     * Filters and sorts players by position and overall rating.
     */
    private static List<Player> filterAndSort(List<Player> players, Position pos) {
        return players.stream()
                .filter(p -> p.getPosition() == pos)
                .sorted(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed())
                .collect(Collectors.toList());
    }

    /**
     * Extracts and removes the top-rated player from a list.
     */
    private static Player extractTop(List<Player> list) {
        if (list.isEmpty()) throw new IllegalStateException("Not enough players for formation.");
        return list.remove(0);
    }

    /**
     * Validates that the team has enough unique players to form a valid lineup.
     * Warns if squad lacks sufficient PIVOTS or WINGERS.
     */
    private static void validateTeamRoster(List<Player> players) {
        if (players.size() < 5)
            throw new IllegalArgumentException("A team must have at least 5 players.");

        Set<String> uniqueIds = new HashSet<>();
        for (Player p : players) {
            if (!uniqueIds.add(p.getId())) {
                throw new IllegalArgumentException("Duplicate player detected: " + p.getName());
            }
        }

        long gkCount = players.stream().filter(p -> p.getPosition() == Position.GOALKEEPER).count();
        if (gkCount == 0)
            throw new IllegalArgumentException("Team must have at least one goalkeeper.");

        if ((players.size() - gkCount) < 4)
            throw new IllegalArgumentException("Team must have at least 4 field players.");

        long pivotCount = players.stream().filter(p -> p.getPosition() == Position.PIVOT).count();
        long wingerCount = players.stream().filter(p -> p.getPosition() == Position.WINGER).count();

        if (pivotCount == 0 || wingerCount < 2) {
            System.out.println("[Warning] Unbalanced squad: consider adding more PIVOTS or WINGERS.");
        }
    }
}