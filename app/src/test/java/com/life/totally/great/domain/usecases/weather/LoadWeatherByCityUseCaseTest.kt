package com.life.totally.great.domain.usecases.weather

import app.cash.turbine.test
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.CityRepository
import com.life.totally.great.domain.repositories.WeatherRepository
import com.life.totally.great.domain.usecases.LoadWeatherByCityUseCase
import com.life.totally.great.utils.factories.GeoLocationFactory
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
class LoadWeatherByCityUseCaseTest {

    private val cityRepo = mockk<CityRepository>()
    private val weatherRepo = mockk<WeatherRepository>()

    private lateinit var loadWeatherByCity: LoadWeatherByCityUseCase

    private val testCityName = "LoadWeatherTest"
    private val coordinates = Coordinates(-74.0, 40.0)
    private val cities = GeoLocationFactory.createList()
    private val testWeatherResponse = WeatherResponseFactory.create(cityName = testCityName)
    private val testError = WeatherError.Unknown("SomeError")


    @Before
    fun setup() {
        loadWeatherByCity = LoadWeatherByCityUseCase(cityRepo, weatherRepo)
    }

    @Test
    fun `Positive scenario - LoadWeatherByCityUseCaseTest success`() = runTest {

        coEvery { cityRepo.searchCity(testCityName) } returns flowOf(DataResult.Success(cities))
        coEvery { weatherRepo.getWeather(coordinates.lat, coordinates.lon) } returns flowOf(
            DataResult.Success(
                testWeatherResponse
            )
        )

        loadWeatherByCity(testCityName).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertEquals(testWeatherResponse, result.data)
            awaitComplete()
        }
    }


    @Test
    fun `Negative scenario - LoadWeatherByCityUseCaseTest weather api error`() = runTest {
        coEvery { cityRepo.searchCity(testCityName) } returns flowOf(DataResult.Success(cities))
        coEvery { weatherRepo.getWeather(coordinates.lat, coordinates.lon) } returns flowOf(
            DataResult.Error(testError)
        )

        loadWeatherByCity(testCityName).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Error)
            assertEquals(testError, result.error)
            awaitComplete()
        }
    }
}
