package com.life.totally.great.domain.usecases.weather

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.WeatherResponse
import com.life.totally.great.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class LoadWeatherByCoordinatesUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository
) {
    suspend operator fun invoke(lat: Double, lon: Double): Flow<DataResult<WeatherResponse, WeatherError>> {
        return weatherRepository.getWeather(lat, lon)
    }
}