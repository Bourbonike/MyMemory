package com.example.mymemory.menu

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.mymemory.R
import com.example.mymemory.databinding.FragmentMenuBinding
import com.example.mymemory.game.GameFragment
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MenuFragment : Fragment() {
    private lateinit var binding: FragmentMenuBinding
    private val viewModel: MenuViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMenuBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.buttonPlay.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.container, GameFragment())
                .addToBackStack(null)
                .commit()
        }
        lifecycleScope.launch {
            viewModel.totalCoinsStateMenu.collect { totalState ->
                binding.totalCoin.text = totalState.toString()
            }
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.updateTotalCoins()
    }
}




