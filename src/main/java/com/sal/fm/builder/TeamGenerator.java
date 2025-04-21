package com.sal.fm.builder;

import com.sal.fm.enums.Position;
import com.sal.fm.enums.Tactic;
import com.sal.fm.model.team.Team;
import com.sal.fm.util.DiceUtil;

import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

/**
 * Responsible for generating a list of teams for a league.
 * Each team gets a name, a predefined tactic, and a set of players with a valid lineup.
 */
public class TeamGenerator {
    private static final List<String> teamNames = new ArrayList<>();
    private static final Random random = new Random();

    // Load team names from file on class initialization
    static {
        loadNames("data/teamNames.txt", teamNames);
    }

    /**
     * Loads team names from a text file into the target list.
     */
    private static void loadNames(String filePath, List<String> targetList) {
        try (InputStream inputStream = TeamGenerator.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.out.println("\u26a0 Team names file not found: " + filePath);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    targetList.add(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates 12 teams with distinct names, alternating tactics, and valid lineups.
     * @return list of fully formed teams
     */
    public static List<Team> generateLeagueTeams() {
        List<Team> teams = new ArrayList<>();

        if (teamNames.size() < 12) {
            throw new IllegalStateException("You must have at least 12 team names in teamNames.txt");
        }

        for (int i = 0; i < 12; i++) {
            String name = teamNames.get(i);
            Tactic tactic = (i % 2 == 0) ? Tactic.DIAMOND : Tactic.SQUARE; // Alternate tactics

            Team team = new Team(name, tactic);

            // Add essential players by position
            team.addPlayer(PlayerGenerator.generatePlayer(Position.GOALKEEPER));
            team.addPlayer(PlayerGenerator.generatePlayer(Position.GOALKEEPER));

            team.addPlayer(PlayerGenerator.generatePlayer(Position.FIXO));
            team.addPlayer(PlayerGenerator.generatePlayer(Position.FIXO));

            team.addPlayer(PlayerGenerator.generatePlayer(Position.PIVOT));
            team.addPlayer(PlayerGenerator.generatePlayer(Position.PIVOT));

            team.addPlayer(PlayerGenerator.generatePlayer(Position.WINGER));
            team.addPlayer(PlayerGenerator.generatePlayer(Position.WINGER));

            // Add two more random field players (excluding GK)
            Position[] fieldPositions = {Position.FIXO, Position.PIVOT, Position.WINGER};
            for (int j = 0; j < 2; j++) {
                Position randomPos = DiceUtil.pickRandom(Arrays.asList(fieldPositions));
                team.addPlayer(PlayerGenerator.generatePlayer(randomPos));
            }

            // Generate the starting lineup and bench
            LineupBuilder.generateLineup(team);
            teams.add(team);
        }

        return teams;
    }
}