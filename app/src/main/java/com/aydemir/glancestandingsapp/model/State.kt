package com.aydemir.glancestandingsapp.model

sealed class Resource<out T> {
    data class Success<out T>(val data: T) : Resource<T>()
    data object Loading : Resource<Nothing>()
    data class Error(val exception: Throwable) : Resource<Nothing>()
}

sealed class StandingsState<out T> {
    data class Success<out T>(val data: T) : StandingsState<T>()
    data object Empty : StandingsState<Nothing>()
    data object NoSelectedTeam : StandingsState<Nothing>()
    data class Error(val exception: Throwable) : StandingsState<Nothing>()
}

sealed interface StandingUisState {
    data object Success : StandingUisState
    data object Loading : StandingUisState
}