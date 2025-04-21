package com.sal.fm.util;

import com.sal.fm.model.Match;
import com.sal.fm.model.league.League;
import com.sal.fm.model.team.Team;

import java.util.List;

public class DebugTools {
    public static void simulateDebugMatch(League league) {
        List<Team> teams = league.getTeams();
        if (teams.size() < 2) {
            System.out.println("Not enough teams to simulate a debug match.");
            return;
        }

        Team teamA = DiceUtil.pickRandom(teams);
        Team teamB;
        do {
            teamB = DiceUtil.pickRandom(teams);
        } while (teamB == teamA);

        System.out.println("Debug Match: " + teamA.getName() + " vs " + teamB.getName());

        Match debugMatch = new Match(teamA, teamB);
        debugMatch.enableDebugMode();   // you can add a method to Match to toggle debug
        debugMatch.startMatch();        // this will already trigger logs + debug logs
    }
}
