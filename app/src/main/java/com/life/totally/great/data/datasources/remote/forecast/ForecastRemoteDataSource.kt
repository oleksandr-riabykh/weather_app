package com.life.totally.great.data.datasources.remote.forecast

import com.life.totally.great.data.models.ForecastResponse

interface ForecastRemoteDataSource {
    suspend fun getForecast(
        lat: Double,
        lon: Double,
    ): ForecastResponse
}