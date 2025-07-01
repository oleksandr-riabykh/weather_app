package com.life.totally.great.presentation.screens.shared

sealed class MainIntent {
    data class SearchCity(val name: String) : MainIntent()
    data class LoadWeatherByCoordinates(val lat: Double, val lon: Double) : MainIntent()
    data class ForecastItemClicked(val date: String) : MainIntent()
    data class GetSelectedForecast(val date: String) : MainIntent()
    data object CloseDetails : MainIntent()
    data object LocationDenied : MainIntent()
    data object LocationGranted : MainIntent()
}