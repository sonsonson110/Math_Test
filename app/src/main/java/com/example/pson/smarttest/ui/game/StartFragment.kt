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
import com.google.android.material.bottomnavigation.BottomNavigationView

class StartFragment : Fragment() {

    private val viewModel: GameViewModel by activityViewModels {
        GameViewModelFactory(
            (activity?.application as ScoreboardApplication).database
                .ScoreboardDao()
        )
    }

    private var filteredPlayerName: String? = null

    private var recentPlayer: String = ""

    //setting ẩn thanh action bar riêng cho fragment
    override fun onStart() {
        super.onStart()
        (activity as AppCompatActivity).supportActionBar?.hide()
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

        //get last player name if game restart
        arguments?.let {
            recentPlayer = it.getString("playerName").toString()
        }
        //set the text field to last playerName if game restart
        binding.playerNameText.setText(recentPlayer)
    }

    //on click start button
    fun startGame() {
        if (isNameFieldTyped()) {
            val action = StartFragmentDirections.actionStartFragmentToGameFragment(playerName = filteredPlayerName)
            findNavController().navigate(action)
            viewModel.reinitializeGame()
            //giấu bottom nav bar
            val bottomNavigationView = activity?.findViewById<BottomNavigationView>(R.id.bottomNavigationView)
            bottomNavigationView?.visibility = View.GONE
        } else {
            binding.playerNameText.requestFocus()
            binding.playerNameText.error = "Please don't leave me blank :("
        }
    }

    //on click pika pic
    fun openLeaderboard() {
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