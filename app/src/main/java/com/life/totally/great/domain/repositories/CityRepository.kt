package com.life.totally.great.domain.repositories

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.GeoLocation
import kotlinx.coroutines.flow.Flow

interface CityRepository {
    suspend fun searchCity(name: String): Flow<DataResult<List<GeoLocation>, WeatherError>>
}