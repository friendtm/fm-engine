package com.sal.fm.manager;

import com.sal.fm.util.JsonUtil;
import com.sal.fm.model.GameState;

import java.io.File;

public class SaveManager {
    private static final String SAVE_PATH = "saves/save.json";

    public static boolean saveExists() {
        return new File(SAVE_PATH).exists();
    }

    public static void save(GameState state) {
        JsonUtil.saveToFile(SAVE_PATH, state);
    }

    public static GameState load() {
        return JsonUtil.loadFromFile(SAVE_PATH, GameState.class);
    }
}
