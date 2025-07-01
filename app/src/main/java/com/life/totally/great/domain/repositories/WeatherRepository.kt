package com.life.totally.great.domain.repositories

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.WeatherResponse
import kotlinx.coroutines.flow.Flow

interface WeatherRepository {
    suspend fun getWeather(
        lat: Double,
        lon: Double
    ): Flow<DataResult<WeatherResponse, WeatherError>>
}