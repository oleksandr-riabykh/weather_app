package com.life.totally.great.domain.usecases.weather

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.WeatherResponse
import com.life.totally.great.domain.repositories.CityRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flattenConcat
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.mapNotNull
import javax.inject.Inject

class LoadWeatherByCityUseCase @Inject constructor(
    private val cityRepository: CityRepository,
    private val weatherRepository: WeatherRepository
) {
    @OptIn(ExperimentalCoroutinesApi::class)
    suspend operator fun invoke(name: String): Flow<DataResult<WeatherResponse, WeatherError>> =
        cityRepository.searchCity(name)
            .mapNotNull { result ->
                when (result) {
                    is DataResult.Success -> result.data.firstOrNull()?.let { city ->
                        weatherRepository.getWeather(city.lat, city.lon)
                    }
                    is DataResult.Error -> flowOf(DataResult.Error(result.error))
                }
            }
            .flattenConcat()
}