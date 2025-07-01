package com.life.totally.great.presentation.screens.shared

import com.life.totally.great.data.models.GeoLocation

sealed class MainIntent {
    data class SearchCity(val name: String) : MainIntent()
    data class LoadForecastByCity(val city: GeoLocation) : MainIntent()
    data class LoadWeatherByCity(val city: GeoLocation) : MainIntent()
    data class LoadWeatherByCoordinates(val lat: Double, val lon: Double) : MainIntent()
    data class ForecastItemClicked(val date: String) : MainIntent()
    data class GetSelectedForecast(val date: String) : MainIntent()
    data object CloseDetails : MainIntent()
    data object RequestLocation : MainIntent()
    data object LocationDenied : MainIntent()
    data object LocationGranted : MainIntent()
}