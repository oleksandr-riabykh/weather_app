package com.life.totally.great.presentation.screens.shared

import com.life.totally.great.data.exceptions.WeatherError

sealed class MainUiState<out T> {
    data object Idle : MainUiState<Nothing>()
    data object Loading : MainUiState<Nothing>()
    data class Success<T>(val data: T) : MainUiState<T>()
    data class Error(val error: WeatherError) : MainUiState<Nothing>()
}