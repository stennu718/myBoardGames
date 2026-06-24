package com.stennu718.myboardgames.core.sound

import android.content.Context
import android.media.SoundPool
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.os.VibratorManager

class SoundManager(private val context: Context) {

    private var soundPool: SoundPool? = null
    private val soundMap = mutableMapOf<SoundEffect, Int>()
    private var isMuted = false

    enum class SoundEffect {
        MOVE, CAPTURE, CHECK, CHECKMATE, WIN, LOSE, CLICK, STREAK, ACHIEVEMENT
    }

    init {
        soundPool = SoundPool.Builder()
            .setMaxStreams(4)
            .build()

        soundPool?.setOnLoadCompleteListener { _, _, status ->
            // Sounds loaded
        }
    }

    fun play(effect: SoundEffect) {
        if (isMuted) return
        // In production, load actual sound files from res/raw
        // For now, just trigger haptic feedback
        vibrate(effect)
    }

    private fun vibrate(effect: SoundEffect) {
        val vibrator = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val manager = context.getSystemService(Context.VIBRATOR_MANAGER_SERVICE) as VibratorManager
            manager.defaultVibrator
        } else {
            @Suppress("DEPRECATION")
            context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val duration = when (effect) {
                SoundEffect.MOVE -> 20L
                SoundEffect.CAPTURE -> 40L
                SoundEffect.CHECK -> 80L
                SoundEffect.CHECKMATE -> 120L
                SoundEffect.WIN -> 200L
                SoundEffect.LOSE -> 200L
                SoundEffect.CLICK -> 15L
                SoundEffect.STREAK -> 60L
                SoundEffect.ACHIEVEMENT -> 150L
            }
            vibrator.vibrate(VibrationEffect.createOneShot(duration, VibrationEffect.DEFAULT_AMPLITUDE))
        }
    }

    fun setMuted(muted: Boolean) {
        isMuted = muted
    }

    fun isMuted(): Boolean = isMuted

    fun release() {
        soundPool?.release()
        soundPool = null
    }
}