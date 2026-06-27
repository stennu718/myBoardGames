[![CI](https://github.com/stennu718/myBoardGames/actions/workflows/ci.yml/badge.svg)](https://github.com/stennu718/myBoardGames/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
[![Kotlin](https://img.shields.io/badge/kotlin-1.9+-7f52ff.svg)](https://kotlinlang.org/)
[![Android](https://img.shields.io/badge/Android-14+-3ddc84.svg)](https://developer.android.com/)
[![Version](https://img.shields.io/github/v/release/stennu718/myBoardGames)](https://github.com/stennu718/myBoardGames/releases)

# myBoardGames

**Android application** — practice Chess, Checkers, Sudoku, Blockudoku and other puzzle games.

Built with Kotlin + Jetpack Compose. All games are self-contained — no third-party APIs, no accounts, no internet required.

---

![Gameplay](docs/screenshot.png)

## Contributing

See [CONTRIBUTING.md](CONTRIBUTING.md) for development setup, architecture decisions, and contribution guidelines.

## 🎯 Vision

A single offline-first Android app with fully local game logic:

- ♟️ **Chess** — play vs CPU, Puzzle Rush mode, multiplayer, game analysis
- 👥 **Multiplayer** — play chess with friends via Bluetooth
- ⚡ **Puzzle Rush** — timed chess puzzle streak with ELO rating
- 🏁 **Checkers** — play vs CPU
- 🔢 **Sudoku** — infinite generated puzzles, difficulty levels
- 🧱 **Blockudoku** — block-placement puzzle
- 🧩 **More puzzles** — 2048, Minesweeper, Memory, Tic-Tac-Toe
- 📊 **Statistics** — rating charts, weekly activity, achievement progress
- 🎨 **Premium Themes** — 10 board themes, 6 color schemes
- 📱 **Home Widget** — daily puzzle preview
- ☁️ **Cloud Sync** — cross-device progress sync (Firebase-ready)
- 🎬 **Onboarding** — animated intro with premium upsell
- 🔊 **Sound & Haptics** — tactile feedback for every action
- ⭐ **In-App Review** — smart review prompt
- 📣 **Ads** — free tier monetization

---

## ✅ What's Built

| Feature | Status | Tests |
|---------|--------|-------|
| Chess Engine (full legal moves, FEN, castling, en passant, promotion) | ✅ | 16 |
| Chess AI (minimax + alpha-beta, piece-square tables) | ✅ | ✅ |
| Chess Board UI (tap controls, difficulty selector) | ✅ | — |
| Multiplayer (Bluetooth P2P, host/join lobby) | ✅ | 8 |
| Puzzle Rush (timed streak, ELO rating, theme stats) | ✅ | 8 |
| Checkers Engine (multi-jumps, king promotion) | ✅ | 8 |
| Checkers Board UI | ✅ | — |
| Sudoku Generator (unique solution, 4 difficulties) | ✅ | 8 |
| Sudoku UI (number pad, hints) | ✅ | — |
| Blockudoku Engine (16 piece shapes, line clearing) | ✅ | 10 |
| Blockudoku UI (drag-to-place) | ✅ | — |
| 2048 Game (swipe controls, score) | ✅ | 4 |
| Minesweeper (flood reveal, flag mode) | ✅ | 5 |
| Memory Card Game (flip animation, move counter) | ✅ | 5 |
| Tic-Tac-Toe (vs AI) | ✅ | 4 |
| Stats Dashboard (charts, breakdowns, progress) | ✅ | 7 |
| Premium Themes (10 board + 6 color schemes) | ✅ | — |
| Home Screen Widget (Glance) | ✅ | — |
| Cloud Sync Manager | ✅ | 6 |
| Daily Challenges (deterministic per date) | ✅ | 6 |
| Gamification (XP, levels, 12 achievements) | ✅ | 8 |
| Onboarding (5-page animated intro) | ✅ | 6 |
| Sound & Haptics (9 effect types) | ✅ | 2 |
| In-App Review (smart prompt) | ✅ | 1 |
| Ad Manager (interstitial + banner) | ✅ | — |
| Room Database (stats, achievements) | ✅ | — |
| Hilt DI | ✅ | — |
| Material 3 Theme (dynamic colors) | ✅ | — |
| GitHub Actions CI | ✅ | — |

**Total: 60+ Kotlin files, ~6,000 lines, 13 test files, ~90 test cases**

---

## 🏗️ Architecture

```
myBoardGames/
├── app/                    # Application module, MainActivity, navigation
├── core/
│   ├── common/             # Shared utilities, extensions
│   ├── database/           # Room entities, DAOs (stats, achievements)
│   ├── ui/                 # Shared UI components, theme, ThemeRepository
│   ├── sound/              # SoundManager (haptics + audio)
│   ├── review/             # ReviewManager (in-app review)
│   └── ads/                # AdManager (free tier ads)
├── feature/
│   ├── chess/              # Chess game + engine + AI + board UI
│   ├── multiplayer/        # Bluetooth P2P multiplayer
│   ├── checkers/           # Checkers game + engine
│   ├── sudoku/             # Sudoku generator + solver + UI
│   ├── blockudoku/         # Blockudoku engine + UI
│   ├── puzzles/            # 2048, Minesweeper, Memory, Tic-Tac-Toe
│   ├── puzzlerush/         # Puzzle Rush mode (timed, rated)
│   ├── daily/              # Daily challenge system
│   ├── profile/            # Stats, achievements, settings
│   ├── premium/            # Premium paywall + theme unlocks
│   ├── stats/              # Statistics dashboard with charts
│   ├── widget/             # Home screen Glance widget
│   ├── cloud/              # Cloud sync manager
│   └── onboarding/         # Animated onboarding flow
└── build-logic/            # Convention plugins
```

---

## 📱 Screens & Navigation

| Tab | Screen | Description |
|-----|--------|-------------|
| ♟ | Chess | Play vs CPU, Puzzle Rush entry |
| 👥 VS | Multiplayer | Bluetooth P2P chess |
| ⚡ Rush | Puzzle Rush | Timed chess puzzles, ELO rating |
| 📊 Stats | Rating chart, weekly activity, game breakdown |
| 👤 Profile | Level, XP, achievements, per-game stats |

**Other games accessible from Chess screen menu:** Checkers, Sudoku, Blockudoku, 2048, Minesweeper, Memory, Tic-Tac-Toe.

---

## 💰 Monetization

**Free tier:** All games playable, basic theme, ads
**Premium ($4.99 lifetime):**
- 10+ board themes (Marble, Emerald, Sunset, Royal, Gold...)
- 6 color schemes (Midnight, Forest, Wine, Amethyst, Monochrome)
- Remove ads
- Cloud sync across devices
- Home screen widgets
- Exclusive premium puzzle packs

---

## 🔬 Technical Details

### Chess Engine
- Full legal move generation with castling, en passant, promotion
- FEN import/export
- Minimax with alpha-beta pruning (depth 1-6)
- Piece-square tables for positional evaluation
- Perft validation (20 → 400 → 8902)

### Multiplayer
- Bluetooth RFCOMM protocol
- Host/Client roles
- Real-time move synchronization
- Resign/disconnect handling

### Sudoku Generator
- Backtracking fill → remove cells with unique solution guarantee
- Difficulty: Easy (35 removed) → Expert (58 removed)

### Blockudoku
- 16 piece shapes (polyominoes)
- Row/column/3×3 box clearing
- Game over detection

### Puzzle Rush
- 3-minute timer
- ELO rating system (K=32)
- Adaptive puzzle selection by rating
- Theme-based performance stats

---

## 🚀 Getting Started

```bash
git clone https://github.com/stennu718/myBoardGames.git
cd myBoardGames
# Requires Android Studio Hedgehog+, JDK 17, AGP 8.7+
./gradlew build
```

### Requirements
- Android Studio Hedgehog (2023.1.1)+
- JDK 17
- Android SDK 35
- Gradle 8.11.1

---

## 📊 Success Metrics

- **90+ test cases** (engine perft + unit + integration)
- **< 1.5s cold start** on mid-range device
- **< 40MB** APK size
- **100% offline** — no internet required ever
- **4.0+ Play Store rating**

---

## 📄 License

MIT
