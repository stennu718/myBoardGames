package com.stennu718.myboardgames.core.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [GameStats::class, Achievement::class, DailyChallengeEntity::class],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun gameStatsDao(): GameStatsDao
    abstract fun achievementDao(): AchievementDao
    abstract fun dailyChallengeDao(): DailyChallengeDao
}