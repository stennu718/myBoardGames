# myBoardGames

**Android application** — practice Chess, Checkers, Sudoku, Blockudoku and other puzzle games.

Built with Kotlin + Jetpack Compose. All games are self-contained — no third-party APIs, no accounts, no internet required.

---

## 🎯 Vision

A single offline-first Android app with fully local game logic:

- ♟️ **Chess** — play vs CPU, puzzles, game analysis
- 🏁 **Checkers** — play vs CPU
- 🔢 **Sudoku** — infinite generated puzzles, difficulty levels
- 🧱 **Blockudoku** — block-placement puzzle
- 🧩 **More puzzles** — 2048, Minesweeper, Memory, Tic-Tac-Toe, etc.

**No accounts. No internet. No APIs. Just games.**

---

## 📋 TODO

### Phase 1: Foundation (Week 1)
- [ ] Initialize Android project with Kotlin + Jetpack Compose
- [ ] Set up multi-module architecture (app, core, feature modules)
- [ ] Configure Gradle (version catalog, build variants, CI)
- [ ] Set up CI/CD (GitHub Actions — build, test, lint)
- [ ] Implement theme system (Material 3, dark/light, dynamic colors)
- [ ] Implement navigation (Compose Navigation, bottom nav)
- [ ] Set up Room database for stats/achievements
- [ ] Implement dependency injection (Hilt)

### Phase 2: Chess (Week 2-3)
- [ ] Build chess engine (legal moves, castling, en passant, promotion, check/checkmate/stalemate)
- [ ] Build chess board UI (Canvas, piece drag, highlight legal moves)
- [ ] Implement Stockfish-compatible engine or simple AI (minimax + alpha-beta)
- [ ] Build puzzle mode (mate in N, tactics trainer)
- [ ] Build game analysis mode (move history, undo, PGN export)
- [ ] Write chess engine tests (perft validation, 1000+ tests)

### Phase 3: Checkers (Week 4)
- [ ] Build checkers engine (standard rules, king promotion, multi-jumps)
- [ ] Build checkers board UI
- [ ] Implement AI opponent (minimax)
- [ ] Write checker engine tests

### Phase 4: Sudoku (Week 5)
- [ ] Build Sudoku generator (backtracking + constraint propagation)
- [ ] Build Sudoku solver (with difficulty rating)
- [ ] Build Sudoku UI (9x9 grid, notes mode, hints, undo)
- [ ] Implement difficulty selector (Easy/Medium/Hard/Expert)
- [ ] Write generator/solver tests

### Phase 5: Blockudoku (Week 6)
- [ ] Build Blockudoku engine (grid, piece placement, line clearing)
- [ ] Build Blockudoku UI (drag pieces, preview placement, score)
- [ ] Implement piece generator (random shapes)
- [ ] Write engine tests

### Phase 6: Additional Puzzles (Week 7)
- [ ] 2048 game
- [ ] Minesweeper
- [ ] Memory card game
- [ ] Tic-Tac-Toe (with AI)
- [ ] Word Search

### Phase 7: Gamification & Polish (Week 8)
- [ ] Implement stats tracking (games played, win rate, streaks)
- [ ] Build achievements system
- [ ] Implement daily challenge (random mix of games)
- [ ] Write unit tests (target: 500+ total)
- [ ] Write UI tests
- [ ] Performance optimization
- [ ] Accessibility (TalkBack, color blind mode)
- [ ] Prepare Play Store listing
- [ ] Release v1.0

---

## 🔬 Research Plan

### 1. Chess Engine

| Topic | Details |
|-------|---------|
| Engine algorithm | Minimax with alpha-beta pruning, iterative deepening |
| Board representation | 0x88 or bitboard (64-bit Longs) |
| Move generation | Pre-computed lookup tables, magic bitboards |
| Evaluation | Material + piece-square tables + mobility + king safety |
| Difficulty levels | Depth limit (1-6) + random factor for lower levels |
| Validation | Perft testing (known positions, expected node counts) |
| Stockfish | Optional: integrate via JNI for stronger play |

### 2. Sudoku Generator

| Topic | Details |
|-------|---------|
| Algorithm | Backtracking fill → remove cells while maintaining unique solution |
| Difficulty | Number of givens + required techniques (naked singles, hidden singles, X-wing) |
| Validation | Verify unique solution, reject ambiguous puzzles |
| Symmetry | Optional rotational symmetry for aesthetic puzzles |

### 3. Blockudoku Engine

| Topic | Details |
|-------|---------|
| Grid | 9×9 board |
| Pieces | All free polyominoes (1-9 cells) |
| Placement | Valid if all cells empty and connected to existing |
| Clearing | Full row, column, or 3×3 box → clear + score |
| Game over | No remaining piece can be placed |

### 4. Technical Research

| Topic | What to Investigate |
|-------|-------------------|
| Jetpack Compose Canvas | Game board rendering, touch input, animations |
| Room DB | Stats, achievements, daily challenge history |
| WorkManager | Daily challenge scheduling |
| Game state | ViewModel + StateFlow for UI state management |
| Save/Restore | Survive configuration changes, process death |
| Testing | JUnit for engines, Compose testing for UI |

### 5. Open Source References

| Project | What to Learn |
|---------|---------------|
| [jcarolus/android-chess](https://github.com/jcarolus/android-chess) | Chess engine, board UI, OEX protocol |
| [kaajjo/LibreSudoku](https://github.com/kaajjo/LibreSudoku) | Sudoku generation, Material3 |
| [Jumman04/Jummania-Chess-Game-Compose](https://github.com/Jumman04/Jummania-Chess-Game-Compose) | Compose chess implementation |

---

## 🏗️ Architecture

```
myBoardGames/
├── app/                    # Application module, MainActivity, navigation
├── core/
│   ├── common/             # Shared utilities, extensions
│   ├── database/           # Room entities, DAOs (stats, achievements)
│   └── ui/                 # Shared UI components, theme
├── feature/
│   ├── chess/              # Chess game + engine + puzzles
│   ├── checkers/           # Checkers game + engine
│   ├── sudoku/             # Sudoku generator + UI
│   ├── blockudoku/         # Blockudoku engine + UI
│   ├── puzzles/            # 2048, Minesweeper, Memory, Tic-Tac-Toe
│   ├── daily-challenge/    # Daily mixed challenges
│   └── profile/            # Stats, achievements, settings
└── build-logic/            # Convention plugins
```

---

## 📊 Success Metrics

- **500+ tests** (engine perft tests + unit + UI)
- **< 1.5s cold start** on mid-range device
- **< 40MB** APK size
- **100% offline** — no internet required ever
- **4.0+ Play Store rating**

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

MIT
