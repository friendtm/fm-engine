package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Represents a player's physical characteristics.
 * These affect movement, endurance, strength, and body control in match simulation.
 */
public class PhysicalAttributes {

    // === Core Physical Traits ===

    private int pace;             // Top-end running speed over distance
    private int acceleration;     // Quickness to reach top speed
    private int balance;          // Stability in movement and when challenged
    private int strength;         // Physical power in duels and shielding
    private int stamina;          // Ability to maintain effort throughout the match
    private int jumpingReach;     // Effectiveness in aerial duels
    private int agility;          // Quick changes in direction and reaction
    private int naturalFitness;   // Long-term conditioning and recovery rate

    /**
     * Default constructor (required for Jackson serialization).
     */
    public PhysicalAttributes() {
    }

    /**
     * Constructs a complete set of physical attributes.
     *
     * @param pace            top running speed
     * @param acceleration    how quickly the player reaches speed
     * @param balance         ability to stay upright in motion or contact
     * @param strength        muscular force and power
     * @param stamina         physical energy over time
     * @param jumpingReach    height and timing for aerials
     * @param agility         responsiveness and movement control
     * @param naturalFitness  recovery rate and training resilience
     */
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

    // === Getters ===

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

    /**
     * Computes the average of all physical attributes.
     *
     * @return rounded average on a 0–100 scale
     */
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
