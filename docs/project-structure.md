# 🧱 Project Structure

The codebase is modular and organized by responsibility, following clean architecture principles.

## 📦 Package Overview

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

## 🧠 Architecture Tips

- **Model layer**: cleanly separated and serializable via Jackson
- **Engine layer**: stat-driven simulation that’s easily testable
- **Core loop**: manages state, progression, I/O — ideal for CLI or UI wrapping

---

## 📚 Related Docs

- [README.md](../README.md)
- [docs/match-engine.md](match-engine.md)
- [docs/player-attributes.md](player-attributes.md)
- [docs/save-system.md](save-system.md)
