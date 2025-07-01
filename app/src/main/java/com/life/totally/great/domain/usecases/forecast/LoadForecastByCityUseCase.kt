package com.life.totally.great.domain.usecases.forecast

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.domain.repositories.CityRepository
import com.life.totally.great.domain.repositories.ForecastRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class LoadForecastByCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val forecastRepository: ForecastRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(name: String): Flow<DataResult<ForecastResponse, WeatherError>> =
        cityRepository.searchCity(name)
            .mapNotNull { result ->
                when (result) {
                    is DataResult.Success -> result.data.firstOrNull()?.let { city ->
                        forecastRepository.getForecast(city.lat, city.lon)
                    }

                    is DataResult.Error -> flowOf(DataResult.Error(result.error))
                }
            }
            .flattenConcat()
}