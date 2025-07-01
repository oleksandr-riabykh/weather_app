package com.life.totally.great.domain.repositories

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import kotlinx.coroutines.flow.Flow

interface LocationRepository {
    suspend fun observeCoordinates(): Flow<DataResult<Coordinates, WeatherError>>
}