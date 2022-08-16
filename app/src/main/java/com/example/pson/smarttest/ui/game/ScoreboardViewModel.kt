package com.example.pson.smarttest.ui.game

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.pson.smarttest.database.ScoreboardDao
import com.example.pson.smarttest.database.ScoreboardItem
import kotlinx.coroutines.launch

class ScoreboardViewModel(private val scoreboardDao: ScoreboardDao) : ViewModel() {

    private fun insertItem(scoreboardItem: ScoreboardItem) {
        viewModelScope.launch {
            scoreboardDao.insert(scoreboardItem)
        }
    }

    private fun getNewScoreboardItemEntry(
        playerName: String,
        playerScore: String,
        playerTime: String) : ScoreboardItem
    {
        return ScoreboardItem(
            name = playerName,
            score = playerScore,
            time = playerTime
        )
    }

    fun addNewScoreboardItem(
        playerName: String,
        playerScore: String,
        playerTime: String
    ) {
        val newItem = getNewScoreboardItemEntry(playerName, playerScore, playerTime)
        insertItem(newItem)
    }
}

class ScoreboardViewModelFactory(private val scoreboardDao: ScoreboardDao)
    : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ScoreboardViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ScoreboardViewModel(scoreboardDao) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}