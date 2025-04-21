# 🎯 Player Attribute System

This simulation uses a deep stat system to determine player performance.  
Each player is composed of 5 major attribute categories, each with specific influence during matches.

---

## 🧠 Attribute Categories

### 1. 🧤 GoalkeepingAttributes

Used when evaluating goalkeepers:
- `aerialReach` – intercepting crosses
- `commandOfArea` – ability to control the box
- `communication` – instructing defenders
- `handling` – securing the ball
- `kicking` – long-ball distribution
- `oneOnOnes` – closing down attackers
- `passing` – short ball distribution
- `reflexes` – quick reaction saves
- `rushingOut` – coming off the line
- `throwing` – fast outlet play

### 2. 🎯 TechnicalAttributes

Used primarily by outfield players:
- `corners`, `freeKicks`, `penaltyTaking` – set pieces
- `shooting` – goal scoring
- `longShots` – outside the box threat
- `heading` – aerial duels
- `passing` – ball distribution
- `dribbling` – 1v1 situations
- `tackling` – winning the ball
- `technique` – general on-ball skill

### 3. 💪 PhysicalAttributes

Affects player mobility and endurance:
- `pace`, `acceleration` – speed and explosiveness
- `balance` – staying upright under pressure
- `strength` – duels and shielding
- `stamina` – energy throughout the match
- `jumpingReach` – aerial reach
- `agility` – direction change
- `naturalFitness` – recovery rate

### 4. 🧠 MentalAttributes

In-game awareness and decision-making:
- `vision` – spotting passing opportunities
- `composure` – calmness under pressure
- `concentration`, `positioning` – maintaining structure
- `decisions` – smart choices
- `offTheBall` – intelligent movement
- `aggression`, `anticipation` – pressing and reactions
- `flair` – creativity
- `workRate` – total effort given

### 5. 🎭 HiddenAttributes

Used behind the scenes for behavior modeling:
- `determination` – willpower
- `leadership` – influence on teammates
- `ambition` – growth mindset
- `teamwork` – tactical cohesion
- `pressureHandling` – clutch moments
- `professionalism` – training and discipline

---

## 📊 Average Ratings

- `getAverageOutfieldSkill()` uses key Technical stats.
- `getAverageGoalkeepingSkill()` uses selected Goalkeeping stats.
- Overall player rating is computed contextually based on position.

---

## 📚 Related

- [Match Engine](match-engine.md)
- [Project Structure](project-structure.md)
