package com.sal.fm;

import com.sal.fm.data.LineupBuilder;
import com.sal.fm.data.PlayerGenerator;
import com.sal.fm.enums.Tactic;
import com.sal.fm.manager.SaveManager;
import com.sal.fm.model.*;
import com.sal.fm.model.table.LeagueTableEntry;

import java.util.*;

public class Main {
    static int currentDay = 1;
    static int currentMatchday = 1;
    static int lastMatchdayDay = 0;
    static League league;
    static GameState gameState;

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        if (SaveManager.saveExists()) {
            System.out.println("Loading saved game...");
            gameState = SaveManager.load();
            league = gameState.getLeague();
            currentDay = gameState.getCurrentDay();
            currentMatchday = gameState.getCurrentMatchday();
            lastMatchdayDay = gameState.getLastMatchdayDay();
        } else {
            System.out.println("No save found. Creating new league...");
            List<Team> teams = generateLeagueTeams();
            league = new League(teams);
            gameState = new GameState(league, currentDay, currentMatchday);
            SaveManager.save(gameState);
        }

        boolean running = true;
        while (running) {
            System.out.println("\n==== Football Match Engine ====");
            System.out.println("Day: " + currentDay + " | Matchday: " + currentMatchday);
            System.out.println("1. Next Turn");
            System.out.println("2. Show Matchday Schedule");
            System.out.println("3. List Teams");
            System.out.println("4. Manage a Team");
            System.out.println("5. Save League");
            System.out.println("6. Show League Table");
            System.out.println("7. Exit Game");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> {
                    if (currentDay - lastMatchdayDay > 2) {
                        System.out.println("⚠️ Warning: Matchday skipped due to mid-turn save/load.");
                    }

                    currentDay++;
                    System.out.println("New day: " + currentDay);

                    if (currentDay - lastMatchdayDay >= 2) {
                        System.out.println("Matchday " + currentMatchday + " begins!");
                        simulateMatchday(league, currentMatchday);
                        currentMatchday++;
                        lastMatchdayDay = currentDay;
                    }
                }
                case 2 -> showMatchdaySchedule(league, currentMatchday);
                case 3 -> listTeams(league);
                case 4 -> manageTeam(scanner, league);
                case 5 -> {
                    saveGame();
                    System.out.println("✔ League saved!");
                }
                case 6 -> displayLeagueTable(league);
                case 7 -> {
                    saveGame();
                    System.out.println("✔ Progress saved. Goodbye!");
                    running = false;
                }
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private static void simulateMatchday(League league, int matchday) {

        if (matchday > 22) {
            System.out.println("🏁 The season is over. No more matchdays.");
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
            match.enableSilentMode(); // <- quiet mode
            match.startMatch();
            match.markAsPlayed();

            System.out.printf("%s %d - %d %s%n",
                    match.getHomeTeam().getName(),
                    match.getTeamAScore(),
                    match.getTeamBScore(),
                    match.getAwayTeam().getName());
        }

        displayLeagueTable(league); // ← show updated standings
        saveGame();
    }

    private static void showMatchdaySchedule(League league, int matchday) {
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

    private static void listTeams(League league) {
        System.out.println("\n=== League Teams ===");
        for (int i = 0; i < league.getTeams().size(); i++) {
            Team team = league.getTeams().get(i);
            System.out.printf("[%02d] %s (Tactic: %s)%n", i, team.getName(), team.getTactic());
        }
    }

    private static void manageTeam(Scanner scanner, League league) {
        listTeams(league);
        System.out.print("Enter team number to manage: ");
        int index = scanner.nextInt();
        if (index < 0 || index >= league.getTeams().size()) {
            System.out.println("Invalid team number.");
            return;
        }

        Team team = league.getTeams().get(index);
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

        saveGame();
    }

    private static void printLineup(Team team) {
        List<Player> startingLineup = team.getStartingLineup();
        List<Player> substitutes = team.getSubstitutes();
        List<Player> all = team.getPlayers();

        System.out.println("### Full Roster (sorted) ###");
        all.stream()
                .sorted((a, b) -> Integer.compare(b.getOverallRating(), a.getOverallRating()))
                .forEach(p -> System.out.println("[R: " + p.getOverallRating() + "] " + p.getName() + " | " + p.getPosition()));

        System.out.println("\n--- Lineup ---");
        startingLineup.forEach(p -> System.out.println("[R: " + p.getOverallRating() + "] " + p.getName() + " | " + p.getPosition()));

        System.out.println("\n--- Substitutes ---");
        substitutes.forEach(p -> System.out.println("[R: " + p.getOverallRating() + "] " + p.getName() + " | " + p.getPosition()));

        System.out.println();
    }

    private static List<Team> generateLeagueTeams() {
        List<Team> teams = new ArrayList<>();
        String[] teamNames = {
                "Red Wolves", "Blue Hawks", "Green Eagles", "Black Panthers",
                "Silver Foxes", "Golden Bulls", "White Tigers", "Crimson Bears",
                "Shadow Lions", "Iron Rhinos", "Storm Crows", "Fire Serpents"
        };

        for (int i = 0; i < 12; i++) {
            Tactic tactic = (i % 2 == 0) ? Tactic.DIAMOND : Tactic.SQUARE;
            Team team = new Team(teamNames[i], tactic);
            PlayerGenerator.generateTeamPlayers().forEach(team::addPlayer);
            LineupBuilder.generateLineup(team);
            teams.add(team);
        }

        return teams;
    }

    private static void displayLeagueTable(League league) {
        Map<String, LeagueTableEntry> table = new HashMap<>();

        // Initialize entries for all teams
        for (Team team : league.getTeams()) {
            table.put(team.getName(), new LeagueTableEntry(team));
        }

        // Process match results
        for (Match match : league.getMatches()) {
            if (!match.isPlayed()) continue;

            Team home = match.getHomeTeam();
            Team away = match.getAwayTeam();
            int homeScore = match.getTeamAScore();
            int awayScore = match.getTeamBScore();

            table.get(home.getName()).recordResult(homeScore, awayScore, true);
            table.get(away.getName()).recordResult(homeScore, awayScore, false);
        }

        // Sort and display
        List<LeagueTableEntry> sorted = table.values().stream()
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

    private static void saveGame() {
        gameState.setLeague(league);
        gameState.setCurrentDay(currentDay);
        gameState.setCurrentMatchday(currentMatchday);
        gameState.setLastMatchdayDay(lastMatchdayDay);
        SaveManager.save(gameState);
    }
}
