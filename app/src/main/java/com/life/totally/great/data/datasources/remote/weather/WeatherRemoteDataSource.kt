package com.life.totally.great.data.datasources.remote.weather

import com.life.totally.great.data.models.WeatherResponse

interface WeatherRemoteDataSource {
    suspend fun getWeather(
        lat: Double,
        lon: Double,
    ): WeatherResponse
}