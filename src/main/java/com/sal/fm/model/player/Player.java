package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

import com.sal.fm.enums.Position;

import java.util.UUID;

public class Player {
    private String id;
    private String name;
    private int age;
    private Position position;
    private PlayerStats stats;
    private boolean isInjured;

    public Player() {
        this.id = UUID.randomUUID().toString();
    }

    public Player(String name, int age, Position position, PlayerStats stats) {
        this();
        this.name = name;
        this.age = age;
        this.position = position;
        this.stats = stats;
    }

    // Getters & Setters

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

    @JsonIgnore
    public int getOverallRating(Position position) {
        if (position == Position.GOALKEEPER) {
            return stats.getAverageGoalkeepingSkill();
        } else {
            return stats.getAverageOutfieldSkill();
        }
    }

    public void setInjured(boolean injured) {
        isInjured = injured;
    }

    @Override
    public String toString() {
        return name + " | Pos: " + position + " | Age: " + age + " | Rating: " + getOverallRating(position);
    }

    // Optional: Equality by ID only
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
