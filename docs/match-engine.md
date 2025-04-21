# ⚙️ Match Engine Overview

The match engine simulates each minute of gameplay using player stats and tactical data.  
It is stat-driven, duel-based, and highly extensible.

---

## 🧠 Core Components

### 🔁 `MatchEngine`
Simulates the match flow minute-by-minute:
- Randomly determines event type: `PASS_OR_TACKLE` or `SHOT_ON_GOAL`
- Selects attackers and defenders based on momentum and stats
- Delegates resolution to `DuelResolver` and `AttributeCalculator`
- Logs events via `MatchLogger`

### 🧮 `AttributeCalculator`
Handles stat-based chance calculation:
- **Passing success** = `passing`, `technique`, `vision` vs `tackling`, `anticipation`
- **Shooting accuracy** = `shooting`, `technique`
- **Shot success** = `shooting`, `composure`, `balance`, `longShots` vs `reflexes`, `positioning`, `oneOnOnes`
- **Under pressure** → adds `pressureHandling` modifier

### ⚔️ `DuelResolver`
Handles randomness and logging:
- Rolls chance against thresholds
- Builds detailed debug logs (when `debugMode` is enabled)
- Provides helper functions like `isShotOnTarget()` and `isGoal(...)`

---

## 🎮 Simulation Cycle

A full match has **40 minutes**, split in two halves:
1. First 20 minutes (`simulateMinute` x 20)
2. Halftime log
3. Second 20 minutes (`simulateMinute` x 20)

Each simulated minute can result in:
- A pass duel: attacker tries to bypass a defender
- A shot: attacker tries to score, resolved against the goalkeeper

---

## 📊 Momentum System

If a team wins a tackle or intercepts a pass, it gains **momentum**:
- Next turn, they are more likely to generate a shot chance

---

## 🔍 Debug Logging

When enabled:
- Saves all duel logs to a file in `/logs/{matchId}.log`
- Includes attacker/defender attributes, success chances, and outcomes

To enable debug mode:
```java
match.enableDebugMode();
```

---

## 📚 Related

- [Player Attributes](player-attributes.md)
- [Project Structure](project-structure.md)
- [Save System](save-system.md)
