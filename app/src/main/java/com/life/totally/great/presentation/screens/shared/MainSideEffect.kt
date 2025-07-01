package com.life.totally.great.presentation.screens.shared

sealed class MainSideEffect {
    data class NavigateToDetails(val date: String) : MainSideEffect()
    data class ShowError(val message: String) : MainSideEffect()
    data object RequestLocationPermission : MainSideEffect()
    data object CloseDetails : MainSideEffect()
}