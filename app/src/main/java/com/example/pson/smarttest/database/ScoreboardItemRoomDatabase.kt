package com.example.pson.smarttest.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ScoreboardItem::class], version = 1, exportSchema = false)
abstract class ScoreboardItemRoomDatabase : RoomDatabase() {
    abstract fun ScoreboardDao(): ScoreboardDao

    companion object {
        @Volatile
        private var INSTANT : ScoreboardItemRoomDatabase? = null

        fun getDatabase(context: Context) : ScoreboardItemRoomDatabase {
            return INSTANT ?: synchronized(this) {
                val instant = Room.databaseBuilder(
                    context.applicationContext,
                    ScoreboardItemRoomDatabase::class.java,
                    "item_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANT = instant
                return instant
            }
        }
    }
}