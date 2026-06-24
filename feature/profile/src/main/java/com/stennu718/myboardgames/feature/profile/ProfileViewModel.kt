package com.stennu718.myboardgames.feature.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.stennu718.myboardgames.core.database.GameStats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class ProfileUiState(
    val totalGamesPlayed: Int = 0,
    val totalWins: Int = 0,
    val totalXp: Int = 0,
    val level: Int = 1,
    val unlockedAchievements: List<Achievement> = emptyList(),
    val recentStats: List<GameStats> = emptyList(),
    val currentStreak: Int = 0
)

class ProfileViewModel @Inject constructor(
    private val statsRepository: StatsRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ProfileUiState())
    val uiState: StateFlow<ProfileUiState> = _uiState.asStateFlow()

    init {
        loadStats()
    }

    private fun loadStats() {
        viewModelScope.launch {
            statsRepository.getAllStats().collect { stats ->
                val totalGames = stats.sumOf { it.gamesPlayed }
                val totalWins = stats.sumOf { it.gamesWon }
                val totalXp = stats.sumOf { it.totalXp }
                val level = (totalXp / 1000) + 1

                _uiState.value = ProfileUiState(
                    totalGamesPlayed = totalGames,
                    totalWins = totalWins,
                    totalXp = totalXp,
                    level = level,
                    recentStats = stats
                )
            }
        }
    }

    fun checkAchievements(stats: List<GameStats>): List<Achievement> {
        val unlocked = mutableListOf<Achievement>()
        val totalGames = stats.sumOf { it.gamesPlayed }
        val totalWins = stats.sumOf { it.gamesWon }
        val totalXp = stats.sumOf { it.totalXp }
        val chessWins = stats.find { it.gameType == "chess" }?.gamesWon ?: 0
        val checkersWins = stats.find { it.gameType == "checkers" }?.gamesWon ?: 0
        val sudokuCompleted = stats.find { it.gameType == "sudoku" }?.gamesPlayed ?: 0

        if (totalWins >= 1) unlocked.add(Achievement.FIRST_WIN)
        if (chessWins >= 5) unlocked.add(Achievement.CHESS_NOVICE)
        if (chessWins >= 25) unlocked.add(Achievement.CHESS_MASTER)
        if (checkersWins >= 10) unlocked.add(Achievement.CHECKERS_PRO)
        if (sudokuCompleted >= 10) unlocked.add(Achievement.SUDOKU_SOLVER)
        if (sudokuCompleted >= 50) unlocked.add(Achievement.SUDOKU_EXPERT)
        if (totalGames >= 20) unlocked.add(Achievement.PUZZLE_FAN)
        if (totalXp >= 1000) unlocked.add(Achievement.SCORE_1000)
        if (totalXp >= 10000) unlocked.add(Achievement.SCORE_10000)

        return unlocked
    }
}