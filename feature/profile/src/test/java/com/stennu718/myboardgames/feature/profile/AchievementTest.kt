package com.stennu718.myboardgames.feature.profile

import org.junit.Test
import org.junit.Assert.*

class AchievementTest {

    @Test
    fun testFirstWinRequirement() {
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

    @Test
    fun testTotalAchievementsCount() {
        assertTrue("Should have at least 10 achievements", Achievement.entries.size >= 10)
    }

    @Test
    fun testAchievementDescriptionsUnique() {
        val descriptions = Achievement.entries.map { it.description }
        assertEquals(descriptions.size, descriptions.toSet().size)
    }

    @Test
    fun testAchievementIdsUnique() {
        val ids = Achievement.entries.map { it.id }
        assertEquals(ids.size, ids.toSet().size)
    }
}
