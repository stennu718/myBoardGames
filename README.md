# myBoardGames

**Chess.com & Lichess Android application** — practice Chess, Checkers, Sudoku, Blockudoku and other puzzle games.

Built with Kotlin + Jetpack Compose. Multi-module architecture. Integrates Chess.com and Lichess APIs.

---

## 🎯 Vision

A single Android app that lets you practice:
- ♟️ **Chess** — puzzles, lessons, analysis, play vs engine/bot
- 🏁 **Checkers** — practice and play
- 🔢 **Sudoku** — daily puzzles, difficulty levels
- 🧱 **Blockudoku** — block-placement puzzle
- 🧩 **Other puzzles** — crossword, word games, pattern puzzles

Data sourced from Chess.com API + Lichess API (free tiers). Offline-first with local caching.

---

## 📋 TODO

### Phase 1: Foundation (Week 1-2)
- [ ] Initialize Android project with Kotlin + Jetpack Compose
- [ ] Set up multi-module architecture (app, core, data, feature modules)
- [ ] Configure Gradle (version catalog, build variants, CI)
- [ ] Set up CI/CD (GitHub Actions — build, test, lint)
- [ ] Implement theme system (Material 3, dark/light, dynamic colors)
- [ ] Implement navigation (Compose Navigation, bottom nav)
- [ ] Set up Room database for offline caching
- [ ] Implement dependency injection (Hilt)

### Phase 2: Chess.com Integration (Week 3-4)
- [ ] Research Chess.com API endpoints (daily puzzle, player stats, game history)
- [ ] Implement Chess.com API client (Retrofit + OkHttp)
- [ ] Build Daily Puzzle screen
- [ ] Build Puzzle Rush / Puzzle Storm screen
- [ ] Build Player Stats screen (ratings per time control)
- [ ] Build Game History viewer (PGN import/export)

### Phase 3: Lichess Integration (Week 5-6)
- [ ] Implement Lichess OAuth2 login (PKCE flow)
- [ ] Implement Lichess API client
- [ ] Build Lichess Puzzle screen (themes, rating filter)
- [ ] Build Lichess Puzzle Storm / Racer screens
- [ ] Build Lichess Bot play screen (play vs Lichess bots)
- [ ] Build Lichess game analysis screen

### Phase 4: Local Puzzle Games (Week 7-8)
- [ ] Build Sudoku engine (generator + solver)
- [ ] Build Sudoku UI (9x9 grid, notes, hints)
- [ ] Build Blockudoku engine
- [ ] Build Blockudoku UI
- [ ] Build Checkers engine + UI
- [ ] Implement puzzle streak tracking

### Phase 5: Social & Gamification (Week 9-10)
- [ ] Implement user profiles (linked Chess.com + Lichess accounts)
- [ ] Build daily challenge system (mix of all games)
- [ ] Implement XP/leveling/streak system
- [ ] Build leaderboard (local + friends)
- [ ] Implement notifications (daily puzzle reminder, streak freeze)

### Phase 6: Polish & Release (Week 11-12)
- [ ] Write unit tests (target: 500+ tests)
- [ ] Write UI tests (Compose testing, Espresso)
- [ ] Performance optimization (startup time, memory, battery)
- [ ] Accessibility (TalkBack, large text, color blind)
- [ ] Prepare Play Store listing (screenshots, description)
- [ ] Release v1.0 to Play Store

---

## 🔬 Research Plan

### 1. API Research

| API | Auth | Rate Limit | Key Endpoints | Cost |
|-----|------|------------|---------------|------|
| Chess.com | None (read-only) | ~100 req/day (unofficial) | `/player/{username}/stats`, `/puzzles/daily`, `/games/{id}` | Free |
| Lichess | OAuth2 / Token | 100 req/min (token) | `/api/puzzle/{id}`, `/api/puzzle/daily`, `/api/account`, `/api/bot/online` | Free |
| Chess Puzzles API | API key | 100 req/month | Random puzzle, themed puzzles | Free tier |

**Open Questions:**
- [ ] Is Chess.com API still accessible without auth? (community reverse-engineered)
- [ ] Can we use Lichess bot accounts for practice mode?
- [ ] What's the Lichess puzzle database size? (millions available)
- [ ] Rate limits for puzzle storm/racer endpoints

### 2. Technical Research

| Topic | What to Investigate |
|-------|-------------------|
| Jetpack Compose | Canvas-based game boards (chess/checkers), touch input, animations |
| Room DB | Caching strategy for puzzles, game history, user preferences |
| WorkManager | Daily puzzle download, streak tracking, notifications |
| DataStore | User settings, theme, last-played puzzle |
| Ktor vs Retrofit | Which HTTP client fits better? (Ktor for Lichess streaming) |
| Chess Engine | Stockfish Android port (C JNI) vs server-side analysis |
| Puzzle Generation | Sudoku generation algorithms (backtracking, constraint propagation) |

### 3. UX Research

| Topic | What to Investigate |
|-------|-------------------|
| Existing apps | Chess.com app, Lichess app, ChessLi, LibreSudoku — what do they do well? |
| Puzzle UX | How do users switch between game types? (tabs, bottom nav, cards) |
| Difficulty curve | How to calibrate puzzle ratings for mixed-game practice? |
| Offline mode | What works offline vs requires connection? |
| Session length | Average practice session — design for 5-15 min engagement |

### 4. Legal / Compliance

| Topic | Status |
|-------|--------|
| Chess.com TOS for API use | ⚠️ Must verify — unofficial API, may violate TOS |
| Lichess TOS | ✅ Open source, API documented, attribution required |
| GDPR (EU user) | Must implement data export/deletion |
| Play Store policy | No gambling, no real-money chess |

### 5. Open Source Projects to Study

| Project | What to Learn |
|---------|---------------|
| [jcarolus/android-chess](https://github.com/jcarolus/android-chess) | Chess engine integration, board UI, OEX protocol |
| [kaajjo/LibreSudoku](https://github.com/kaajjo/LibreSudoku) | Sudoku generation, Material3 theming |
| [pwenker/chessli](https://github.com/pwenker/chessli) | Lichess + Anki integration patterns |
| Lichess mobile app (GitHub) | Lichess API usage, OAuth flow, real-time play |

---

## 🏗️ Architecture (Planned)

```
myBoardGames/
├── app/                    # Application module, MainActivity, navigation
├── core/
│   ├── common/             # Shared utilities, extensions
│   ├── database/           # Room entities, DAOs
│   ├── network/            # Retrofit/Ktor clients, API services
│   └── ui/                 # Shared UI components, theme
├── feature/
│   ├── chess/              # Chess puzzles, analysis, play
│   ├── checkers/           # Checkers game
│   ├── sudoku/             # Sudoku puzzle
│   ├── blockudoku/         # Blockudoku puzzle
│   ├── daily-challenge/    # Mixed daily challenges
│   └── profile/            # User accounts, stats, settings
└── build-logic/            # Convention plugins, build configuration
```

---

## 📊 Success Metrics

- **500+ tests** (unit + integration)
- **< 2s cold start** on mid-range device
- **< 50MB** APK size
- **4.0+ Play Store rating**
- **Offline-first** — core puzzles work without internet

---

## 🚀 Getting Started

```bash
git clone https://github.com/stennu718/myBoardGames.git
cd myBoardGames
# Requires Android Studio Hedgehog+ and AGP 8.x+
./gradlew build
```

---

## 📄 License

MIT (or TBD)
