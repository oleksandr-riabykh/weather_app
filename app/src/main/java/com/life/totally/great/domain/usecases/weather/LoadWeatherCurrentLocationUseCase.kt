package com.life.totally.great.domain.usecases.weather

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.WeatherResponse
import com.life.totally.great.domain.repositories.LocationRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class LoadWeatherCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository,
    private val weatherRepository: WeatherRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<DataResult<WeatherResponse, WeatherError>> =
        repository.getCoordinatesFlow()
            .mapNotNull { result ->
                when (result) {
                    is DataResult.Success -> result.data.let { coords ->
                        weatherRepository.getWeather(coords.lat, coords.lon)
                    }

                    is DataResult.Error -> flowOf(DataResult.Error(result.error))
                }
            }
            .flattenConcat()
}