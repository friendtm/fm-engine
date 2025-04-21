# 💾 Save System

The simulation supports full save/load functionality using JSON files.  
All game state — teams, players, league data, and progression — is serialized and persisted.

---

## 📂 File Structure

| File | Purpose |
|------|---------|
| `saves/save.json` | Main save file that stores the `GameState` object. |
| `logs/{matchId}.log` | Optional debug logs per match when debug mode is enabled. |

---

## 🧠 Components

### 🧾 `GameState`
Main container object storing:
- `League`: all teams and matches
- `currentDay`: simulation time
- `currentMatchday`: which round to simulate next
- `lastMatchdayDay`: used to space out matchdays

### 💾 `SaveManager`
Handles low-level save/load using JSON:
```java
public static void save(GameState state);
public static GameState load();
```
Uses `JsonUtil` for serialization.

### 🧪 `SaveHelper`
A higher-level API for saving and exiting:
```java
SaveHelper.save(gameState);
SaveHelper.saveAndExit(gameState);
```
Also checks if a save exists:
```java
SaveHelper.hasSave();
```

---

## 🛠️ How It Works

1. On startup:
   - If a save exists, it's loaded.
   - Otherwise, a new game is created and saved.

2. During play:
   - You can manually save anytime.
   - The game also saves on exit.

3. Match logs (debug):
   - Stored as text files for inspection.
   - Show all duels, stats, outcomes.

---

## 📚 Related

- [Player Attributes](player-attributes.md)
- [Match Engine](match-engine.md)
- [Project Structure](project-structure.md)
