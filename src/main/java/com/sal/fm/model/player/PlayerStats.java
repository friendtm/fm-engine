package com.sal.fm.model.player;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * Container for all player attributes grouped by category:
 * - Technical
 * - Physical
 * - Mental
 * - Hidden (personality)
 * - Goalkeeping
 *
 * Provides utility methods to compute role-specific averages.
 */
public class PlayerStats {

    private TechnicalAttributes technical;
    private PhysicalAttributes physical;
    private MentalAttributes mental;
    private HiddenAttributes hidden;
    private GoalkeepingAttributes goalkeeping;

    /**
     * Default constructor for serialization frameworks (e.g., Jackson).
     */
    public PlayerStats() {
    }

    /**
     * Constructs a PlayerStats object with all attribute categories initialized.
     *
     * @param technical     ball control, passing, tackling, etc.
     * @param physical      pace, stamina, strength, etc.
     * @param mental        vision, decisions, aggression, etc.
     * @param hidden        personality traits like professionalism or pressure handling
     * @param goalkeeping   attributes used only by goalkeepers
     */
    public PlayerStats(TechnicalAttributes technical,
                       PhysicalAttributes physical,
                       MentalAttributes mental,
                       HiddenAttributes hidden,
                       GoalkeepingAttributes goalkeeping) {
        this.technical = technical;
        this.physical = physical;
        this.mental = mental;
        this.hidden = hidden;
        this.goalkeeping = goalkeeping;
    }

    // === Getters and Setters ===

    public TechnicalAttributes getTechnical() {
        return technical;
    }

    public void setTechnical(TechnicalAttributes technical) {
        this.technical = technical;
    }

    public PhysicalAttributes getPhysical() {
        return physical;
    }

    public void setPhysical(PhysicalAttributes physical) {
        this.physical = physical;
    }

    public MentalAttributes getMental() {
        return mental;
    }

    public void setMental(MentalAttributes mental) {
        this.mental = mental;
    }

    public HiddenAttributes getHidden() {
        return hidden;
    }

    public void setHidden(HiddenAttributes hidden) {
        this.hidden = hidden;
    }

    public GoalkeepingAttributes getGoalkeeping() {
        return goalkeeping;
    }

    public void setGoalkeeping(GoalkeepingAttributes goalkeeping) {
        this.goalkeeping = goalkeeping;
    }

    /**
     * Computes a simplified average for outfield performance.
     * Focuses on passing, dribbling, shooting, and technique.
     *
     * @return average score between 0–100 for use in player evaluations
     */
    @JsonIgnore
    public int getAverageOutfieldSkill() {
        return (technical.getPassing() +
                technical.getDribbling() +
                technical.getShooting() +
                technical.getTechnique()) / 4;
    }

    /**
     * Returns the goalkeeper-specific overall skill score.
     *
     * @return average based on goalkeeping attributes
     */
    @JsonIgnore
    public int getAverageGoalkeepingSkill() {
        return goalkeeping.getAverage();
    }
}
