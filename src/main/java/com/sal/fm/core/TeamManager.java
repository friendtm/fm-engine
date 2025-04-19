package com.sal.fm.core;

import com.sal.fm.builder.LineupBuilder;
import com.sal.fm.builder.PlayerGenerator;
import com.sal.fm.model.GameState;
import com.sal.fm.model.player.Player;
import com.sal.fm.model.team.Team;

import java.util.Comparator;
import java.util.List;
import java.util.Scanner;

public class TeamManager {

    public static void manageTeam(GameState state, Scanner scanner) {
        List<Team> teams = state.getLeague().getTeams();

        listTeams(teams);
        System.out.print("Enter team number to manage: ");
        int index = scanner.nextInt();

        if (index < 0 || index >= teams.size()) {
            System.out.println("Invalid team number.");
            return;
        }

        Team team = teams.get(index);
        System.out.println("Managing " + team.getName());
        System.out.println("1. Generate New Players");
        System.out.println("2. Show Lineup");
        System.out.print("Choose: ");
        int action = scanner.nextInt();

        switch (action) {
            case 1 -> {
                team.getPlayers().clear();
                PlayerGenerator.generateTeamPlayers().forEach(team::addPlayer);
                LineupBuilder.generateLineup(team);
                System.out.println("✔ New players generated.");
            }
            case 2 -> printLineup(team);
            default -> System.out.println("Invalid option.");
        }
    }

    public static void listTeams(List<Team> teams) {
        System.out.println("\n=== League Teams ===");
        for (int i = 0; i < teams.size(); i++) {
            Team team = teams.get(i);
            System.out.printf("[%02d] %s (Tactic: %s)%n", i, team.getName(), team.getTactic());
        }
    }

    private static void printLineup(Team team) {
        List<Player> all = team.getPlayers();
        List<Player> starters = team.getStartingLineup();
        List<Player> subs = team.getSubstitutes();

        System.out.println("### Full Roster (sorted) ###");
        all.stream()
                .sorted(Comparator.comparingInt((Player p) -> p.getOverallRating(p.getPosition())).reversed())
                .forEach(p -> System.out.println("[R: " + p.getOverallRating(p.getPosition()) + "] " + p.getName() + " | " + p.getPosition()));

        System.out.println("\n--- Lineup ---");
        starters.forEach(p -> System.out.println("[R: " + p.getOverallRating(p.getPosition()) + "] " + p.getName() + " | " + p.getPosition()));

        System.out.println("\n--- Substitutes ---");
        subs.forEach(p -> System.out.println("[R: " + p.getOverallRating(p.getPosition()) + "] " + p.getName() + " | " + p.getPosition()));

        System.out.println();
    }
}