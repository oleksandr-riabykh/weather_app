package com.life.totally.great.domain.usecases.weather

import app.cash.turbine.test
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.WeatherRepository
import com.life.totally.great.utils.factories.WeatherResponseFactory
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertTrue

@ExperimentalCoroutinesApi
class LoadWeatherByCoordinatesUseCaseTest {

    private val weatherRepo = mockk<WeatherRepository>()

    private lateinit var loadWeatherByCoords: LoadWeatherByCoordinatesUseCase
    private val testCityName = "LoadForecastTest"
    private val coordinates = Coordinates(-74.0, 40.0)
    private val testWeatherResponse = WeatherResponseFactory.create(cityName = testCityName)
    private val testError = WeatherError.Unknown("SomeError")


    @Before
    fun setup() {
        loadWeatherByCoords = LoadWeatherByCoordinatesUseCase(weatherRepo)
    }

    @Test
    fun `Positive scenario - LoadWeatherByCoordinatesUseCaseTest success`() = runTest {

        coEvery { weatherRepo.getWeather(coordinates.lat, coordinates.lon) } returns flowOf(
            DataResult.Success(
                testWeatherResponse
            )
        )

        loadWeatherByCoords(coordinates.lat, coordinates.lon).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertEquals(testWeatherResponse, result.data)
            awaitComplete()
        }
    }


    @Test
    fun `Negative scenario - LoadWeatherByCoordinatesUseCaseTest weather api error`() = runTest {
        coEvery { weatherRepo.getWeather(coordinates.lat, coordinates.lon) } returns flowOf(
            DataResult.Error(testError)
        )

        loadWeatherByCoords(coordinates.lat, coordinates.lon).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Error)
            assertEquals(testError, result.error)
            awaitComplete()
        }
    }
}
