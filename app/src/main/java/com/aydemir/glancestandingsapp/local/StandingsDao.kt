package com.aydemir.glancestandingsapp.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.aydemir.glancestandingsapp.model.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface StandingsDao {
    @Query("SELECT * FROM teams")
    fun getTeams(): Flow<List<Team>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTeams(standings: List<Team>)

    @Query("DELETE FROM teams")
    fun deleteTeams()
}