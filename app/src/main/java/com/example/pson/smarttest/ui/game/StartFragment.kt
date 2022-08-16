package com.example.pson.smarttest.ui.game

import android.annotation.SuppressLint
import android.os.Bundle
import android.text.InputFilter
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

    private var filteredPlayerName: String? = null

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
        //set max length name in editText
        binding.playerNameText.filters += InputFilter.LengthFilter(9)
    }

    //on click start button
    fun startGame() {
        if (isNameFieldTyped()) {
            val action = StartFragmentDirections.actionStartFragmentToGameFragment(playerName = filteredPlayerName)
            findNavController().navigate(action)
            viewModel.reinitializeGame()
        } else {
            binding.playerNameText.error = "Please don't leave me blank :("
        }
    }

    //on click pika pic
    fun openLeaderboard() {
        val action = StartFragmentDirections.actionStartFragmentToScoreboardFragment()
        findNavController().navigate(action)
    }

    //func that check if text field is empty or full of space digit
    private fun isNameFieldTyped() : Boolean {
        if (binding.playerNameText.text!!.isBlank())
            return false

        val playerName = binding.playerNameText.text!!.toString()

        //xoá hết kí tự space ở đầu và cuối
        var start = 0
        var end = playerName.length - 1
        while (playerName[start] == ' ') start++
        while (playerName[end] == ' ') end--

        //chuỗi kết quả
        filteredPlayerName = playerName.substring(start, end+1)

        return filteredPlayerName!!.isNotBlank()
    }
}