package com.sal.fm.core;

import com.sal.fm.model.GameState;
import com.sal.fm.util.JsonUtil;

import java.io.File;

/**
 * Handles low-level persistence logic using JSON serialization.
 * Responsible for saving and loading the game state to/from a JSON file.
 */
public class SaveManager {

    // Path to the JSON file used for saving the game state
    private static final String SAVE_PATH = "saves/save.json";

    /**
     * Checks whether the save file exists at the designated path.
     *
     * @return true if save.json exists, false otherwise
     */
    public static boolean saveExists() {
        return new File(SAVE_PATH).exists();
    }

    /**
     * Serializes the given GameState and writes it to the save file.
     *
     * @param state the game state to persist
     */
    public static void save(GameState state) {
        JsonUtil.saveToFile(SAVE_PATH, state);
    }

    /**
     * Loads and deserializes the GameState from the save file.
     *
     * @return the loaded GameState instance
     */
    public static GameState load() {
        return JsonUtil.loadFromFile(SAVE_PATH, GameState.class);
    }
}
