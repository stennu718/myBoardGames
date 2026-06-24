package com.stennu718.myboardgames.feature.daily

import org.junit.Test
import org.junit.Assert.*

class DailyChallengeTest {

    @Test
    fun testDailyChallengesGenerated() {
        val manager = DailyChallengeManager()
        val challenges = manager.getTodayChallenges()
        assertEquals(3, challenges.size)
    }

    @Test
    fun testDailyChallengesHaveValidGameTypes() {
        val manager = DailyChallengeManager()
        val challenges = manager.getTodayChallenges()
        val validTypes = setOf("chess", "checkers", "sudoku", "blockudoku", "puzzle")
        challenges.forEach { challenge ->
            assertTrue("Invalid game type: ${challenge.gameType}", challenge.gameType in validTypes)
        }
    }

    @Test
    fun testDailyChallengesHaveXpRewards() {
        val manager = DailyChallengeManager()
        val challenges = manager.getTodayChallenges()
        challenges.forEach { challenge ->
            assertTrue("XP reward should be positive", challenge.xpReward > 0)
        }
    }

    @Test
    fun testDailyChallengesHaveTargets() {
        val manager = DailyChallengeManager()
        val challenges = manager.getTodayChallenges()
        challenges.forEach { challenge ->
            assertTrue("Target should be positive", challenge.target > 0)
        }
    }

    @Test
    fun testSameDateSameChallenges() {
        val manager = DailyChallengeManager()
        val challenges1 = manager.getTodayChallenges()
        val challenges2 = manager.getTodayChallenges()
        assertEquals(challenges1.size, challenges2.size)
        // Same date should produce same game types (order may vary)
        assertEquals(challenges1.map { it.gameType }.toSet(), challenges2.map { it.gameType }.toSet())
    }

    @Test
    fun testTodayDateStringFormat() {
        val manager = DailyChallengeManager()
        val dateStr = manager.getTodayDateString()
        assertTrue("Date should be in ISO format", dateStr.matches(Regex("\\d{4}-\\d{2}-\\d{2}")))
    }
}
