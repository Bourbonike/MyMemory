package com.example.mymemory.end

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.mymemory.databinding.FragmentEndGameBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class EndGameFragment : Fragment() {
    private lateinit var binding: FragmentEndGameBinding
    private val viewModel: EndViewModel by viewModels()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentEndGameBinding.inflate(layoutInflater)
        return binding.root


    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.coinsEarnedCount.text = viewModel.earnedCoinsState.toString()
        binding.homeButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

    }
}
