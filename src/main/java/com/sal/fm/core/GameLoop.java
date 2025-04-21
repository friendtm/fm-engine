package com.sal.fm.core;

import com.sal.fm.config.MatchConfig;
import com.sal.fm.model.GameState;
import com.sal.fm.model.league.LeagueTableEntry;
import com.sal.fm.util.DebugTools;

import java.util.List;
import java.util.Scanner;

/**
 * Main game loop that handles user input, match progression,
 * viewing schedules, teams, and league standings.
 */
public class GameLoop {

    private final Scanner scanner = new Scanner(System.in);
    private GameState gameState;

    /**
     * Starts the main game loop. Handles loading or initializing game state,
     * then provides a text-based interface to interact with the simulation.
     */
    public void start() {
        // Load game if save exists, otherwise initialize a new one
        if (SaveHelper.hasSave()) {
            System.out.println("Loading saved game...");
            gameState = SaveHelper.load();
        } else {
            System.out.println("No save found. Starting a new game...");
            gameState = GameInitializer.initializeNewGame();
            SaveHelper.save(gameState);
        }

        // Main interaction loop
        boolean running = true;
        while (running) {
            System.out.println("\n==== Football Match Engine ====");
            System.out.println("Day: " + gameState.getCurrentDay() + " | Matchday: " + gameState.getCurrentMatchday());
            System.out.println("1. Next Turn");
            System.out.println("2. Show Matchday Schedule");
            System.out.println("3. List Teams");
            System.out.println("4. Manage a Team");
            System.out.println("5. Save League");
            System.out.println("6. Show League Table");
            System.out.println("7. Exit Game");
            System.out.println("99. Simulate Debug Match");
            System.out.print("Choose an option: ");

            int choice = scanner.nextInt();

            switch (choice) {
                case 1 -> handleNextTurn();
                case 2 -> handleShowSchedule();
                case 3 -> handleListTeams();
                case 4 -> handleManageTeam();
                case 5 -> SaveHelper.save(gameState);
                case 6 -> handleShowTable();
                case 7 -> {
                    SaveHelper.save(gameState);
                    System.out.println("✔ Progress saved. Goodbye!");
                    running = false;
                }
                case 99 -> DebugTools.simulateDebugMatch(gameState.getLeague());
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    /**
     * Advances the simulation by one day. Triggers a matchday every N days.
     */
    private void handleNextTurn() {
        int currentDay = gameState.getCurrentDay() + 1;
        gameState.setCurrentDay(currentDay);
        System.out.println("New day: " + currentDay);

        // Trigger matchday if interval reached
        if (currentDay - gameState.getLastMatchdayDay() >= MatchConfig.DAYS_BETWEEN_MATCHDAYS) {
            int matchday = gameState.getCurrentMatchday();
            System.out.println("Matchday " + matchday + " begins!");
            MatchSimulator.simulateMatchday(gameState);
            gameState.setCurrentMatchday(matchday + 1);
            gameState.setLastMatchdayDay(currentDay);
        }
    }

    /**
     * Displays upcoming or played matches for the current matchday.
     */
    private void handleShowSchedule() {
        UIPrinter.showMatchdaySchedule(gameState.getLeague().getMatches(), gameState.getCurrentMatchday());
    }

    /**
     * Displays all teams in the league with their tactics.
     */
    private void handleListTeams() {
        TeamManager.listTeams(gameState.getLeague().getTeams());
    }

    /**
     * Allows the user to view or regenerate players for a specific team.
     */
    private void handleManageTeam() {
        TeamManager.manageTeam(gameState, scanner);
    }

    /**
     * Displays the current league standings in table format.
     */
    private void handleShowTable() {
        List<LeagueTableEntry> table = MatchSimulator.buildLeagueTable(gameState.getLeague());
        UIPrinter.displayLeagueTable(table);
    }
}
