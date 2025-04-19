package com.sal.fm.builder;

import com.sal.fm.enums.Position;
import com.sal.fm.enums.Tactic;
import com.sal.fm.model.player.Player;
import com.sal.fm.model.team.Team;

import java.util.*;
import java.util.stream.Collectors;

public class LineupBuilder {

    public static void generateLineup(Team team) {
        Tactic tactic = team.getTactic();
        List<Player> allPlayers = new ArrayList<>(team.getPlayers());

        validateTeamRoster(allPlayers);

        List<Player> startingLineup = new ArrayList<>();
        List<Player> substitutes;

        List<Player> gks = filterAndSort(allPlayers, Position.GOALKEEPER);
        List<Player> fixos = filterAndSort(allPlayers, Position.FIXO);
        List<Player> wingers = filterAndSort(allPlayers, Position.WINGER);
        List<Player> pivots = filterAndSort(allPlayers, Position.PIVOT);

        Set<String> selectedIds = new HashSet<>();

        Player gk = extractTop(gks);
        startingLineup.add(gk);
        selectedIds.add(gk.getId());

        List<Player> rolePlayers = switch (tactic) {
            case DIAMOND -> buildDiamondLineup(fixos, wingers, pivots, selectedIds);
            case SQUARE -> buildSquareLineup(fixos, wingers, pivots, selectedIds);
        };

        startingLineup.addAll(rolePlayers);
        selectedIds.addAll(rolePlayers.stream().map(Player::getId).toList());

        substitutes = allPlayers.stream()
                .filter(p -> !selectedIds.contains(p.getId()))
                .collect(Collectors.toList());

        team.setStartingLineup(startingLineup);
        team.setSubstitutes(substitutes);
    }

    private static List<Player> buildDiamondLineup(List<Player> fixos, List<Player> wingers, List<Player> pivots, Set<String> selectedIds) {
        List<Player> lineup = new ArrayList<>();

        List<Player> availableFixos = fixos.stream()
                .filter(p -> !selectedIds.contains(p.getId()))
                .sorted(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed())
                .toList();

        Player fixo = availableFixos.get(0);
        lineup.add(fixo);
        selectedIds.add(fixo.getId());

        List<Player> wingCandidates = new ArrayList<>();
        wingers.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(wingCandidates::add);
        pivots.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(wingCandidates::add);

        wingCandidates.sort(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed());

        Player w1 = wingCandidates.get(0);
        Player w2 = wingCandidates.get(1);

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

    private static List<Player> buildSquareLineup(List<Player> fixos, List<Player> wingers, List<Player> pivots, Set<String> selectedIds) {
        List<Player> lineup = new ArrayList<>();

        List<Player> backCandidates = new ArrayList<>();
        fixos.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(backCandidates::add);
        pivots.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(backCandidates::add);

        backCandidates.sort(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed());

        Player b1 = backCandidates.get(0);
        Player b2 = backCandidates.get(1);

        lineup.addAll(List.of(b1, b2));
        selectedIds.addAll(List.of(b1.getId(), b2.getId()));

        List<Player> frontCandidates = new ArrayList<>();
        wingers.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(frontCandidates::add);
        pivots.stream().filter(p -> !selectedIds.contains(p.getId())).forEach(frontCandidates::add);

        frontCandidates.sort(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed());

        if (frontCandidates.size() < 2) {
            throw new IllegalStateException("Not enough players for front line.");
        }

        Player f1 = frontCandidates.get(0);
        Player f2 = frontCandidates.get(1);

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

    private static List<Player> filterAndSort(List<Player> players, Position pos) {
        return players.stream()
                .filter(p -> p.getPosition() == pos)
                .sorted(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed())
                .collect(Collectors.toList());
    }

    private static Player extractTop(List<Player> list) {
        if (list.isEmpty()) throw new IllegalStateException("Not enough players for formation.");
        return list.remove(0);
    }

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
