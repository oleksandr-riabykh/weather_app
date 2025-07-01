package com.life.totally.great.presentation.screens.weather

sealed class WeatherSideEffect {
    data class NavigateToDetails(val date: String) : WeatherSideEffect()
    data class ShowError(val message: String) : WeatherSideEffect()
}