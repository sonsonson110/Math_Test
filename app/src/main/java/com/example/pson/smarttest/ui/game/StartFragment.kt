package com.example.pson.smarttest.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pson.smarttest.R
import com.example.pson.smarttest.application.ScoreboardApplication
import com.example.pson.smarttest.databinding.FragmentStartBinding

class StartFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels {
        GameViewModelFactory(
            (activity?.application as ScoreboardApplication).database
                .ScoreboardDao()
        )
    }

    //setting ẩn thanh action bar riêng cho fragment
    @SuppressLint("RestrictedApi")
    override fun onResume() {
        super.onResume()
        (activity as AppCompatActivity).supportActionBar?.setShowHideAnimationEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.hide()
    }

    override fun onStop() {
        super.onStop()
        (activity as AppCompatActivity).supportActionBar?.show()
    }

    //đổi data binding layout first
    private lateinit var binding: FragmentStartBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_start, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.apply {
            startFragment = this@StartFragment
        }
    }

    //on click start button
    fun startGame() {
        var playerName = binding.playerNameText.text.toString()
        if (playerName == "") playerName = "Noname"
        val action = StartFragmentDirections.actionStartFragmentToGameFragment(playerName = playerName)
        findNavController().navigate(action)
        viewModel.reinitializeGame()
    }

    //on click pika pic
    fun openLeaderboard() {
        val action = StartFragmentDirections.actionStartFragmentToScoreboardFragment()
        findNavController().navigate(action)
    }
}