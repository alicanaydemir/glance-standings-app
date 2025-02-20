package com.aydemir.glancestandingsapp.main

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.aydemir.glancestandingsapp.data.StandingsRepositoryImp
import com.aydemir.glancestandingsapp.local.DataStoreSelectedTeamManager
import com.aydemir.glancestandingsapp.model.Resource
import com.aydemir.glancestandingsapp.model.StandingsUiState
import com.aydemir.glancestandingsapp.util.updateWidget
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    @ApplicationContext private val appContext: Context,
    private val standingsRepositoryImp: StandingsRepositoryImp
) : ViewModel() {

    private val _hasSelectedTeamId = MutableStateFlow(0)
    val hasSelectedTeamId: StateFlow<Int> = _hasSelectedTeamId

    private val _loadingSave = MutableStateFlow<StandingsUiState>(StandingsUiState.Success)
    val loadingSave: StateFlow<StandingsUiState> = _loadingSave.asStateFlow()

    private val _loadingDelete = MutableStateFlow<StandingsUiState>(StandingsUiState.Success)
    val loadingDelete: StateFlow<StandingsUiState> = _loadingDelete.asStateFlow()

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
            _loadingSave.value = StandingsUiState.Loading
            standingsRepositoryImp.setSelectedTeam(selectedTeamId).collect {
                if (it is Resource.Success) {
                    _hasSelectedTeamId.value = selectedTeamId
                    appContext.updateWidget()
                }
                _loadingSave.value = StandingsUiState.Success
            }
        }
    }

    fun deleteSelectedTeam() {
        viewModelScope.launch(Dispatchers.IO) {
            _loadingDelete.value = StandingsUiState.Loading
            standingsRepositoryImp.deleteSelectedTeam().collect {
                if (it is Resource.Success) {
                    _hasSelectedTeamId.value = 0
                    appContext.updateWidget()
                }
                _loadingDelete.value = StandingsUiState.Success
            }
        }
    }
}