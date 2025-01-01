package com.aydemir.glancetestapp.main

import android.content.Context
import androidx.glance.appwidget.GlanceAppWidgetManager
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydemir.glancetestapp.data.StandingsRepositoryImp
import com.aydemir.glancetestapp.local.DataStoreSelectedTeamManager
import com.aydemir.glancetestapp.model.Resource
import com.aydemir.glancetestapp.model.StandingUisState
import com.aydemir.glancetestapp.widget.GlanceTestAppWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val standingsRepositoryImp: StandingsRepositoryImp
) : ViewModel() {

    private val _hasSelectedTeamId = MutableStateFlow(0)
    val hasSelectedTeamId: StateFlow<Int> = _hasSelectedTeamId

    private val _loadingSave = MutableStateFlow<StandingUisState>(StandingUisState.Success)
    val loadingSave: StateFlow<StandingUisState> = _loadingSave.asStateFlow()

    private val _loadingDelete = MutableStateFlow<StandingUisState>(StandingUisState.Success)
    val loadingDelete: StateFlow<StandingUisState> = _loadingDelete.asStateFlow()

    init {
        getSelectedTeamData()
    }

    private fun getSelectedTeamData() {
        viewModelScope.launch(Dispatchers.IO) {
            DataStoreSelectedTeamManager.readData(appContext).collect {
                _hasSelectedTeamId.value = it
            }
        }
    }

    fun saveSelectedTeam(selectedTeamId: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingSave.value = StandingUisState.Loading
            standingsRepositoryImp.setSelectedTeam(selectedTeamId).collect {
                if (it is Resource.Success) {
                    _hasSelectedTeamId.value = selectedTeamId
                    updateWidget(appContext)
                }
                _loadingSave.value = StandingUisState.Success
            }
        }
    }

    fun deleteSelectedTeam() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingDelete.value = StandingUisState.Loading
            standingsRepositoryImp.deleteSelectedTeam().collect {
                if (it is Resource.Success) {
                    _hasSelectedTeamId.value = 0
                    updateWidget(appContext)
                }
                _loadingDelete.value = StandingUisState.Success
            }
        }
    }

    private suspend fun updateWidget(context: Context) {
        withContext(Dispatchers.Main) {
            val manager = GlanceAppWidgetManager(context)
            val widget = GlanceTestAppWidget()
            val glanceIds = manager.getGlanceIds(widget.javaClass)
            glanceIds.forEach { glanceId ->
                widget.update(context, glanceId)
            }
        }
    }
}