package com.example.pson.smarttest.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.pson.smarttest.R
import com.example.pson.smarttest.application.ScoreboardApplication
import com.example.pson.smarttest.databinding.FragmentGameBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import java.text.SimpleDateFormat
import java.util.*

class GameFragment : Fragment() {

    //viewModel
    private val viewModel: GameViewModel by activityViewModels {
        GameViewModelFactory(
            (activity?.application as ScoreboardApplication).database
                .ScoreboardDao()
        )
    }

    private lateinit var binding: FragmentGameBinding

    private lateinit var playerName: String

    private lateinit var bottomNavigationView: BottomNavigationView

    //this var prevent double click action
    private var firstClick = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil
            .inflate(inflater, R.layout.fragment_game, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //định nghĩa thanh bottom nav
        bottomNavigationView = activity?.findViewById(R.id.bottomNavigationView)!!

        //hiện action bar
        (activity as AppCompatActivity).supportActionBar?.show()

        //khởi tạo các element của xml
        binding.apply {
            gameViewModel = viewModel
            gameFragment = this@GameFragment
            lifecycleOwner = viewLifecycleOwner
        }

        //liên tục theo dõi thời gian còn lại và cập nhất theo tính huống
        viewModel.remainTime.observe(viewLifecycleOwner) { remainTime ->
            if (remainTime == 0L)
                nextAction()
        }
        //get argument from start fragment
        arguments.let {
            playerName = it?.getString("playerName").toString()
        }
    }

    override fun onStop() {
        super.onStop()
        bottomNavigationView.visibility = View.VISIBLE
    }

    // function của Layout
    fun onAnswerSubmitted(answer: Int) {
        //lấy nút user bấm
        val userAnswer: String = when (answer) {
            1 -> binding.answerA.text.toString()
            2 -> binding.answerB.text.toString()
            3 -> binding.answerC.text.toString()
            else -> binding.answerD.text.toString()
        }
        //cập nhật điểm -> chuyển câu hỏi / hiện thông báo kết thúc
        if (viewModel.isUserAnswerCorrect(userAnswer))
            viewModel.increaseScore()

        nextAction()
    }

    // tiếp / hiện thông báo kết thúc
    private fun nextAction() {
        //prevent double click event
        if (firstClick) {
            firstClick = false

            if (!viewModel.nextWord()) {
                //show result dialog
                showResultDialog()
                viewModel.freezeTime()

                //insert player result to database
                val playerScore = viewModel.score.value.toString()
                val playerTime = SimpleDateFormat("HH:mm, dd/MM/yyyy").format(Date())
                //decide to add new player or update higher score for old player
                viewModel.updateHigherScore(playerName, playerScore, playerTime)

            } else {
                viewModel.getNextQuestion()
                //return first click value only if vẫn còn câu hỏi
                firstClick = true
            }
        }
    }

    //thiết lập thông báo kết thúc
    private fun showResultDialog() {
        MaterialAlertDialogBuilder(requireContext())
            .setMessage(getString(R.string.score_dialog, viewModel.score.value))
            .setCancelable(false)
            .setNegativeButton(R.string.exit) { _, _ ->
                exitGame()
            }
            .setPositiveButton(R.string.restart) { _, _ ->
                restartGame()
            }
            .show()
    }

    private fun restartGame() {
        val action = GameFragmentDirections.actionGameFragmentToStartFragment(playerName = playerName)
        findNavController().navigate(action)
    }

    private fun exitGame() {
        activity?.finish()
    }
}