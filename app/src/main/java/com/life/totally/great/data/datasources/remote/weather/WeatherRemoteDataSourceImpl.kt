package com.life.totally.great.data.datasources.remote.weather

import com.life.totally.great.data.api.ApiService
import com.life.totally.great.data.models.WeatherResponse
import javax.inject.Inject

class WeatherRemoteDataSourceImpl @Inject constructor(
    private val api: ApiService
) : WeatherRemoteDataSource {

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
    ): WeatherResponse {
        return api.getWeather(lat, lon)
    }
}