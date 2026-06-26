package com.stennu718.myboardgames.feature.profile

import com.stennu718.myboardgames.core.database.GameStatsDao
import com.stennu718.myboardgames.core.database.GameStats
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class StatsRepository @Inject constructor(
    private val gameStatsDao: GameStatsDao
) {
    fun getAllStats(): Flow<List<GameStats>> = gameStatsDao.getAll()

    suspend fun recordGame(gameType: String, won: Boolean, score: Int = 0) {
        val existing = gameStatsDao.getByType(gameType)
        val updated = GameStats(
            gameType = gameType,
            gamesPlayed = (existing?.gamesPlayed ?: 0) + 1,
            gamesWon = (existing?.gamesWon ?: 0) + if (won) 1 else 0,
            bestScore = maxOf(existing?.bestScore ?: 0, score),
            totalXp = (existing?.totalXp ?: 0) + (if (won) 100 else 25) + score
        )
        gameStatsDao.upsert(updated)
    }

    suspend fun getGamesPlayed(gameType: String): Int {
        return gameStatsDao.getByType(gameType)?.gamesPlayed ?: 0
    }

    suspend fun getWinRate(gameType: String): Int {
        val stats = gameStatsDao.getByType(gameType) ?: return 0
        if (stats.gamesPlayed == 0) return 0
        return (stats.gamesWon * 100) / stats.gamesPlayed
    }
}