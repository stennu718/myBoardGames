package com.stennu718.myboardgames.feature.stats

import org.junit.Test
import org.junit.Assert.*
import com.stennu718.myboardgames.core.database.GameStats

class StatsDashboardTest {

    @Test
    fun testBuildDashboard() {
        val engine = StatsDashboardEngine()
        val stats = listOf(
            GameStats("chess", gamesPlayed = 10, gamesWon = 5, totalXp = 1000),
            GameStats("sudoku", gamesPlayed = 20, gamesWon = 18, totalXp = 2000)
        )
        val dashboard = engine.buildDashboard(stats, emptyList())
        assertEquals(30, dashboard.totalGamesPlayed)
        assertEquals(23, dashboard.totalWins)
        assertEquals(3000, dashboard.totalXp)
    }

    @Test
    fun testWinRateCalculation() {
        val engine = StatsDashboardEngine()
        val stats = listOf(GameStats("chess", gamesPlayed = 10, gamesWon = 7))
        val dashboard = engine.buildDashboard(stats, emptyList())
        assertEquals(70, dashboard.winRate)
    }

    @Test
    fun testZeroGamesWinRate() {
        val engine = StatsDashboardEngine()
        val dashboard = engine.buildDashboard(emptyList(), emptyList())
        assertEquals(0, dashboard.winRate)
    }

    @Test
    fun testLevelCalculation() {
        val engine = StatsDashboardEngine()
        val stats = listOf(GameStats("chess", totalXp = 5000))
        val dashboard = engine.buildDashboard(stats, emptyList())
        assertEquals(6, dashboard.level)
    }

    @Test
    fun testXpToNextLevel() {
        val engine = StatsDashboardEngine()
        assertEquals(500, engine.getXpToNextLevel(500))
        assertEquals(1000, engine.getXpToNextLevel(0))
        assertEquals(1, engine.getXpToNextLevel(999))
    }

    @Test
    fun testLevelProgress() {
        val engine = StatsDashboardEngine()
        assertEquals(0.5f, engine.getLevelProgress(500), 0.01f)
        assertEquals(0.0f, engine.getLevelProgress(0), 0.01f)
        assertEquals(0.999f, engine.getLevelProgress(999), 0.01f)
    }

    @Test
    fun testFavoriteGame() {
        val engine = StatsDashboardEngine()
        val stats = listOf(
            GameStats("chess", gamesPlayed = 50),
            GameStats("sudoku", gamesPlayed = 20)
        )
        val dashboard = engine.buildDashboard(stats, emptyList())
        assertEquals("chess", dashboard.favoriteGame)
    }

    @Test
    fun testWeeklyActivityGenerated() {
        val engine = StatsDashboardEngine()
        val dashboard = engine.buildDashboard(emptyList(), emptyList())
        assertEquals(7, dashboard.weeklyActivity.size)
    }
}
