package com.sal.fm.builder;

import com.sal.fm.enums.Position;
import com.sal.fm.model.player.*;
import com.sal.fm.util.DiceUtil;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

/**
 * Responsible for generating random players with realistic attributes based on their position.
 * Utilizes external name files and random dice rolls to simulate real player diversity.
 */
public class PlayerGenerator {

    private static final List<String> firstNames = new ArrayList<>();
    private static final List<String> lastNames = new ArrayList<>();

    // Load name files once on class load
    static {
        loadNames("data/firstNames.txt", firstNames);
        loadNames("data/lastNames.txt", lastNames);
    }

    /**
     * Loads names from a resource file into a list.
     */
    private static void loadNames(String filePath, List<String> targetList) {
        try (InputStream inputStream = PlayerGenerator.class.getClassLoader().getResourceAsStream(filePath)) {
            if (inputStream == null) {
                System.out.println("File not found: " + filePath);
                return;
            }

            try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream))) {
                String line;
                while ((line = reader.readLine()) != null) {
                    targetList.add(line);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * Generates a single player for a given position with randomized stats and name.
     */
    public static Player generatePlayer(Position position) {
        String name = DiceUtil.pickRandom(firstNames) + " " + DiceUtil.pickRandom(lastNames);
        int age = DiceUtil.roll(18, 35);
        PlayerStats stats = generateStats(position);

        return new Player(name, age, position, stats);
    }

    /**
     * Generates a full squad of 12 players with a reasonable positional distribution.
     */
    public static List<Player> generateTeamPlayers() {
        List<Player> players = new ArrayList<>();

        // 2 Goalkeepers
        players.add(generatePlayer(Position.GOALKEEPER));
        players.add(generatePlayer(Position.GOALKEEPER));

        // 2 Fixos
        players.add(generatePlayer(Position.FIXO));
        players.add(generatePlayer(Position.FIXO));

        // 2 Pivots
        players.add(generatePlayer(Position.PIVOT));
        players.add(generatePlayer(Position.PIVOT));

        // 2 Wingers
        players.add(generatePlayer(Position.WINGER));
        players.add(generatePlayer(Position.WINGER));

        // 4 more random field players
        Position[] fieldPositions = {Position.FIXO, Position.PIVOT, Position.WINGER};
        for (int i = 0; i < 4; i++) {
            Position randomPos = DiceUtil.pickRandom(List.of(fieldPositions));
            players.add(generatePlayer(randomPos));
        }

        return players;
    }

    /**
     * Generates appropriate stats for a given position using dedicated stat profiles.
     */
    public static PlayerStats generateStats(Position position) {
        return switch (position) {
            case GOALKEEPER -> generateGoalkeeperStats();
            case FIXO -> generateFixoStats();
            case WINGER -> generateWingerStats();
            case PIVOT -> generatePivotStats();
        };
    }

    // === POSITIONAL PROFILES ===

    private static PlayerStats generateFixoStats() {
        return new PlayerStats(
                generateDefensiveTechnicals(),
                generateStrongPhysicals(),
                generateBalancedMentals(),
                generateHiddenAttributes(),
                generateBasicGKAttributes()
        );
    }

    private static PlayerStats generateWingerStats() {
        return new PlayerStats(
                generateWingerTechnicals(),
                generateFastPhysicals(),
                generateCreativeMentals(),
                generateHiddenAttributes(),
                generateBasicGKAttributes()
        );
    }

    private static PlayerStats generatePivotStats() {
        return new PlayerStats(
                generateAttackerTechnicals(),
                generatePowerfulPhysicals(),
                generateAggressiveMentals(),
                generateHiddenAttributes(),
                generateBasicGKAttributes()
        );
    }

    private static PlayerStats generateGoalkeeperStats() {
        return new PlayerStats(
                generateMinimalTechnicals(),
                generateGKPhysicals(),
                generateBalancedMentals(),
                generateHiddenAttributes(),
                generateGoalkeepingSkills()
        );
    }

    // === TECHNICAL ATTRIBUTES ===

    private static TechnicalAttributes generateDefensiveTechnicals() {
        return new TechnicalAttributes(
                DiceUtil.roll(1, 10),  // corners
                DiceUtil.roll(1, 10),  // penalty
                DiceUtil.roll(40, 60), // free kick
                DiceUtil.roll(30, 50), // shooting
                DiceUtil.roll(20, 40), // long shots
                DiceUtil.roll(50, 70), // heading
                DiceUtil.roll(50, 70), // passing
                DiceUtil.roll(30, 50), // dribbling
                DiceUtil.roll(60, 85), // tackling
                DiceUtil.roll(50, 75)  // technique
        );
    }

    private static TechnicalAttributes generateWingerTechnicals() {
        return new TechnicalAttributes(
                DiceUtil.roll(30, 70), DiceUtil.roll(50, 80), DiceUtil.roll(40, 80),
                DiceUtil.roll(60, 85), DiceUtil.roll(65, 90), DiceUtil.roll(40, 65),
                DiceUtil.roll(60, 85), DiceUtil.roll(75, 95), DiceUtil.roll(30, 50), DiceUtil.roll(70, 95)
        );
    }

    private static TechnicalAttributes generateAttackerTechnicals() {
        return new TechnicalAttributes(
                DiceUtil.roll(20, 40), DiceUtil.roll(60, 85), DiceUtil.roll(40, 70),
                DiceUtil.roll(70, 90), DiceUtil.roll(55, 80), DiceUtil.roll(65, 85),
                DiceUtil.roll(60, 80), DiceUtil.roll(60, 75), DiceUtil.roll(40, 60), DiceUtil.roll(60, 85)
        );
    }

    private static TechnicalAttributes generateMinimalTechnicals() {
        return new TechnicalAttributes(
                1, 1, DiceUtil.roll(10, 30), DiceUtil.roll(10, 30), DiceUtil.roll(10, 30),
                DiceUtil.roll(10, 30), DiceUtil.roll(30, 50), DiceUtil.roll(20, 40), DiceUtil.roll(20, 40), DiceUtil.roll(20, 40)
        );
    }

    // === PHYSICAL ATTRIBUTES ===

    private static PhysicalAttributes generateStrongPhysicals() {
        return new PhysicalAttributes(
                DiceUtil.roll(30, 60), DiceUtil.roll(30, 60), DiceUtil.roll(60, 90),
                DiceUtil.roll(70, 95), DiceUtil.roll(50, 80), DiceUtil.roll(60, 85),
                DiceUtil.roll(40, 60), DiceUtil.roll(60, 90)
        );
    }

    private static PhysicalAttributes generateFastPhysicals() {
        return new PhysicalAttributes(
                DiceUtil.roll(80, 100), DiceUtil.roll(80, 100), DiceUtil.roll(60, 80),
                DiceUtil.roll(40, 60), DiceUtil.roll(60, 80), DiceUtil.roll(60, 85),
                DiceUtil.roll(75, 100), DiceUtil.roll(60, 85)
        );
    }

    private static PhysicalAttributes generatePowerfulPhysicals() {
        return new PhysicalAttributes(
                DiceUtil.roll(50, 75), DiceUtil.roll(50, 70), DiceUtil.roll(70, 95),
                DiceUtil.roll(80, 100), DiceUtil.roll(50, 80), DiceUtil.roll(60, 85),
                DiceUtil.roll(40, 70), DiceUtil.roll(70, 90)
        );
    }

    private static PhysicalAttributes generateGKPhysicals() {
        return new PhysicalAttributes(
                DiceUtil.roll(50, 70), DiceUtil.roll(50, 70), DiceUtil.roll(60, 80),
                DiceUtil.roll(50, 75), DiceUtil.roll(50, 70), DiceUtil.roll(65, 85),
                DiceUtil.roll(50, 75), DiceUtil.roll(65, 85)
        );
    }

    // === MENTAL ATTRIBUTES ===

    private static MentalAttributes generateBalancedMentals() {
        return new MentalAttributes(
                DiceUtil.roll(50, 80), DiceUtil.roll(50, 80), DiceUtil.roll(50, 80),
                DiceUtil.roll(50, 80), DiceUtil.roll(50, 80), DiceUtil.roll(50, 80),
                DiceUtil.roll(50, 80), DiceUtil.roll(50, 80), DiceUtil.roll(50, 80), DiceUtil.roll(50, 80)
        );
    }

    private static MentalAttributes generateCreativeMentals() {
        return new MentalAttributes(
                DiceUtil.roll(70, 95), DiceUtil.roll(60, 90), DiceUtil.roll(50, 80),
                DiceUtil.roll(40, 70), DiceUtil.roll(50, 80), DiceUtil.roll(60, 90),
                DiceUtil.roll(50, 70), DiceUtil.roll(60, 85), DiceUtil.roll(80, 100), DiceUtil.roll(65, 85)
        );
    }

    private static MentalAttributes generateAggressiveMentals() {
        return new MentalAttributes(
                DiceUtil.roll(50, 80), DiceUtil.roll(50, 80), DiceUtil.roll(50, 80),
                DiceUtil.roll(60, 90), DiceUtil.roll(50, 80), DiceUtil.roll(60, 85),
                DiceUtil.roll(85, 100), DiceUtil.roll(80, 95), DiceUtil.roll(50, 70), DiceUtil.roll(60, 85)
        );
    }

    // === HIDDEN ATTRIBUTES ===

    private static HiddenAttributes generateHiddenAttributes() {
        return new HiddenAttributes(
                DiceUtil.roll(50, 100), DiceUtil.roll(40, 90), DiceUtil.roll(40, 100),
                DiceUtil.roll(50, 90), DiceUtil.roll(40, 90), DiceUtil.roll(50, 100)
        );
    }

    // === GOALKEEPING ATTRIBUTES ===

    private static GoalkeepingAttributes generateGoalkeepingSkills() {
        return new GoalkeepingAttributes(
                DiceUtil.roll(60, 90), DiceUtil.roll(60, 90), DiceUtil.roll(60, 90), DiceUtil.roll(60, 90),
                DiceUtil.roll(60, 90), DiceUtil.roll(60, 90), DiceUtil.roll(60, 90), DiceUtil.roll(60, 90),
                DiceUtil.roll(60, 90), DiceUtil.roll(60, 90)
        );
    }

    private static GoalkeepingAttributes generateBasicGKAttributes() {
        return new GoalkeepingAttributes(
                DiceUtil.roll(20, 60), DiceUtil.roll(1, 20), DiceUtil.roll(10, 40), DiceUtil.roll(1, 30),
                DiceUtil.roll(20, 50), DiceUtil.roll(1, 10), DiceUtil.roll(20, 50), DiceUtil.roll(1, 30),
                DiceUtil.roll(1, 30), DiceUtil.roll(1, 20)
        );
    }
}