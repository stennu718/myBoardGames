package com.stennu718.myboardgames.feature.onboarding

import org.junit.Test
import org.junit.Assert.*

class OnboardingTest {

    @Test
    fun testOnboardingPagesCount() {
        assertEquals(5, onboardingPages.size)
    }

    @Test
    fun testAllPagesHaveTitles() {
        onboardingPages.forEach { page ->
            assertTrue("Page title should not be empty", page.title.isNotEmpty())
        }
    }

    @Test
    fun testAllPagesHaveDescriptions() {
        onboardingPages.forEach { page ->
            assertTrue("Page description should not be empty", page.description.isNotEmpty())
        }
    }

    @Test
    fun testAllPagesHaveIcons() {
        onboardingPages.forEach { page ->
            assertNotNull("Page should have an icon", page.icon)
        }
    }

    @Test
    fun testFirstPageIsWelcome() {
        assertEquals("Welcome to MyBoardGames", onboardingPages.first().title)
    }

    @Test
    fun testLastPageIsPremium() {
        assertEquals("Go Premium", onboardingPages.last().title)
    }
}
