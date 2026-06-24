package com.stennu718.myboardgames.core.sound

import org.junit.Test
import org.junit.Assert.*

class SoundManagerTest {

    @Test
    fun testSoundEffectsCount() {
        assertTrue("Should have multiple sound effects", SoundManager.SoundEffect.entries.size >= 5)
    }

    @Test
    fun testAllSoundEffectsExist() {
        val effects = SoundManager.SoundEffect.entries
        assertTrue(effects.contains(SoundManager.SoundEffect.MOVE))
        assertTrue(effects.contains(SoundManager.SoundEffect.CAPTURE))
        assertTrue(effects.contains(SoundManager.SoundEffect.CHECK))
        assertTrue(effects.contains(SoundManager.SoundEffect.WIN))
        assertTrue(effects.contains(SoundManager.SoundEffect.LOSE))
        assertTrue(effects.contains(SoundManager.SoundEffect.CLICK))
    }
}
