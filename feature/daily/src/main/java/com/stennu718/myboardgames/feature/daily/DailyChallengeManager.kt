package com.stennu718.myboardgames.feature.daily

import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

data class DailyChallengeItem(
    val id: String,
    val gameType: String,
    val description: String,
    val target: Int,
    val xpReward: Int
)

class DailyChallengeManager {
    private val challenges = listOf(
        "chess", "checkers", "sudoku", "blockudoku", "puzzle"
    )

    fun getTodayChallenges(): List<DailyChallengeItem> {
        val seed = LocalDate.now().toEpochDay()
        val random = Random(seed)

        val selected = challenges.shuffled(random).take(3)
        return selected.map { gameType ->
            when (gameType) {
                "chess" -> DailyChallengeItem(
                    id = "daily_chess_${LocalDate.now()}",
                    gameType = "chess",
                    description = "Win 2 chess games",
                    target = 2,
                    xpReward = 150
                )
                "checkers" -> DailyChallengeItem(
                    id = "daily_checkers_${LocalDate.now()}",
                    gameType = "checkers",
                    description = "Win 3 checkers games",
                    target = 3,
                    xpReward = 120
                )
                "sudoku" -> DailyChallengeItem(
                    id = "daily_sudoku_${LocalDate.now()}",
                    gameType = "sudoku",
                    description = "Complete 2 Sudoku puzzles",
                    target = 2,
                    xpReward = 100
                )
                "blockudoku" -> DailyChallengeItem(
                    id = "daily_blockudoku_${LocalDate.now()}",
                    gameType = "blockudoku",
                    description = "Score 150+ in Blockudoku",
                    target = 150,
                    xpReward = 100
                )
                else -> DailyChallengeItem(
                    id = "daily_puzzle_${LocalDate.now()}",
                    gameType = "puzzle",
                    description = "Play 3 puzzle games",
                    target = 3,
                    xpReward = 80
                )
            }
        }
    }

    fun getTodayDateString(): String {
        return LocalDate.now().format(DateTimeFormatter.ISO_LOCAL_DATE)
    }
}