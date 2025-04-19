package com.sal.fm.model.player;

public class PlayerStats {

    private TechnicalAttributes technical;
    private PhysicalAttributes physical;
    private MentalAttributes mental;
    private HiddenAttributes hidden;
    private GoalkeepingAttributes goalkeeping;

    public PlayerStats() {
        // Default constructor for serialization
    }

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

    public int getAverageOutfieldSkill() {
        return (technical.getPassing() + technical.getDribbling() + technical.getShooting() + technical.getTechnique()) / 4;
    }

    public int getAverageGoalkeepingSkill() {
        return goalkeeping.getAverage();
    }
}