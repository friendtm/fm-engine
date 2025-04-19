package com.sal.fm.engine;

import com.sal.fm.enums.MatchEvent;
import com.sal.fm.enums.Position;
import com.sal.fm.model.Match;
import com.sal.fm.model.player.Player;
import com.sal.fm.model.team.Team;
import com.sal.fm.util.DiceUtil;
import com.sal.fm.util.MatchLogger;
import com.sal.fm.config.MatchConfig;

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
        int skill = attacker.getStats().getTechnical().getDribbling();
        int creativity = (int) ((passing * 0.6 + skill * 0.4));

        int defendingScore = defender.getStats().getTechnical().getTackling();
        if ((attacker.getPosition() == Position.WINGER || attacker.getPosition() == Position.PIVOT)
                && attacker.getStats().getMental().getWorkRate() >= MatchConfig.HIGH_WORKRATE_THRESHOLD) {
            defendingScore += 5;
        }

        boolean success = DiceUtil.successCheck(creativity, defendingScore);

        if (debugMode) {
            String result = success ? "✅ Success" : "❌ Intercepted";
            logger.logDebug(minute, "🛡️ PASS DUEL: " + attacker.getName() + " (PASS: " + passing + ", SKILL: " + skill + ") vs " +
                    defender.getName() + " (DEF: " + defender.getStats().getTechnical().getTackling() + ") → " + result);
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

        boolean onTarget = DuelResolver.isShotOnTarget(shooter);
        boolean goal = false;

        if (onTarget) {
            goal = DuelResolver.isGoal(shooter, goalkeeper);
            if (goal) {
                match.scoreGoal(shooter, attacking);
                logger.log(minute, shooter.getName() + " scores for " + attacking.getName() + "!", silentMode);
            } else {
                logger.log(minute, goalkeeper.getName() + " makes a big save for " + defending.getName(), silentMode);
            }
        } else {
            logger.log(minute, shooter.getName() + " fires wide!", silentMode);
        }

        if (debugMode) {
            String result = !onTarget ? "❌ Missed" : (goal ? "✅ Goal" : "🧤 Saved");
            logger.logDebug(minute,
                    "🎯 SHOT DUEL: " + shooter.getName() + " (SHOOT: " + shooter.getStats().getTechnical().getShooting() +
                            ", SKILL: " + shooter.getStats().getTechnical().getDribbling() + ", PACE: " + shooter.getStats().getPhysical().getPace() + ") vs " +
                            goalkeeper.getName() + " (GK: " + goalkeeper.getStats().getGoalkeeping().getAverage() + ") → " + result);
        }
    }

    private List<Player> getFieldPlayers(Team team) {
        return team.getStartingLineup().stream()
                .filter(p -> p.getPosition() != Position.GOALKEEPER)
                .collect(Collectors.toList());
    }
}