package com.aydemir.glancestandingsapp.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.aydemir.glancestandingsapp.model.Team
import javax.inject.Singleton

@Singleton
@Database(entities = [Team::class], version = 1, exportSchema = false)
abstract class StandingsDatabase : RoomDatabase() {
    abstract fun standingsDao(): StandingsDao
}