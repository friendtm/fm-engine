# Football Match Engine Simulation

A mini football (futsal-style) match simulation engine written in Java.  
Simulates realistic matches between AI teams, featuring simple tactical formations, player attributes, duels, league progression, and save/load support.

---

## Features

- **Stat-driven simulation engine**
    - Technical, Physical, Mental, and Goalkeeping attributes
    - Context-aware duels (pass, tackle, shot on goal)
- **Tactical formations**
    - Supports **DIAMOND** and **SQUARE** tactics with intelligent lineup selection
- **League system**
    - 12 teams
    - 22 matchdays (home/away double round-robin)
- **Match calendar**
    - Configurable match spacing (e.g. every 2 days)
    - Auto-scheduling with validation
- **League table tracking**
    - Wins, draws, losses, goals for/against, points
- **Save/load support**
    - Stores full simulation state to JSON (`/saves/save.json`)
- **Debug mode**
    - Detailed duel logging
    - Match logs saved to file

---

## Project Structure

The codebase is modular and organized by responsibility, following clean architecture principles:

| Package | Description |
|--------|-------------|
| `com.sal.fm` | The root package. Contains the `Main` class — entry point to launch the simulation. |
| `com.sal.fm.core` | Core gameplay logic: game loop (`GameLoop`), save system (`SaveHelper`), state manager, and matchday simulation (`MatchSimulator`). |
| `com.sal.fm.builder` | Logic for generating randomized players and teams (`PlayerGenerator`, `TeamGenerator`, `LineupBuilder`). |
| `com.sal.fm.engine` | The match engine. Responsible for simulating matches minute-by-minute (`MatchEngine`), resolving duels (`DuelResolver`), and calculating outcomes (`AttributeCalculator`). |
| `com.sal.fm.enums` | Enumerations for game logic: `Position`, `Tactic`, `MatchEvent`, etc. |
| `com.sal.fm.model` | Core data models: `GameState`, `Match`, `League`. Tracks simulation state and data flow. |
| `com.sal.fm.model.team` | The `Team` class and its lineup structure — manages players, substitutes, and tactical setup. |
| `com.sal.fm.model.player` | Player model and all attribute categories: `Technical`, `Physical`, `Mental`, `Hidden`, and `Goalkeeping`. Central to simulation realism. |
| `com.sal.fm.util` | Utility classes for randomness (`DiceUtil`), match logging (`MatchLogger`), and JSON persistence (`JsonUtil`). |

---

## Attribute System

Players are generated with a deep stat system:

- **Technical**: passing, shooting, dribbling, tackling...
- **Physical**: pace, stamina, strength, agility...
- **Goalkeeping**: reflexes, one-on-ones, communication...
- **Mental**: composure, positioning, anticipation, flair...
- **Hidden**: professionalism, leadership, pressure handling...

Each position (FIXO, WINGER, PIVOT, GOALKEEPER) has its own stat profile.

---

## Simulation Cycle

Each match consists of 40 minutes simulated in two halves (20 + 20).  
Every minute can result in:
- PASS vs TACKLE duel
- SHOT ON GOAL vs GOALKEEPER save

Duels are resolved based on weighted stats and randomness.

---

## Save System

- Game state is automatically saved to `saves/save.json`
- Save/load is handled via `SaveHelper` and `JsonUtil`
- Matches, teams, and league progression are persisted

---

## Debug Mode

Enable debug mode to see detailed logs for:
- Each pass and tackle duel
- Each shot and the scoring chance
- Logs saved to a uniquely named `.log` file per match

---

## Documentation

- [Project Structure](docs/project-structure.md)
- [Match Engine](docs/match-engine.md)
- [Player Attributes](docs/player-attributes.md)
- [Save System](docs/save-system.md)

---

## Running the Simulation

### Requirements:
- Java 17+
- Gradle or Maven (or run manually with `javac`)

### Run via terminal:

```bash
javac -d out $(find src -name "*.java")
java -cp out com.sal.fm.Main
