package com.example.pson.smarttest.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.pson.smarttest.R
import com.example.pson.smarttest.databinding.FragmentGameBinding
import com.google.android.material.dialog.MaterialAlertDialogBuilder

open class GameFragment : Fragment() {

    //viewModel
    private val viewModel: GameViewModel by viewModels()

    private lateinit var binding: FragmentGameBinding

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

        //khởi tạo các element của xml
        binding.apply {
            gameViewModel = viewModel
            gameFragment = this@GameFragment
            lifecycleOwner = viewLifecycleOwner
        }
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

        if (!viewModel.nextWord()) {
            showResultDialog()
            viewModel.freeTime()
        } else {
            viewModel.getNextQuestion()
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
        viewModel.reinitializeGame()
    }

    private fun exitGame() {
        activity?.finish()
    }
}