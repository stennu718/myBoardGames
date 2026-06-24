package com.stennu718.myboardgames.feature.profile

enum class Achievement(
    val id: String,
    val name: String,
    val description: String,
    val requirement: Int
) {
    FIRST_WIN("first_win", "First Victory", "Win your first game", 1),
    CHESS_NOVICE("chess_novice", "Chess Novice", "Win 5 chess games", 5),
    CHESS_MASTER("chess_master", "Chess Master", "Win 25 chess games", 25),
    CHECKERS_PRO("checkers_pro", "Checkers Pro", "Win 10 checkers games", 10),
    SUDOKU_SOLVER("sudoku_solver", "Sudoku Solver", "Complete 10 Sudoku puzzles", 10),
    SUDOKU_EXPERT("sudoku_expert", "Sudoku Expert", "Complete 50 Sudoku puzzles", 50),
    PUZZLE_FAN("puzzle_fan", "Puzzle Fan", "Play 20 puzzle games", 20),
    STREAK_3("streak_3", "On Fire", "3-day streak", 3),
    STREAK_7("streak_7", "Unstoppable", "7-day streak", 7),
    STREAK_30("streak_30", "Legendary", "30-day streak", 30),
    SCORE_1000("score_1000", "Score Hunter", "Reach 1000 total score", 1000),
    SCORE_10000("score_10000", "Score Legend", "Reach 10000 total score", 10000);

    companion object {
        fun fromId(id: String): Achievement? = entries.find { it.id == id }
    }
}

data class AchievementProgress(
    val achievement: Achievement,
    val currentProgress: Int,
    val isUnlocked: Boolean
)