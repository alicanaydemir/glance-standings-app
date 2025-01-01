package com.aydemir.glancetestapp.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.aydemir.glancetestapp.R
import com.aydemir.glancetestapp.model.Team

@Database(entities = [Team::class], version = 2, exportSchema = false)
abstract class StandingsDatabase : RoomDatabase() {
    abstract fun standingsDao(): StandingsDao

    companion object {
        @Volatile
        private var INSTANCE: StandingsDatabase? = null

        fun getInstance(context: Context): StandingsDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    StandingsDatabase::class.java,
                    context.getString(R.string.standings_database)
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}