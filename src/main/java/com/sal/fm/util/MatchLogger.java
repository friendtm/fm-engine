package com.sal.fm.util;

public class MatchLogger {
    public void log(int minute, String message) {
        System.out.printf("[%02d'] %s%n", minute, message);
    }
}
