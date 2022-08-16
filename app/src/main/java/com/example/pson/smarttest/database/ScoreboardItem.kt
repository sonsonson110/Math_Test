package com.example.pson.smarttest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "scoreboard")
data class ScoreboardItem (
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    @ColumnInfo(name = "name")
    val name: String,
    @ColumnInfo(name = "score")
    val score: String,
    @ColumnInfo(name = "time")
    val time: String
)