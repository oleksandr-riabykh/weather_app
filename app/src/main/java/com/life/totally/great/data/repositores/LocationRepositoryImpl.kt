package com.life.totally.great.data.repositores

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.persistance.LocationStoreManager
import com.life.totally.great.domain.repositories.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val dataStore: LocationStoreManager,
    private val dispatcher: CoroutineDispatcher
) : LocationRepository {

    override suspend fun observeCoordinates() = dataStore.coordsFlow
        .map {
            if (it != null) DataResult.Success(it)
            else DataResult.Error(WeatherError.NoLocation("No location saved"))
        }
        .flowOn(dispatcher)
}