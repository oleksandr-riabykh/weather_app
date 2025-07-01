package com.life.totally.great.presentation.screens.models

import com.life.totally.great.data.models.Coordinates

data class WeatherUiModel(
    val coord: Coordinates,
    val main: String,
    val description: String,
    val iconUrl: String,
    val temp: Double,
    val feelsLike: Double,
    val tempMin: Double,
    val tempMax: Double,
    val pressure: Int,
    val humidity: Int,
    val seaLevel: Int,
    val grndLevel: Int,
    val tempKf: Double?,
    val windSpeed: Double,
    val rainOneHourVolume: Double,
    val cityName: String,
    val dateString: String?,
    val timeString: String?,
    val datetime: Long,
    val hour: Int
)