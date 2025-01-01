package com.aydemir.glancestandingsapp.local

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map

object DataStoreSelectedTeamManager {
    private val KEY_SELECTED_TEAM_ID = intPreferencesKey("selectedTeamId")

    suspend fun saveData(context: Context, value: Int) {
        context.dataStore.edit { preferences ->
            preferences[KEY_SELECTED_TEAM_ID] = value
        }
    }

    suspend fun readData(context: Context): Flow<Int> {
        val preferences = context.dataStore.data.first()
        return flow {
            emit(preferences[KEY_SELECTED_TEAM_ID] ?: 0)
        }
    }

    fun getData(context: Context): Flow<Int> {
        return context.dataStore.data.map { preference ->
            preference[KEY_SELECTED_TEAM_ID] ?: 0
        }
    }

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(
        name = "user_info"
    )
}