package com.stennu718.myboardgames.core.database

import androidx.room.*

@Dao
interface GameStatsDao {
    @Query("SELECT * FROM game_stats")
    suspend fun getAll(): List<GameStats>

    @Query("SELECT * FROM game_stats WHERE gameType = :type")
    suspend fun getByType(type: String): GameStats?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(stats: GameStats)
}

@Dao
interface AchievementDao {
    @Query("SELECT * FROM achievements")
    suspend fun getAll(): List<Achievement>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(achievements: List<Achievement>)

    @Update
    suspend fun update(achievement: Achievement)
}

@Dao
interface DailyChallengeDao {
    @Query("SELECT * FROM daily_challenges WHERE date = :date")
    suspend fun getByDate(date: String): DailyChallengeEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(entity: DailyChallengeEntity)
}