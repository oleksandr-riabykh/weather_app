package com.life.totally.great.domain.usecases.forecast

import app.cash.turbine.test
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.ForecastRepository
import com.life.totally.great.domain.repositories.LocationRepository
import com.life.totally.great.utils.factories.ForecastResponseFactory
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
class LoadForecastCurrentLocationUseCaseTest {

    private val forecastRepo = mockk<ForecastRepository>()
    private val locationRepo = mockk<LocationRepository>()

    private lateinit var loadForecastCurrentLocation: LoadForecastCurrentLocationUseCase

    private val testCityName = "LoadForecastTest"
    private val coordinates = Coordinates(-74.0, 40.0)
    private val responseCity = ForecastResponseFactory.createCity(name = testCityName)
    private val testResponse = ForecastResponseFactory.create(city = responseCity)
    private val testError = WeatherError.Unknown("SomeError")


    @Before
    fun setup() {
        loadForecastCurrentLocation = LoadForecastCurrentLocationUseCase(locationRepo, forecastRepo)
    }

    @Test
    fun `Positive scenario - LoadForecastCurrentLocationUseCase success`() = runTest {
        coEvery { locationRepo.getCoordinatesFlow() } returns flowOf(DataResult.Success(coordinates))
        coEvery { forecastRepo.getForecast(coordinates.lat, coordinates.lon) } returns flowOf(
            DataResult.Success(
                testResponse
            )
        )

        loadForecastCurrentLocation().test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertEquals(testResponse, result.data)
            awaitComplete()
        }
    }


    @Test
    fun `Negative scenario - LoadForecastCurrentLocationUseCase search error`() = runTest {
        coEvery { locationRepo.getCoordinatesFlow() } returns flowOf(DataResult.Success(coordinates))
        coEvery { forecastRepo.getForecast(coordinates.lat, coordinates.lon) } returns flowOf(DataResult.Error(testError))

        loadForecastCurrentLocation().test {
            val result = awaitItem()
            assertTrue(result is DataResult.Error)
            assertEquals(testError, result.error)
            awaitComplete()
        }
    }
}
