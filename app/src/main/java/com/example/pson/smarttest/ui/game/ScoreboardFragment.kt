package com.example.pson.smarttest.ui.game

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.pson.smarttest.application.ScoreboardApplication
import com.example.pson.smarttest.databinding.FragmentScoreboardBinding

class ScoreboardFragment : Fragment() {

    //viewModel
    private val viewModel: GameViewModel by activityViewModels {
        GameViewModelFactory(
            (activity?.application as ScoreboardApplication).database
                .ScoreboardDao()
        )
    }

    private lateinit var binding: FragmentScoreboardBinding

    private lateinit var recyclerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        binding = FragmentScoreboardBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.lifecycleOwner = this.viewLifecycleOwner

        recyclerView = binding.recyclerView
        val adapter = ScoreboardListViewAdapter()
        recyclerView.adapter = adapter
        //update scoreboard
        viewModel.topResults.observe(viewLifecycleOwner) { scoreboardItem ->
            scoreboardItem.let {
                adapter.submitList(it)
            }
        }
        //set first empty init scoreboard
        if (viewModel.noResult()) {
            binding.message.text = "Be the first dominator \n(╯▔皿▔)╯"
        } else {
            binding.message.visibility = View.INVISIBLE
        }
    }
}