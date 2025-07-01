package com.life.totally.great.data.repositores

import com.life.totally.great.data.datasources.remote.forecast.ForecastRemoteDataSource
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.exceptions.mapExceptionToWeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.domain.repositories.ForecastRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import java.io.IOException
import javax.inject.Inject

class ForecastRepositoryImpl @Inject constructor(
    private val remoteDataSource: ForecastRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : ForecastRepository {

    override suspend fun getForecast(
        lat: Double,
        lon: Double,
    ): Flow<DataResult<ForecastResponse, WeatherError>> =
        flow {
            try {
                val result = remoteDataSource.getForecast(lat, lon)
                emit(DataResult.Success(result))
            } catch (e: Exception) {
                emit(DataResult.Error(e.mapExceptionToWeatherError()))
            }
            // we can add here retryWhen and more advanced retry business logic
        }.retry(3) { e -> e is IOException }.flowOn(dispatcher)
}