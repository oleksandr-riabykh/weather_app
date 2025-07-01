package com.life.totally.great.data.datasources.remote.forecast

import com.life.totally.great.data.api.ApiService
import com.life.totally.great.data.models.ForecastResponse
import javax.inject.Inject

class ForecastRemoteDataSourceImpl @Inject constructor(
    private val api: ApiService
) : ForecastRemoteDataSource {

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
    ): ForecastResponse {
        return api.getForecast(lat, lon)
    }
}