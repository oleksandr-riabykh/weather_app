package com.life.totally.great.domain.usecases.forecast

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.domain.repositories.ForecastRepository
import com.life.totally.great.domain.repositories.LocationRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class LoadForecastCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository,
    private val forecastRepository: ForecastRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(): Flow<DataResult<ForecastResponse, WeatherError>> =
        repository.getCoordinatesFlow()
            .mapNotNull { result ->
                when (result) {
                    is DataResult.Success -> result.data.let { coords ->
                        forecastRepository.getForecast(coords.lat, coords.lon)
                    }

                    is DataResult.Error -> flowOf(DataResult.Error(result.error))
                }
            }
            .flattenConcat()
}