package com.example.pson.smarttest.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreboardDao {
    @Query("SELECT * from scoreboard ORDER BY score DESC")
    fun getTopResults(): Flow<List<ScoreboardItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ScoreboardItem)
}