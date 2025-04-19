package com.sal.fm.core;

import com.sal.fm.core.SaveManager;
import com.sal.fm.model.GameState;

public class SaveHelper {

    public static boolean hasSave() {
        return SaveManager.saveExists();
    }

    public static GameState load() {
        return SaveManager.load();
    }

    public static void save(GameState gameState) {
        SaveManager.save(gameState);
        System.out.println("✔ Game saved!");
    }

    public static void saveAndExit(GameState gameState) {
        save(gameState);
        System.out.println("✔ Progress saved. Goodbye!");
        System.exit(0);
    }
}