package com.life.totally.great.domain.usecases

import app.cash.turbine.test
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.data.models.WeatherResponse
import com.life.totally.great.domain.repositories.ForecastRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

class LoadWeatherUseCaseTest {

    private val weatherRepository: WeatherRepository = mockk()
    private val forecastRepository: ForecastRepository = mockk()
    private lateinit var useCase: LoadWeatherUseCase

    private val lat = 1.23
    private val lon = 4.56

    @Before
    fun setUp() {
        useCase = LoadWeatherUseCase(weatherRepository, forecastRepository)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `returns success when both weather and forecast succeed`() = runTest {
        val weatherResponse = mockk<WeatherResponse>()
        val forecastResponse = mockk<ForecastResponse>()

        coEvery { weatherRepository.getWeather(lat, lon) } returns flowOf(DataResult.Success(weatherResponse))
        coEvery { forecastRepository.getForecast(lat, lon) } returns flowOf(DataResult.Success(forecastResponse))

        useCase(lat, lon).test {
            val item = awaitItem()
            assert(item is DataResult.Success)
            val model = (item as DataResult.Success).data
            assertEquals(weatherResponse, model.currentWeather)
            assertEquals(forecastResponse, model.forecast)
            awaitComplete()
        }
    }

    @Test
    fun `emits error if weather fails`() = runTest {
        val error = mockk<WeatherError>()

        coEvery { weatherRepository.getWeather(lat, lon) } returns flowOf(DataResult.Error(error))
        // forecastRepository should not be called if weather fails, but let's mock it anyway
        coEvery { forecastRepository.getForecast(any(), any()) } returns flowOf()

        useCase(lat, lon).test {
            val item = awaitItem()
            assert(item is DataResult.Error)
            assertEquals(error, (item as DataResult.Error).error)
            awaitComplete()
        }
    }

    @Test
    fun `emits error if forecast fails`() = runTest {
        val weatherResponse = mockk<WeatherResponse>()
        val error = mockk<WeatherError>()

        coEvery { weatherRepository.getWeather(lat, lon) } returns flowOf(DataResult.Success(weatherResponse))
        coEvery { forecastRepository.getForecast(lat, lon) } returns flowOf(DataResult.Error(error))

        useCase(lat, lon).test {
            val item = awaitItem()
            assert(item is DataResult.Error)
            assertEquals(error, (item as DataResult.Error).error)
            awaitComplete()
        }
    }
}
