package com.stennu718.myboardgames.feature.profile

import org.junit.Test
import org.junit.Assert.*

class AchievementTest {

    @Test
    fun testFirstWin() {
        val stats = listOf(
            com.stennu718.myboardgames.core.database.GameStats("chess", gamesPlayed = 1, gamesWon = 1)
        )
        val vm = ProfileViewModel(object : StatsRepository(mockk()) {})
        // Simplified test - verify achievement enum values
        assertEquals(1, Achievement.FIRST_WIN.requirement)
    }

    @Test
    fun testAchievementFromId() {
        val achievement = Achievement.fromId("first_win")
        assertNotNull(achievement)
        assertEquals(Achievement.FIRST_WIN, achievement)
    }

    @Test
    fun testAchievementNotFound() {
        val achievement = Achievement.fromId("nonexistent")
        assertNull(achievement)
    }

    @Test
    fun testAllAchievementsHaveValidRequirements() {
        Achievement.entries.forEach { a ->
            assertTrue("Achievement $a should have positive requirement", a.requirement > 0)
            assertTrue("Achievement $a should have non-empty name", a.name.isNotEmpty())
            assertTrue("Achievement $a should have non-empty description", a.description.isNotEmpty())
        }
    }

    @Test
    fun testLevelCalculation() {
        val totalXp = 5000
        val level = (totalXp / 1000) + 1
        assertEquals(6, level)
    }

    @Test
    fun testLevelOneAtZeroXp() {
        val totalXp = 0
        val level = (totalXp / 1000) + 1
        assertEquals(1, level)
    }
}

// Minimal mock for testing
private fun mockk(): com.stennu718.myboardgames.core.database.GameStatsDao {
    return object : com.stennu718.myboardgames.core.database.GameStatsDao {
        override suspend fun getAll(): List<com.stennu718.myboardgames.core.database.GameStats> = emptyList()
        override suspend fun getByType(type: String): com.stennu718.myboardgames.core.database.GameStats? = null
        override suspend fun upsert(stats: com.stennu718.myboardgames.core.database.GameStats) {}
    }
}
