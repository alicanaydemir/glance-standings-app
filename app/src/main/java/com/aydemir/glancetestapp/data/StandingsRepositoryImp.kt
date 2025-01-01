package com.aydemir.glancetestapp.data

import android.content.Context
import com.aydemir.glancetestapp.di.AppCoroutineScope
import com.aydemir.glancetestapp.local.DataStoreSelectedTeamManager
import com.aydemir.glancetestapp.local.StandingsDao
import com.aydemir.glancetestapp.model.DataSample
import com.aydemir.glancetestapp.model.Resource
import com.aydemir.glancetestapp.model.StandingUisState
import com.aydemir.glancetestapp.model.StandingsState
import com.aydemir.glancetestapp.model.Team
import dagger.hilt.EntryPoint
import dagger.hilt.EntryPoints
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.last
import kotlinx.coroutines.launch
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class StandingsRepositoryImp @Inject internal constructor(
    private val standingsDao: StandingsDao,
    @AppCoroutineScope private val coroutineScope: CoroutineScope,
    @ApplicationContext private val appContext: Context,
) {

    @EntryPoint
    @InstallIn(SingletonComponent::class)
    interface WidgetModelRepositoryEntryPoint {
        fun widgetModelRepository(): StandingsRepositoryImp
    }

    companion object {
        fun get(applicationContext: Context): StandingsRepositoryImp {
            val widgetModelRepositoryEntryPoint: WidgetModelRepositoryEntryPoint = EntryPoints.get(
                applicationContext,
                WidgetModelRepositoryEntryPoint::class.java,
            )
            return widgetModelRepositoryEntryPoint.widgetModelRepository()
        }
    }

    private fun showLoading() {
        _loading.value = StandingUisState.Loading
    }

    private fun hideLoading() {
        _loading.value = StandingUisState.Success
    }

    private val _loading = MutableStateFlow<StandingUisState>(StandingUisState.Success)
    val loading: StateFlow<StandingUisState> = _loading.asStateFlow()

    //-----------------------------------------------------------------------//

    fun getStandingsState(): Flow<StandingsState<List<Team>>> =
        DataStoreSelectedTeamManager.getData(appContext)
            .combine(standingsDao.getTeams()) { id, list ->
                if (id == 0) {
                    StandingsState.NoSelectedTeam
                } else {
                    if (list.isEmpty()) {
                        StandingsState.Empty
                    } else {
                        list.forEach { team ->
                            team.selected = id == team.id
                        }
                        StandingsState.Success(list)
                    }
                }
            }

    //-----------------------------------------------------------------------//

    suspend fun getStandings() = flow {
        val selectedTeamId = DataStoreSelectedTeamManager.getData(appContext).first()
        if (selectedTeamId == 0) {
            emit(Resource.Error(Exception()))
            return@flow
        }

        showLoading()
        deleteStandingsNoResult()

        //api request will be here
        delay(2000)

        //get data to insert room
        val result = insertStandingsLocal(selectedTeamId)
        when (result.last()) {
            is Resource.Success -> {
                hideLoading()
                emit(Resource.Success(Unit))
            }

            else -> {
                hideLoading()
                emit(Resource.Error(Exception()))
            }
        }
    }

    suspend fun deleteSelectedTeam(): Flow<Resource<Unit>> = flow {
        showLoading()
        //api request will be here
        delay(2000)

        deleteStandingsLocal().collect {
            hideLoading()
            emit(Resource.Success(Unit))
        }
    }

    suspend fun setSelectedTeam(selectedTeamId: Int): Flow<Resource<Unit>> = flow {
        showLoading()
        //api request will be here
        delay(2000)

        DataStoreSelectedTeamManager.saveData(appContext, selectedTeamId)
        hideLoading()
        emit(Resource.Success(Unit))
    }

    //-------------------------------Room-------------------------------//
    private suspend fun insertStandingsLocal(selectedTeamId: Int) = flow {
        val data = DataSample.getListStandings()
        emit(Resource.Loading)
        try {
            DataStoreSelectedTeamManager.saveData(appContext, selectedTeamId)
            standingsDao.insertTeams(data)
            delay(200)
            emit(Resource.Success(Unit))
        } catch (e: Exception) {
            emit(Resource.Error(e))
        }
    }

    private suspend fun deleteStandingsLocal(): Flow<Resource<Unit>> = flow {
        DataStoreSelectedTeamManager.saveData(appContext, 0)
        standingsDao.deleteTeams()
        delay(200)
        emit(Resource.Success(Unit))
    }

    fun deleteStandingsNoResult() {
        coroutineScope.launch {
            standingsDao.deleteTeams()
        }
    }
    //-----------------------------------------------------------------------//
}

