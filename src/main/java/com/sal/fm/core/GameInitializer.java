package com.sal.fm.core;

import com.sal.fm.builder.LineupBuilder;
import com.sal.fm.builder.PlayerGenerator;
import com.sal.fm.builder.TeamGenerator;
import com.sal.fm.enums.Tactic;
import com.sal.fm.model.*;
import com.sal.fm.model.league.League;
import com.sal.fm.model.team.Team;

import java.util.ArrayList;
import java.util.List;

public class GameInitializer {

    public static GameState initializeNewGame() {
        List<Team> teams = TeamGenerator.generateLeagueTeams();  // ← this does everything
        League league = new League(teams);
        return new GameState(league, 1, 1);
    }
}