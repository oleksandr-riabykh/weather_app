package com.life.totally.great.domain.usecases.forecast

import app.cash.turbine.test
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.CityRepository
import com.life.totally.great.domain.repositories.ForecastRepository
import com.life.totally.great.utils.factories.ForecastResponseFactory
import com.life.totally.great.utils.factories.GeoLocationFactory
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
class LoadForecastByCityUseCaseTest {

    private val cityRepo = mockk<CityRepository>()
    private val forecastRepo = mockk<ForecastRepository>()

    private lateinit var loadForecastByCity: LoadForecastByCityUseCase

    private val testCityName = "LoadForecastTest"
    private val city = GeoLocationFactory.create(name = testCityName, lat = 1.0, lon = 2.0)
    private val responseCity = ForecastResponseFactory.createCity(name = testCityName)
    private val testResponse = ForecastResponseFactory.create(city = responseCity)
    private val testError = WeatherError.Unknown("SomeError")


    @Before
    fun setup() {
        loadForecastByCity = LoadForecastByCityUseCase(cityRepo, forecastRepo)
    }

    @Test
    fun `Positive scenario - LoadForecastByCityUseCaseTest success`() = runTest {

        coEvery { cityRepo.searchCity(testCityName) } returns flowOf(DataResult.Success(listOf(city)))
        coEvery { forecastRepo.getForecast(1.0, 2.0) } returns flowOf(
            DataResult.Success(
                testResponse
            )
        )

        loadForecastByCity(testCityName).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertEquals(testResponse, result.data)
            awaitComplete()
        }
    }


    @Test
    fun `Negative scenario - LoadForecastByCityUseCaseTest search error`() = runTest {
        coEvery { cityRepo.searchCity(testCityName) } returns flowOf(DataResult.Error(testError))

        loadForecastByCity(testCityName).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Error)
            assertEquals(testError, result.error)
            awaitComplete()
        }
    }
}
