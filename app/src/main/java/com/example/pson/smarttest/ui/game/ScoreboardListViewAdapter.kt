package com.example.pson.smarttest.ui.game

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.pson.smarttest.database.ScoreboardItem
import com.example.pson.smarttest.databinding.ScoreboardItemBinding

class ScoreboardListViewAdapter: androidx.recyclerview.widget.ListAdapter<ScoreboardItem, ScoreboardListViewAdapter.ItemViewHolder>(DiffCallback) {

    class ItemViewHolder(private val binding: ScoreboardItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(scoreboardItem: ScoreboardItem) {
            binding.apply {
                name.text = scoreboardItem.name
                score.text = scoreboardItem.score
                time.text = scoreboardItem.time
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        return ItemViewHolder(ScoreboardItemBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<ScoreboardItem>() {
            override fun areItemsTheSame(
                oldItem: ScoreboardItem,
                newItem: ScoreboardItem
            ): Boolean {
                return oldItem.time == newItem.time
            }

            override fun areContentsTheSame(
                oldItem: ScoreboardItem,
                newItem: ScoreboardItem
            ): Boolean {
                return oldItem == newItem
            }

        }
    }
}