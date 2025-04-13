package com.sal.fm;

import com.sal.fm.data.LineupBuilder;
import com.sal.fm.data.PlayerGenerator;
import com.sal.fm.enums.Tactic;
import com.sal.fm.model.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        // Create and setup teams
        Team teamA = createTeam("Team A", Tactic.DIAMOND);
        Team teamB = createTeam("Team B", Tactic.DIAMOND);

        // Debug output for team lineups and substitutes
        printLineup(teamA, "A");
        printLineup(teamB, "B");

        // Start the match simulation
        startMatch(teamA, teamB);
    }

    private static Team createTeam(String teamName, Tactic tactic) {
        Team team = new Team(teamName, tactic);
        PlayerGenerator.generateTeamPlayers().forEach(team::addPlayer);
        LineupBuilder.generateLineup(team); // Ensure lineup is built
        return team;
    }

    private static void printLineup(Team team, String teamName) {
        List<Player> startingLineup = team.getStartingLineup();
        List<Player> substitutes = team.getSubstitutes();

        System.out.println("### Team " + teamName + " Lineup ###");
        startingLineup.forEach(p -> System.out.println(p.getName() + " | Pos: " + p.getPosition() + " | R: " + p.getSkill()));
        System.out.println();

        System.out.println("### Team " + teamName + " Substitutes ###");
        substitutes.forEach(p -> System.out.println(p.getName() + " | Pos: " + p.getPosition() + " | R: " + p.getSkill()));
        System.out.println();
    }

    private static void startMatch(Team teamA, Team teamB) {
        Match match = new Match(teamA, teamB);
        System.out.println("Match Started!");
        System.out.println();
        match.startMatch();
    }
}
