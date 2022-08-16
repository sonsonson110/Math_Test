package com.example.pson.smarttest.application

import android.app.Application
import com.example.pson.smarttest.database.ScoreboardItemRoomDatabase

class ScoreboardApplication : Application() {
    val database: ScoreboardItemRoomDatabase by lazy {
        ScoreboardItemRoomDatabase.getDatabase(this)
    }
}