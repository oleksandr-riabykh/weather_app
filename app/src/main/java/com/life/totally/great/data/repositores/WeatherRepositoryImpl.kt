package com.life.totally.great.data.repositores

import com.life.totally.great.data.datasources.remote.weather.WeatherRemoteDataSource
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.exceptions.mapExceptionToWeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.WeatherResponse
import com.life.totally.great.data.persistance.LocationStoreManager
import com.life.totally.great.domain.repositories.WeatherRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import okio.IOException
import javax.inject.Inject

class WeatherRepositoryImpl @Inject constructor(
    private val remoteDataSource: WeatherRemoteDataSource,
    private val locationStoreManager: LocationStoreManager,
    private val dispatcher: CoroutineDispatcher
) : WeatherRepository {

    override suspend fun getWeather(
        lat: Double,
        lon: Double,
    ): Flow<DataResult<WeatherResponse, WeatherError>> =
        flow {
            try {
                locationStoreManager.saveCoordinates(lat, lon)
                val result = remoteDataSource.getWeather(lat, lon)
                emit(DataResult.Success(result))
            } catch (e: Exception) {
                emit(DataResult.Error(e.mapExceptionToWeatherError()))
            }
            // we can add here retryWhen and more advanced retry business logic
        }.retry(3) { e -> e is IOException }.flowOn(dispatcher)
}