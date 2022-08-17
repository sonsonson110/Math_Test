package com.example.pson.smarttest.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ScoreboardDao {
    @Query("SELECT * from scoreboard ORDER BY score DESC")
    fun getTopResults(): Flow<List<ScoreboardItem>>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(item: ScoreboardItem)

    //get an item base on name
    @Query("SELECT * FROM scoreboard WHERE name = :name")
    suspend fun getPlayerResultWithName(name: String): ScoreboardItem?

    //update an item base on name
    @Query("UPDATE scoreboard SET score = :score, time = :time WHERE name = :name")
    suspend fun update(name: String, score: String, time: String)
}