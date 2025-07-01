package com.life.totally.great.domain.models

import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.data.models.WeatherResponse

data class WeatherDomainModel(
    val currentWeather: WeatherResponse,
    val forecast: ForecastResponse,
)
