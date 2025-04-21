package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class PhysicalAttributes {
    public int pace;
    public int acceleration;
    public int balance;
    public int strength;
    public int stamina;
    public int jumpingReach;
    public int agility;
    public int naturalFitness;

    public PhysicalAttributes() {
        // required for Jackson
    }

    public PhysicalAttributes(int pace, int acceleration, int balance, int strength, int stamina,
                              int jumpingReach, int agility, int naturalFitness) {
        this.pace = pace;
        this.acceleration = acceleration;
        this.balance = balance;
        this.strength = strength;
        this.stamina = stamina;
        this.jumpingReach = jumpingReach;
        this.agility = agility;
        this.naturalFitness = naturalFitness;
    }

    public int getPace() {
        return pace;
    }

    public int getAcceleration() {
        return acceleration;
    }

    public int getBalance() {
        return balance;
    }

    public int getStrength() {
        return strength;
    }

    public int getStamina() {
        return stamina;
    }

    public int getJumpingReach() {
        return jumpingReach;
    }

    public int getAgility() {
        return agility;
    }

    public int getNaturalFitness() {
        return naturalFitness;
    }

    @JsonIgnore
    public int getAverage() {
        int sum =
                pace +
                        acceleration +
                        balance +
                        strength +
                        stamina +
                        jumpingReach +
                        agility +
                        naturalFitness;

        return sum / 8;
    }
}
