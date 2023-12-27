package com.example.mymemory.game

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mymemory.end.EndGameFragment
import com.example.mymemory.R
import com.example.mymemory.databinding.FragmentGameBinding
import com.example.mymemory.databinding.ItemLayoutBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class GameFragment : Fragment() {
    private lateinit var binding: FragmentGameBinding
    private val viewModel: GameViewModel by viewModels()

    private var gameCards: List<List<ItemLayoutBinding>> = listOf()

    private fun timerStringFromLong(ms: Int): String {
        val seconds = ms % 60
        val minutes = (ms / 60 % 60)
        return makeTimeString(seconds = seconds, minutes = minutes)
    }

    private fun makeTimeString(minutes: Int, seconds: Int): String {
        return String.format("%02d:%02d", minutes, seconds)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentGameBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        lifecycleScope.launch {
            viewModel.timerState.collect { timerState ->
                binding.timer.text = timerStringFromLong(timerState)
            }
        }
        lifecycleScope.launch {
            viewModel.totalCoinsStateGame.collect { totalState ->
                binding.totalCoin.text = totalState.toString()
            }
        }

        lifecycleScope.launch {
            viewModel.gameState.collect { gameState ->
                if (gameCards.isEmpty()) {
                    inflateGameCards(gameState)
                }

                if (gameState.solved) {
                    parentFragmentManager.popBackStack()
                    parentFragmentManager.beginTransaction()
                        .replace(
                            R.id.container,
                            EndGameFragment().apply {
                                arguments = bundleOf("COINS_COUNT" to gameState.coinCount)
                            })
                        .addToBackStack(null)
                        .commit()
                }

                gameState.cards.forEachIndexed { rowIndex, row ->
                    row.forEachIndexed { columnIndex, cardData ->
                        gameCards[rowIndex][columnIndex].gridImage.apply {
                            animate()
                                .alpha(if (cardData.state == CardState.OPEN) 1f else 0f)
                                .setDuration(300)
                                .setListener(null)

                        }

                        gameCards[rowIndex][columnIndex].gameCard.apply {
                            animate()
                                .alpha(if (cardData.state == CardState.GUESSED) 0f else 1f)
                                .setDuration(300)
                                .setListener(null)
                        }

                    }
                }
            }

        }
    }

    private fun inflateGameCards(gameState: GameListUIState) {
        binding.gridView.columnCount = gameState.cards[0].size

        val inflatedGameCards = mutableListOf<List<ItemLayoutBinding>>()
        gameState.cards.forEachIndexed { rowIndex, row ->
            val gameCardRow = mutableListOf<ItemLayoutBinding>()
            row.forEachIndexed { columnIndex, cardData ->
                val cardBinding =
                    ItemLayoutBinding.inflate(layoutInflater, binding.gridView, true).apply {
                        root.layoutParams = GridLayout.LayoutParams(
                            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                            GridLayout.spec(GridLayout.UNDEFINED, GridLayout.FILL, 1f),
                        ).apply {
                            width = 0
                            height = 0
                        }
                    }
                gameCardRow.add(cardBinding)
                cardBinding.gameCard.setOnClickListener {
                    viewModel.onItemClicked(rowIndex = rowIndex, columnIndex = columnIndex)
                }
                cardBinding.gridImage.setImageResource(
                    when (cardData.id) {
                        0 -> R.drawable.game_1
                        1 -> R.drawable.game_2
                        2 -> R.drawable.game_3
                        3 -> R.drawable.game_4
                        4 -> R.drawable.game_5
                        5 -> R.drawable.game_6
                        6 -> R.drawable.game_7
                        7 -> R.drawable.game_8
                        8 -> R.drawable.game_9
                        9 -> R.drawable.game_10
                        10 -> R.drawable.game_11
                        11 -> R.drawable.game_12
                        12 -> R.drawable.game_13
                        13 -> R.drawable.game_14
                        14 -> R.drawable.game_15
                        15 -> R.drawable.game_16
                        16 -> R.drawable.game_17
                        17 -> R.drawable.game_18
                        18 -> R.drawable.game_19
                        else -> R.drawable.game_20
                    }
                )
            }
            inflatedGameCards.add(gameCardRow)
        }
        gameCards = inflatedGameCards
    }
}

