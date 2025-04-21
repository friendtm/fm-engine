package com.sal.fm;

import com.sal.fm.core.GameLoop;

/**
 * Entry point for the Football Match Engine Simulation.
 *
 * Initializes and starts the main game loop.
 */
public class Main {

    /**
     * Application startup method.
     *
     * @param args command-line arguments (unused)
     */
    public static void main(String[] args) {
        // Start the main game loop which handles simulation and user interaction
        new GameLoop().start();
    }
}
