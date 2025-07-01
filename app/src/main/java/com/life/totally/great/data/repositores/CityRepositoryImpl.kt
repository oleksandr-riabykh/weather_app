package com.life.totally.great.data.repositores

import com.life.totally.great.data.datasources.remote.city.CityRemoteDataSource
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.exceptions.mapExceptionToWeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.GeoLocation
import com.life.totally.great.domain.repositories.CityRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.retry
import java.io.IOException
import javax.inject.Inject

class CityRepositoryImpl @Inject constructor(
    private val remoteDataSource: CityRemoteDataSource,
    private val dispatcher: CoroutineDispatcher
) : CityRepository {

    override suspend fun searchCity(name: String): Flow<DataResult<List<GeoLocation>, WeatherError>> =
        flow {
            try {
                val result = remoteDataSource.getCitiesByName(name)
                emit(DataResult.Success(result))
            } catch (e: Exception) {
                emit(DataResult.Error(e.mapExceptionToWeatherError()))
            }
            // we can add here retryWhen and more advanced retry business logic
        }.retry(3) { e -> e is IOException }.flowOn(dispatcher)
}