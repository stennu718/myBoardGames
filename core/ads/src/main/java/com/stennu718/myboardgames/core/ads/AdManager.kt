package com.stennu718.myboardgames.core.ads

import android.content.Context

data class AdConfig(
    val isEnabled: Boolean = true,
    val interstitialFrequency: Int = 5, // Show every N games
    val bannerEnabled: Boolean = true
)

class AdManager(private val context: Context) {

    private var gamesPlayed = 0
    private var isPremium = false

    fun setPremium(premium: Boolean) {
        isPremium = premium
    }

    fun shouldShowInterstitial(): Boolean {
        if (!AdConfig().isEnabled || isPremium) return false
        gamesPlayed++
        return gamesPlayed % AdConfig().interstitialFrequency == 0
    }

    fun shouldShowBanner(): Boolean {
        if (!AdConfig().isEnabled || isPremium) return false
        return AdConfig().bannerEnabled
    }

    fun onAdShown() {
        // Track ad impressions
    }
}