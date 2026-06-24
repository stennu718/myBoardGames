package com.stennu718.myboardgames.core.review

import android.content.Context
import android.content.SharedPreferences

class ReviewManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("review_prefs", Context.MODE_PRIVATE)

    fun shouldShowReview(): Boolean {
        val gamesPlayed = prefs.getInt("games_played_since_review", 0)
        val lastReviewTime = prefs.getLong("last_review_time", 0)
        val now = System.currentTimeMillis()
        val daysSinceLastReview = (now - lastReviewTime) / (1000 * 60 * 60 * 24)

        return gamesPlayed >= 10 && daysSinceLastReview >= 7
    }

    fun onGamePlayed() {
        val current = prefs.getInt("games_played_since_review", 0)
        prefs.edit().putInt("games_played_since_review", current + 1).apply()
    }

    fun onReviewShown() {
        prefs.edit()
            .putInt("games_played_since_review", 0)
            .putLong("last_review_time", System.currentTimeMillis())
            .apply()
    }

    fun reset() {
        prefs.edit().clear().apply()
    }
}