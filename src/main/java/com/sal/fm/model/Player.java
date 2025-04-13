package com.sal.fm.model;

import com.sal.fm.enums.Position;

import java.util.UUID;

public class Player {
    private final String id; // Unique identifier
    private String name;
    private int age;
    private Position position;
    private int skill;
    private int stamina;
    private int fitness;
    private boolean isInjured;

    public Player(String name, int age, Position position, int skill, int stamina) {
        this.id = UUID.randomUUID().toString(); // Unique ID generated
        this.name = name;
        this.age = age;
        this.position = position;
        this.skill = skill;
        this.stamina = stamina;
        this.fitness = 100;
        this.isInjured = false;
    }

    public int getOverallRating(){
        return (int)((skill * 0.6) + (stamina * 0.4));
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

    public int getSkill() {
        return skill;
    }

    public int getStamina() {
        return stamina;
    }

    public int getFitness() {
        return fitness;
    }

    public boolean isInjured() {
        return isInjured;
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
