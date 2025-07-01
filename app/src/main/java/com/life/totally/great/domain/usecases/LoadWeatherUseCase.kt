package com.life.totally.great.domain.usecases

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.data.models.WeatherResponse
import com.life.totally.great.domain.models.WeatherDomainModel
import com.life.totally.great.domain.repositories.ForecastRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.channelFlow
import javax.inject.Inject

class LoadWeatherUseCase @Inject constructor(
    private val weatherRepository: WeatherRepository,
    private val forecastRepository: ForecastRepository
) {
    // can be modified later
    suspend operator fun invoke(
        lat: Double,
        lon: Double
    ): Flow<DataResult<WeatherDomainModel, WeatherError>> = channelFlow {
        var weather: WeatherResponse? = null
        var forecast: ForecastResponse? = null

        weatherRepository.getWeather(lat, lon).collect { result ->
            when (result) {
                is DataResult.Success -> weather = result.data
                is DataResult.Error -> send(DataResult.Error(result.error))
            }
        }

        forecastRepository.getForecast(lat, lon).collect { result ->
            when (result) {
                is DataResult.Success -> forecast = result.data
                is DataResult.Error -> send(DataResult.Error(result.error))
            }
        }

        if (weather != null && forecast != null) {
            send(DataResult.Success(WeatherDomainModel(weather!!, forecast!!)))
        }
    }
}
