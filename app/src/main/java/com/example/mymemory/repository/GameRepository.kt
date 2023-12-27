package com.example.mymemory.repository

import android.content.Context

private const val GAME_SHARED_PREFS = "GAME_SHARED_PREFS"
private const val TOTAL_EARNED_COINS = "TOTAL_EARNED_COINS"


class GameRepository(
    context: Context
) {
    private val sharedPreferences =
        context.getSharedPreferences(GAME_SHARED_PREFS, Context.MODE_PRIVATE)

    fun getTotalCoins(): Int = sharedPreferences.getInt(TOTAL_EARNED_COINS, 0)

    fun addEarnedCoins(earnedCoins: Int) {
        val totalCoins = getTotalCoins()
        sharedPreferences
            .edit()
            .putInt(TOTAL_EARNED_COINS, totalCoins + earnedCoins)
            .apply()
    }
}