package com.sal.fm.data;

import com.sal.fm.enums.Position;
import com.sal.fm.model.Player;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class PlayerGenerator {
    private static final List<String> firstNames = new ArrayList<>();
    private static final List<String> lastNames = new ArrayList<>();

    static {
        loadNames("data/firstNames.txt", firstNames);
        loadNames("data/lastNames.txt", lastNames);
    }

    private static final Random random = new Random();

    // Method to load names from a file
    private static void loadNames(String filePath, List<String> targetList) {
        try (InputStream inputStream = PlayerGenerator.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.out.println("File not found in resources: " + filePath);
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

    public static Player generateRandomPlayer(Position position) {
        String name = firstNames.get(random.nextInt(firstNames.size())) + " " +
                lastNames.get(random.nextInt(lastNames.size()));

        int age = 18 + random.nextInt(18); // 18–35
        int skill = generateWeightedSkill(); // custom skill generator
        int stamina = 50 + random.nextInt(61); // 50–100

        return new Player(name, age, position, skill, stamina);
    }

    private static int generateWeightedSkill() {
        int roll = random.nextInt(100); // 0–99

        if (roll < 60) { // 60%
            return 72 + random.nextInt(15); // 72–86
        } else if (roll < 80) { // next 20%
            return 59 + random.nextInt(13); // 59–71
        } else if (roll < 90) { // next 10%
            return 87 + random.nextInt(3); // 87–89
        } else if (roll < 96) { // next 6%
            return 90 + random.nextInt(7); // 90–96
        } else { // last 4%
            return 97 + random.nextInt(3); // 97–99
        }
    }

    public static List<Player> generateTeamPlayers() {
        List<Player> team = new ArrayList<>();

        // Generate goalkeepers (at least 2)
        team.add(generateRandomPlayer(Position.GOALKEEPER));
        team.add(generateRandomPlayer(Position.GOALKEEPER));

        // Generate defenders (at least 2)
        for (int i = 0; i < 2; i++) {
            team.add(generateRandomPlayer(Position.FIXO));
        }

        // Generate Pivots (at least 2)
        for (int i = 0; i < 2; i++) {
            team.add(generateRandomPlayer(Position.PIVOT));
        }

        // Generate Wingers (at least 2)
        for (int i = 0; i < 2; i++) {
            team.add(generateRandomPlayer(Position.WINGER));
        }

        // Generate 2 more random players to complete the team
        for (int i = 0; i < 2; i++) {
            // Exclude the Goalkeeper position by selecting randomly from the other positions
            Position[] fieldPositions = {Position.FIXO, Position.PIVOT, Position.WINGER};
            Position randomPosition = fieldPositions[random.nextInt(fieldPositions.length)];
            team.add(generateRandomPlayer(randomPosition));
        }

        return team;
    }
}
