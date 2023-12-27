package com.example.mymemory.menu

import androidx.lifecycle.ViewModel
import com.example.mymemory.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import javax.inject.Inject

@HiltViewModel
class MenuViewModel @Inject constructor(
    private val gameRepository: GameRepository,
) : ViewModel() {
    val totalCoinsStateMenu = MutableStateFlow(gameRepository.getTotalCoins())


    fun updateTotalCoins() {
        totalCoinsStateMenu.value = gameRepository.getTotalCoins()
    }
}
