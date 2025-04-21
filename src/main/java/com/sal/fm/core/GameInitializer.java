package com.sal.fm.core;

import com.sal.fm.builder.TeamGenerator;
import com.sal.fm.model.GameState;
import com.sal.fm.model.league.League;
import com.sal.fm.model.team.Team;

import java.util.List;

/**
 * Responsible for initializing a brand-new game state.
 * This class sets up all necessary components like teams and the league.
 */
public class GameInitializer {

    /**
     * Creates a new GameState instance with 12 generated teams and a league.
     *
     * @return the initialized GameState with matchday = 1 and day = 1
     */
    public static GameState initializeNewGame() {
        // Generate league teams with default tactics and rosters
        List<Team> teams = TeamGenerator.generateLeagueTeams();

        // Initialize league with those teams
        League league = new League(teams);

        // Create and return the full GameState
        return new GameState(league, 1, 1);  // Matchday 1, Day 1
    }
}
