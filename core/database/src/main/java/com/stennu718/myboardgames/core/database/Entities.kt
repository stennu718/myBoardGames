package com.stennu718.myboardgames.core.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "game_stats")
data class GameStats(
    @PrimaryKey val gameType: String,
    val gamesPlayed: Int = 0,
    val gamesWon: Int = 0,
    val bestScore: Int = 0,
    val totalXp: Int = 0
)

@Entity(tableName = "achievements")
data class Achievement(
    @PrimaryKey val id: String,
    val name: String,
    val description: String,
    val isUnlocked: Boolean = false,
    val unlockedAt: Long? = null
)

@Entity(tableName = "daily_challenges")
data class DailyChallengeEntity(
    @PrimaryKey val date: String,
    val challengeData: String,
    val isCompleted: Boolean = false
)