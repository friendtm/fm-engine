package com.sal.fm.engine;

import com.sal.fm.config.MatchConfig;
import com.sal.fm.enums.MatchEvent;
import com.sal.fm.enums.Position;
import com.sal.fm.model.Match;
import com.sal.fm.model.player.Player;
import com.sal.fm.model.team.Team;
import com.sal.fm.util.DiceUtil;
import com.sal.fm.util.MatchLogger;

import java.util.List;
import java.util.stream.Collectors;

public class MatchEngine {
    private final Match match;
    private final MatchLogger logger;
    private final boolean debugMode;

    private boolean homeMomentum = false;
    private boolean awayMomentum = false;

    public MatchEngine(Match match, MatchLogger logger, boolean debugMode) {
        this.match = match;
        this.logger = logger;
        this.debugMode = debugMode;
    }

    public void simulateMinute(int currentMinute, boolean silentMode) {
        MatchEvent event;

        if (homeMomentum || awayMomentum) {
            event = DiceUtil.rollPercent(MatchConfig.MOMENTUM_SHOT_WEIGHT) ? MatchEvent.SHOT_ON_GOAL : MatchEvent.PASS_OR_TACKLE;
        } else {
            event = DiceUtil.rollPercent(MatchConfig.NORMAL_PASS_WEIGHT) ? MatchEvent.PASS_OR_TACKLE : MatchEvent.SHOT_ON_GOAL;
        }

        homeMomentum = false;
        awayMomentum = false;

        switch (event) {
            case PASS_OR_TACKLE -> handlePassOrTackle(currentMinute, silentMode);
            case SHOT_ON_GOAL -> handleShotOnGoal(currentMinute, silentMode);
        }
    }

    private void handlePassOrTackle(int minute, boolean silentMode) {
        Team attacking = DiceUtil.rollPercent(50) ? match.getHomeTeam() : match.getAwayTeam();
        Team defending = attacking == match.getHomeTeam() ? match.getAwayTeam() : match.getHomeTeam();

        List<Player> attackers = getFieldPlayers(attacking);
        List<Player> defenders = getFieldPlayers(defending);

        if (attackers.isEmpty() || defenders.isEmpty()) return;

        Player attacker = DiceUtil.pickRandom(attackers);
        Player defender = DiceUtil.pickRandom(defenders);

        int passing = attacker.getStats().getTechnical().getPassing();
        int technique = attacker.getStats().getTechnical().getTechnique();
        int vision = attacker.getStats().getMental().getVision();
        double attackerScore = passing * 0.5 + technique * 0.3 + vision * 0.2;

        int tackling = defender.getStats().getTechnical().getTackling();
        double defenderScore = tackling * 1.0;

        boolean success = attackerScore > defenderScore || DiceUtil.successCheck((int) attackerScore, (int) defenderScore);

        if (debugMode) {
            logger.logDebug(minute, DuelResolver.buildPassingDebugLog(attacker, defender, success, attackerScore, defenderScore));
        }

        if (success) {
            logger.log(minute, attacker.getName() + " breaks through the press!", silentMode);
        } else {
            logger.log(minute, defender.getName() + " wins the ball for " + defending.getName(), silentMode);
            if (defending == match.getHomeTeam()) homeMomentum = true;
            else awayMomentum = true;

            logger.log(minute, defending.getName() + " builds momentum after a strong tackle!", silentMode);
        }
    }

    private void handleShotOnGoal(int minute, boolean silentMode) {
        Team attacking = DiceUtil.rollPercent(50) ? match.getHomeTeam() : match.getAwayTeam();
        Team defending = attacking == match.getHomeTeam() ? match.getAwayTeam() : match.getHomeTeam();

        List<Player> shooters = getFieldPlayers(attacking);
        List<Player> goalkeepers = defending.getStartingLineup().stream()
                .filter(p -> p.getPosition() == Position.GOALKEEPER)
                .collect(Collectors.toList());

        if (shooters.isEmpty() || goalkeepers.isEmpty()) return;

        Player shooter = DiceUtil.pickRandom(shooters);
        Player goalkeeper = goalkeepers.get(0);

        boolean underPressure = DiceUtil.rollPercent(35);
        boolean onTarget = DuelResolver.isShotOnTarget(shooter);
        boolean goal = false;

        if (onTarget) {
            double chance = AttributeCalculator.calculateShotSuccessChance(shooter, goalkeeper);
            if (underPressure) chance -= MatchConfig.PRESSURE_BALANCE_PENALTY;
            goal = DiceUtil.chance(chance);

            if (goal) {
                match.scoreGoal(shooter, attacking);
                logger.log(minute, shooter.getName() + " scores for " + attacking.getName() + "!", silentMode);
            } else {
                logger.log(minute, goalkeeper.getName() + " makes a big save for " + defending.getName(), silentMode);
            }

            if (debugMode) {
                String shotLog = DuelResolver.buildShootingDebugLog(shooter, goalkeeper, chance, goal, underPressure);
                logger.logDebug(minute, shotLog);
            }
        } else {
            logger.log(minute, shooter.getName() + " fires wide!", silentMode);
            if (debugMode) {
                logger.logDebug(minute, "❌ Shot missed by " + shooter.getName());
            }
        }
    }

    private List<Player> getFieldPlayers(Team team) {
        return team.getStartingLineup().stream()
                .filter(p -> p.getPosition() != Position.GOALKEEPER)
                .collect(Collectors.toList());
    }
}