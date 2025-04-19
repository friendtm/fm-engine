package com.sal.fm.util;

import java.util.List;
import java.util.Random;

public class DiceUtil {
    private static final Random random = new Random();

    /**
     * Returns a random int between min and max (inclusive)
     */
    public static int roll(int min, int max) {
        return min + random.nextInt(max - min + 1);
    }

    /**
     * Returns a random double between 0 and 1
     */
    public static double chance() {
        return random.nextDouble();
    }

    /**
     * Returns true with a given probability between 0.0 and 1.0 (e.g., 0.75 = 75% chance)
     */
    public static boolean chance(double probability) {
        return random.nextDouble() < probability;
    }

    /**
     * Returns true with a probability of x% (0–100)
     */
    public static boolean rollPercent(double percentChance) {
        return chance() < percentChance / 100.0;
    }

    /**
     * Picks a random element from an array
     */
    public static <T> T pickRandom(T[] array) {
        return array[random.nextInt(array.length)];
    }

    /**
     * Picks a random element from a list
     */
    public static <T> T pickRandom(List<T> list) {
        if (list == null || list.isEmpty()) {
            throw new IllegalArgumentException("List must not be null or empty");
        }
        return list.get(random.nextInt(list.size()));
    }

    /**
     * Simulates a duel where one side wins based on relative weight.
     * Example: attacker = 70, defender = 30 → 70% chance attacker wins
     */
    public static boolean successCheck(int attackerScore, int defenderScore) {
        return roll(0, attackerScore + defenderScore - 1) < attackerScore;
    }

}