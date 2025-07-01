package com.life.totally.great.domain.usecases.weather

import app.cash.turbine.test
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.LocationRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import com.life.totally.great.domain.usecases.LoadCurrentLocationUseCase
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
class LoadWeatherByCurrentLocationUseCaseTest {

    private val weatherRepo = mockk<WeatherRepository>()
    private val locationRepo = mockk<LocationRepository>()

    private lateinit var loadWeatherCurrentLocation: LoadCurrentLocationUseCase

    private val testCityName = "LoadForecastTest"
    private val testWeatherResponse = WeatherResponseFactory.create(cityName = testCityName)
    private val coordinates = Coordinates(2.0, 1.0)
    private val testError = WeatherError.Unknown("SomeError")


    @Before
    fun setup() {
        loadWeatherCurrentLocation = LoadCurrentLocationUseCase(locationRepo, weatherRepo)
    }

    @Test
    fun `Positive scenario - LoadWeatherByCurrentLocationUseCaseTest success`() = runTest {

        coEvery { locationRepo.getCoordinatesFlow() } returns flowOf(DataResult.Success(coordinates))
        coEvery { weatherRepo.getWeather(coordinates.lat, coordinates.lon) } returns flowOf(
            DataResult.Success(
                testWeatherResponse
            )
        )

        loadWeatherCurrentLocation().test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertEquals(testWeatherResponse, result.data)
            awaitComplete()
        }
    }


    @Test
    fun `Negative scenario - LoadWeatherByCurrentLocationUseCaseTest weather api error`() =
        runTest {
            coEvery { locationRepo.getCoordinatesFlow() } returns flowOf(
                DataResult.Success(
                    coordinates
                )
            )
            coEvery { weatherRepo.getWeather(coordinates.lat, coordinates.lon) } returns flowOf(
                DataResult.Error(
                    testError
                )
            )

            loadWeatherCurrentLocation().test {
                val result = awaitItem()
                assertTrue(result is DataResult.Error)
                assertEquals(testError, result.error)
                awaitComplete()
            }
        }
}
