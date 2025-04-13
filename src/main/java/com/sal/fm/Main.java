package com.sal.fm;

import com.sal.fm.util.JsonUtil;
import com.sal.fm.data.LineupBuilder;
import com.sal.fm.data.PlayerGenerator;
import com.sal.fm.enums.Tactic;
import com.sal.fm.model.*;
import com.sal.fm.manager.SaveManager;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Scanner;
import java.util.TreeMap;
import java.util.stream.Collectors;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Team teamA, teamB;

        if (SaveManager.saveExists()) {
            System.out.println("Save found! Loading teams...");
            Team[] loaded = SaveManager.load();
            teamA = loaded[0];
            teamB = loaded[1];
        } else {
            System.out.println("No save found. Creating new teams...");
            teamA = createTeam("Team A", Tactic.DIAMOND);
            teamB = createTeam("Team B", Tactic.SQUARE);
            SaveManager.save(teamA, teamB);
        }

        boolean running = true;
        while (running) {
            System.out.println("\n==== Football Match Engine ====");
            System.out.println("1. Run Match");
            System.out.println("2. Generate New Players for Team A");
            System.out.println("3. Generate New Players for Team B");
            System.out.println("4. Generate New Players for Both Teams");
            System.out.println("5. Print Team A Players");
            System.out.println("6. Print Team B Players");
            System.out.println("7. Print Both Teams");
            System.out.println("8. Create League");
            System.out.println("9. Exit Game");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    Match match = new Match(teamA, teamB);
                    match.startMatch();
                }
                case 2 -> {
                    teamA = createTeam("Team A", teamA.getTactic());
                    SaveManager.save(teamA, teamB);
                    System.out.println("✔ New players generated for Team A.");
                }
                case 3 -> {
                    teamB = createTeam("Team B", teamB.getTactic());
                    SaveManager.save(teamA, teamB);
                    System.out.println("✔ New players generated for Team B.");
                }
                case 4 -> {
                    teamA = createTeam("Team A", teamA.getTactic());
                    teamB = createTeam("Team B", teamB.getTactic());
                    SaveManager.save(teamA, teamB);
                    System.out.println("✔ New players generated for both teams.");
                }
                case 5 -> printLineup(teamA, "A");
                case 6 -> printLineup(teamB, "B");
                case 7 -> {
                    printLineup(teamA, "A");
                    printLineup(teamB, "B");
                }
                case 8 -> {
                    List<Team> my12Teams = generateLeagueTeams();
                    League league = new League(my12Teams);

                    league.getMatches().stream()
                            .sorted(Comparator.comparingInt(Match::getRound))
                            .collect(Collectors.groupingBy(Match::getRound, TreeMap::new, Collectors.toList()))
                            .forEach((round, matchList) -> {
                                String run = (round <= 11) ? "Run 1" : "Run 2";
                                System.out.println("\n=== Matchday " + round + " [" + run + "] ===");
                                matchList.forEach(match ->
                                        System.out.println(match.getHomeTeam().getName() + " vs " + match.getAwayTeam().getName()));
                            });
                }
                case 9 -> {
                    SaveManager.save(teamA, teamB);
                    System.out.println("Progress saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static Team createTeam(String teamName, Tactic tactic) {
        Team team = new Team(teamName, tactic);
        PlayerGenerator.generateTeamPlayers().forEach(team::addPlayer);
        LineupBuilder.generateLineup(team); // Ensure lineup is built
        return team;
    }

    private static void printLineup(Team team, String teamName) {
        List<Player> startingLineup = team.getStartingLineup();
        List<Player> substitutes = team.getSubstitutes();
        List<Player> all = team.getPlayers();

        System.out.println("### Team " + teamName + " Full Roster (sorted) ###");
        all.stream()
                .sorted((a, b) -> Integer.compare(b.getOverallRating(), a.getOverallRating()))
                .forEach(p -> System.out.println("[R: " + p.getOverallRating() + "] " + p.getName() + " | Pos: " + p.getPosition()));

        System.out.println("\n--- Lineup ---");
        startingLineup.forEach(p -> System.out.println("[R: " + p.getOverallRating() + "] " + p.getName() + " | Pos: " + p.getPosition()));

        System.out.println("\n--- Substitutes ---");
        substitutes.forEach(p -> System.out.println("[R: " + p.getOverallRating() + "] " + p.getName() + " | Pos: " + p.getPosition()));

        System.out.println();
    }

    private static void startMatch(Team teamA, Team teamB) {
        Match match = new Match(teamA, teamB);
        System.out.println("Match Started!");
        System.out.println();
        match.startMatch();
    }

    private static List<Team> generateLeagueTeams() {
        List<Team> teams = new ArrayList<>();
        String[] teamNames = {
                "Red Wolves", "Blue Hawks", "Green Eagles", "Black Panthers",
                "Silver Foxes", "Golden Bulls", "White Tigers", "Crimson Bears",
                "Shadow Lions", "Iron Rhinos", "Storm Crows", "Fire Serpents"
        };

        for (int i = 0; i < 12; i++) {
            Tactic tactic = (i % 2 == 0) ? Tactic.DIAMOND : Tactic.SQUARE; // Alternate tactics
            Team team = new Team(teamNames[i], tactic);
            PlayerGenerator.generateTeamPlayers().forEach(team::addPlayer);
            LineupBuilder.generateLineup(team);
            teams.add(team);
        }

        return teams;
    }
}
