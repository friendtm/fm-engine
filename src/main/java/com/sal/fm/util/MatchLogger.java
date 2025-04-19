package com.sal.fm.util;

import java.io.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class MatchLogger {
    private static final String LOGS_FOLDER = "logs/";

    private PrintWriter fileWriter;
    private PrintWriter debugWriter;

    public MatchLogger(String matchId) {
        try {
            File dir = new File(LOGS_FOLDER);
            if (!dir.exists()) {
                dir.mkdirs();
            }

            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss"));
            String matchFile = LOGS_FOLDER + "match_" + matchId + "_" + timestamp + ".log";
            String debugFile = LOGS_FOLDER + "debug_" + matchId + "_" + timestamp + ".log";

            fileWriter = new PrintWriter(new FileWriter(matchFile, true));
            debugWriter = new PrintWriter(new FileWriter(debugFile, true));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void log(int minute, String message, boolean silent) {
        String formatted = String.format("[%02d'] %s", minute, message);
        fileWriter.println(formatted);
        fileWriter.flush();

        if (!silent) {
            System.out.println(formatted);
        }
    }

    public void logDebug(int minute, String message) {
        String formatted = String.format("[%02d'] %s", minute, message);
        debugWriter.println(formatted);
        debugWriter.flush();
    }

    public void close() {
        if (fileWriter != null) fileWriter.close();
        if (debugWriter != null) debugWriter.close();
    }
}