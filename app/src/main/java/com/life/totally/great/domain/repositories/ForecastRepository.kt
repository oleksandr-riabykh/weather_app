package com.life.totally.great.domain.repositories

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.ForecastResponse
import kotlinx.coroutines.flow.Flow

interface ForecastRepository {
    suspend fun getForecast(
        lat: Double,
        lon: Double
    ): Flow<DataResult<ForecastResponse, WeatherError>>
}