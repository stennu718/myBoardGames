# Contributing to myBoardGames

Thank you for contributing to this Android board game collection!

## Development Setup

### Prerequisites
- Android Studio Hedgehog (2023.1.1) or later
- JDK 17
- Android SDK 34

### Building
1. Clone the repository
2. Open in Android Studio
3. Sync Gradle files
4. Run tests: `./gradlew test`
5. Build: `./gradlew assembleDebug`

## Architecture
- Multi-module architecture (app/, core/, feature/)
- Hilt for dependency injection
- Room for local database
- Jetpack Compose for UI
- Convention plugins in build-logic/

## Pull Request Process

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/game-name`)
3. Add tests for new features
4. Ensure all existing tests pass
5. Follow Kotlin coding conventions
6. Open a Pull Request

## Code Style
- Kotlin coding conventions
- ktlint for formatting
- All new games must include unit tests
- UI tests for new screens
