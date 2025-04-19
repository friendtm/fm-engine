package com.sal.fm.core;

import com.sal.fm.config.MatchConfig;
import com.sal.fm.core.MatchSimulator;
import com.sal.fm.core.SaveManager;
import com.sal.fm.model.GameState;
import com.sal.fm.model.league.League;
import com.sal.fm.model.league.LeagueTableEntry;
import com.sal.fm.model.Match;
import com.sal.fm.model.team.Team;

import com.sal.fm.util.JsonUtil;

import java.util.*;

public class GameLoop {

    private final Scanner scanner = new Scanner(System.in);
    private GameState gameState;

    public void start() {
        // Load or create game state
        if (SaveHelper.hasSave()) {
            System.out.println("Loading saved game...");
            gameState = SaveHelper.load();
        } else {
            System.out.println("No save found. Starting a new game...");
            gameState = GameInitializer.initializeNewGame();
            SaveHelper.save(gameState);
        }

        // Main loop
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
                default -> System.out.println("Invalid option. Try again.");
            }
        }
    }

    private void handleNextTurn() {
        int currentDay = gameState.getCurrentDay() + 1;
        gameState.setCurrentDay(currentDay);
        System.out.println("New day: " + currentDay);

        if (currentDay - gameState.getLastMatchdayDay() >= MatchConfig.DAYS_BETWEEN_MATCHDAYS) {
            int matchday = gameState.getCurrentMatchday();
            System.out.println("Matchday " + matchday + " begins!");
            MatchSimulator.simulateMatchday(gameState);
            gameState.setCurrentMatchday(matchday + 1);
            gameState.setLastMatchdayDay(currentDay);
        }
    }

    private void handleShowSchedule() {
        UIPrinter.showMatchdaySchedule(gameState.getLeague().getMatches(), gameState.getCurrentMatchday());
    }

    private void handleListTeams() {
        TeamManager.listTeams(gameState.getLeague().getTeams());
    }

    private void handleManageTeam() {
        TeamManager.manageTeam(gameState, scanner);
    }

    private void handleShowTable() {
        List<LeagueTableEntry> table = MatchSimulator.buildLeagueTable(gameState.getLeague());
        UIPrinter.displayLeagueTable(table);
    }
}