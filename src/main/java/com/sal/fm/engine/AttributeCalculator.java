package com.sal.fm.engine;

import com.sal.fm.model.player.*;

/**
 * Calculates contextual attribute scores for players in various match situations
 */
public class AttributeCalculator {

    // === SHOT QUALITY ===
    public static int calculateShotScore(Player p) {
        TechnicalAttributes tech = p.getStats().getTechnical();
        PhysicalAttributes phys = p.getStats().getPhysical();
        MentalAttributes mental = p.getStats().getMental();

        int shooting = tech.getShooting();
        int technique = tech.getTechnique();
        int composure = mental.getComposure();
        int balance = phys.getBalance();
        int strength = phys.getStrength();

        // Basic shot score
        return (int) ((shooting * 0.4 + technique * 0.25 + composure * 0.2 + balance * 0.1 + strength * 0.05));
    }

    public static int calculateLongShotScore(Player p) {
        TechnicalAttributes tech = p.getStats().getTechnical();
        return (int) ((calculateShotScore(p) + tech.getLongShots() * 0.4) / 1.4);
    }

    // === PASS QUALITY ===
    public static int calculatePassScore(Player p) {
        TechnicalAttributes tech = p.getStats().getTechnical();
        MentalAttributes mental = p.getStats().getMental();

        int passing = tech.getPassing();
        int technique = tech.getTechnique();
        int vision = mental.getVision();

        return (int) ((passing * 0.5 + technique * 0.3 + vision * 0.2));
    }

    // === TACKLE QUALITY ===
    public static int calculateDefensiveScore(Player p) {
        TechnicalAttributes tech = p.getStats().getTechnical();
        MentalAttributes mental = p.getStats().getMental();

        int tackling = tech.getTackling();
        int aggression = mental.getAggression();
        int positioning = mental.getPositioning();

        return (int) ((tackling * 0.5 + aggression * 0.2 + positioning * 0.3));
    }

    // === GOALKEEPING QUALITY ===
    public static int calculateGoalkeeperSaveScore(Player p) {
        GoalkeepingAttributes gk = p.getStats().getGoalkeeping();
        MentalAttributes mental = p.getStats().getMental();

        int reflexes = gk.getReflexes();
        int handling = gk.getHandling();
        int positioning = mental.getPositioning();

        return (int) ((reflexes * 0.4 + handling * 0.3 + positioning * 0.3));
    }
}