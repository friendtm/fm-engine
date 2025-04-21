package com.sal.fm.core;

import com.sal.fm.model.GameState;

/**
 * Utility class that simplifies interaction with SaveManager.
 * Acts as a convenience layer for loading, saving, and exiting the game.
 */
public class SaveHelper {

    /**
     * Checks if a save file currently exists on disk.
     *
     * @return true if a save file is found, false otherwise
     */
    public static boolean hasSave() {
        return SaveManager.saveExists();
    }

    /**
     * Loads a previously saved GameState from disk.
     *
     * @return the deserialized GameState
     */
    public static GameState load() {
        return SaveManager.load();
    }

    /**
     * Saves the current GameState to disk.
     *
     * @param gameState the game state to be saved
     */
    public static void save(GameState gameState) {
        SaveManager.save(gameState);
        System.out.println("✔ Game saved!");
    }

    /**
     * Saves the current GameState and exits the application.
     *
     * @param gameState the game state to save before exiting
     */
    public static void saveAndExit(GameState gameState) {
        save(gameState);
        System.out.println("✔ Progress saved. Goodbye!");
        System.exit(0);
    }
}
