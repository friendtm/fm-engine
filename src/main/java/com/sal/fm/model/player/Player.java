package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sal.fm.enums.Position;

import java.util.UUID;

/**
 * Represents a player in the simulation, including their position,
 * name, age, stats, and injury status.
 *
 * Each player has a unique UUID and is evaluated based on their position-specific ratings.
 */
public class Player {

    private String id;
    private String name;
    private int age;
    private Position position;
    private PlayerStats stats;
    private boolean isInjured;

    /**
     * Default constructor. Automatically assigns a unique ID.
     * Required for JSON deserialization.
     */
    public Player() {
        this.id = UUID.randomUUID().toString();
    }

    /**
     * Creates a fully initialized Player with identity, role, and stats.
     *
     * @param name     the player’s full name
     * @param age      player’s age
     * @param position their primary position (e.g., WINGER, FIXO, etc.)
     * @param stats    the full set of technical, mental, physical, etc. attributes
     */
    public Player(String name, int age, Position position, PlayerStats stats) {
        this();
        this.name = name;
        this.age = age;
        this.position = position;
        this.stats = stats;
    }

    // === Getters ===

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getAge() {
        return age;
    }

    public Position getPosition() {
        return position;
    }

    public PlayerStats getStats() {
        return stats;
    }

    public boolean getIsInjured() {
        return isInjured;
    }

    // === Setters ===

    public void setStats(PlayerStats stats) {
        this.stats = stats;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
    }

    /**
     * Computes the player's overall rating based on their position.
     * Goalkeepers are rated using goalkeeping attributes;
     * field players use a weighted average of outfield stats.
     *
     * @param position the role being evaluated
     * @return an integer rating between 1–100
     */
    @JsonIgnore
    public int getOverallRating(Position position) {
        if (position == Position.GOALKEEPER) {
            return stats.getAverageGoalkeepingSkill();
        } else {
            return stats.getAverageOutfieldSkill();
        }
    }

    /**
     * String representation used in debug/logs or UI.
     */
    @Override
    public String toString() {
        return name + " | Pos: " + position + " | Age: " + age + " | Rating: " + getOverallRating(position);
    }

    /**
     * Equality based solely on unique player ID.
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Player)) return false;
        Player other = (Player) obj;
        return id.equals(other.id);
    }

    @Override
    public int hashCode() {
        return id.hashCode();
    }
}
