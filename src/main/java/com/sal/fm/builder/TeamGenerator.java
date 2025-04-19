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

public class TeamGenerator {
    private static final List<String> teamNames = new ArrayList<>();
    private static final Random random = new Random();

    static {
        loadNames("data/teamNames.txt", teamNames);
    }

    private static void loadNames(String filePath, List<String> targetList) {
        try (InputStream inputStream = TeamGenerator.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.out.println("⚠ Team names file not found: " + filePath);
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

    public static List<Team> generateLeagueTeams() {
        List<Team> teams = new ArrayList<>();

        if (teamNames.size() < 12) {
            throw new IllegalStateException("You must have at least 12 team names in teamNames.txt");
        }

        for (int i = 0; i < 12; i++) {
            String name = teamNames.get(i);
            Tactic tactic = (i % 2 == 0) ? Tactic.DIAMOND : Tactic.SQUARE;

            Team team = new Team(name, tactic);

            // 2 Goalkeepers
            team.addPlayer(PlayerGenerator.generatePlayer(Position.GOALKEEPER));
            team.addPlayer(PlayerGenerator.generatePlayer(Position.GOALKEEPER));

            // 2 Fixos
            team.addPlayer(PlayerGenerator.generatePlayer(Position.FIXO));
            team.addPlayer(PlayerGenerator.generatePlayer(Position.FIXO));

            // 2 Pivots
            team.addPlayer(PlayerGenerator.generatePlayer(Position.PIVOT));
            team.addPlayer(PlayerGenerator.generatePlayer(Position.PIVOT));

            // 2 Wingers
            team.addPlayer(PlayerGenerator.generatePlayer(Position.WINGER));
            team.addPlayer(PlayerGenerator.generatePlayer(Position.WINGER));

            // 2 more random field players (excluding GK)
            Position[] fieldPositions = {Position.FIXO, Position.PIVOT, Position.WINGER};
            for (int j = 0; j < 2; j++) {
                Position randomPos = DiceUtil.pickRandom(Arrays.asList(fieldPositions));
                team.addPlayer(PlayerGenerator.generatePlayer(randomPos));
            }

            LineupBuilder.generateLineup(team);
            teams.add(team);
        }

        return teams;
    }
}