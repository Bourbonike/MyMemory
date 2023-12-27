package com.example.mymemory.end

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.mymemory.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class EndViewModel @Inject constructor(
    gameRepository: GameRepository,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    val earnedCoinsState = requireNotNull(savedStateHandle.get<Int>("COINS_COUNT"))

    init {
        gameRepository.addEarnedCoins(earnedCoinsState)
    }
}