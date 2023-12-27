package com.example.mymemory.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.mymemory.repository.GameRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.random.Random

private const val FIELD_WIDTH = 2
private const val FIELD_HEIGHT = 3

@HiltViewModel
class GameViewModel @Inject constructor(
    gameRepository: GameRepository,
) : ViewModel() {
    val gameState = MutableStateFlow(
        GameListUIState(
            cards = generateFields(),
            solved = false,
            coinCount = 0
        )
    )
    val totalCoinsStateGame = MutableStateFlow(gameRepository.getTotalCoins())

    val timerState = MutableStateFlow(0)

    init {
        viewModelScope.launch {
            launchTimer()
        }
    }

    private suspend fun launchTimer() {
        while (true) {
            delay(1000)
            timerState.value = timerState.value + 1
        }
    }

    private fun generateFields(): List<List<CardData>> {
        val cards = mutableListOf<MutableList<Int>>()
        for (rowIndex in 0 until FIELD_HEIGHT) {
            val row = mutableListOf<Int>()
            for (columnIndex in 0 until FIELD_WIDTH) {
                row.add(-1)
            }
            cards.add(row)
        }
        val pairsCount = FIELD_WIDTH * FIELD_HEIGHT / 2
        val ids = mutableListOf<Int>()
        for (i in 0 until pairsCount) {
            ids.add(i)
            ids.add(i)
        }
        ids.forEach { id ->
            val freePlaces = mutableListOf<Pair<Int, Int>>()
            cards.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, id ->
                    if (cards[rowIndex][columnIndex] == -1) {
                        freePlaces.add(rowIndex to columnIndex)
                    }
                }
            }
            val freePlace = if (freePlaces.size <= 1) {
                freePlaces.first()
            } else {
                freePlaces[Random.nextInt(0, freePlaces.size)]
            }
            cards[freePlace.first][freePlace.second] = id
        }
        return cards.map { row ->
            row.map { id ->
                CardData(
                    state = CardState.CLOSED,
                    id = id
                )
            }
        }
    }

    fun onItemClicked(rowIndex: Int, columnIndex: Int) {
        when (gameState.value.cards[rowIndex][columnIndex].state) {
            CardState.OPEN -> Unit
            CardState.CLOSED -> {
                val openedCards = gameState.value.cards.flatMap { row ->
                    row.filter { card ->
                        card.state == CardState.OPEN
                    }
                }
                if (openedCards.size < 2) {
                    val updatedCards = gameState.value.cards
                        .map { cardsRow -> cardsRow.toMutableList() }
                        .toMutableList()
                    updatedCards[rowIndex][columnIndex] =
                        gameState.value.cards[rowIndex][columnIndex].copy(state = CardState.OPEN)
                    gameState.value = gameState.value.copy(
                        cards = updatedCards,
                    )
                    val openedCardsAfterUpdate = updatedCards.flatMap { row ->
                        row.filter { card ->
                            card.state == CardState.OPEN
                        }
                    }
                    if (openedCardsAfterUpdate.size > 1) {
                        viewModelScope.launch {
                            calculateGameState(openedCardsAfterUpdate)
                        }
                    }
                }
            }
            CardState.GUESSED -> Unit
        }
    }

    private suspend fun calculateGameState(openedCards: List<CardData>) {
        delay(500)


        val allItemsSame = openedCards.all { cardData ->
            cardData.id == openedCards[0].id
        }
        if (openedCards.size > 1) {
            val updatedCards = gameState.value.cards
                .map { cardsRow -> cardsRow.toMutableList() }
                .toMutableList()
            updatedCards.forEachIndexed { rowIndex, row ->
                row.forEachIndexed { columnIndex, cardData ->
                    if (cardData.state == CardState.OPEN) {
                        updatedCards[rowIndex][columnIndex] =
                            cardData.copy(
                                state = if (allItemsSame) {
                                    CardState.GUESSED
                                } else {
                                    CardState.CLOSED
                                }
                            )
                    }
                }
            }
            var solved = true

            updatedCards.forEach { row ->
                row.forEach { cardData ->
                    if (cardData.state != CardState.GUESSED) {
                        solved = false
                    }
                }
            }

            gameState.value = gameState.value.copy(
                cards = updatedCards,
                solved = solved,
                coinCount = if (timerState.value == 0)
                    0
                else {
                    (100 - 5 * (timerState.value - 20).coerceAtLeast(0)).coerceAtLeast(10)
                }
            )
        }
    }
}

data class GameListUIState(
    val cards: List<List<CardData>>,
    val solved: Boolean,
    val coinCount: Int
)

enum class CardState {
    OPEN,
    CLOSED,
    GUESSED,
}

data class CardData(
    val state: CardState, val id: Int
)
