package com.sal.fm.manager;

import com.sal.fm.model.Team;
import com.sal.fm.util.JsonUtil;

import java.io.File;

public class SaveManager {
    private static final String SAVE_PATH_A = "saves/teamA.json";
    private static final String SAVE_PATH_B = "saves/teamB.json";

    public static boolean saveExists() {
        return new File(SAVE_PATH_A).exists() && new File(SAVE_PATH_B).exists();
    }

    public static void save(Team teamA, Team teamB) {
        JsonUtil.saveToFile(SAVE_PATH_A, teamA);
        JsonUtil.saveToFile(SAVE_PATH_B, teamB);
    }

    public static Team[] load() {
        Team teamA = JsonUtil.loadFromFile(SAVE_PATH_A, Team.class);
        Team teamB = JsonUtil.loadFromFile(SAVE_PATH_B, Team.class);
        return new Team[]{teamA, teamB};
    }
}
