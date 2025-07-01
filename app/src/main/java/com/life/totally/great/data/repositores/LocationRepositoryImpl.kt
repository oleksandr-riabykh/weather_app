package com.life.totally.great.data.repositores

import com.life.totally.great.data.datasources.local.location.LocationDataSource
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.exceptions.mapExceptionToWeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.LocationRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.transform
import javax.inject.Inject

class LocationRepositoryImpl @Inject constructor(
    private val dataSource: LocationDataSource,
    private val dispatcher: CoroutineDispatcher
) : LocationRepository {

    override suspend fun getCoordinatesFlow(): Flow<DataResult<Coordinates, WeatherError>> =
        dataSource.getLocationFlow()
            .transform { location ->
                emit(DataResult.Success(Coordinates(location.longitude, location.latitude)))
            }
            .catch { e ->
                DataResult.Error(e.mapExceptionToWeatherError())
            }.flowOn(dispatcher)
}