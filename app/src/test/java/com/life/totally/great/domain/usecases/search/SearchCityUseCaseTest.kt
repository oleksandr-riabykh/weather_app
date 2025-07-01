package com.life.totally.great.domain.usecases.search

import app.cash.turbine.test
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.CityRepository
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
class SearchCityUseCaseTest {

    private val cityRepo = mockk<CityRepository>()

    private lateinit var searchCity: SearchCityUseCase

    private val testCityName = "SearchTest"
    private val coordinates = Coordinates(-74.0, 40.0)
    private val city =
        GeoLocationFactory.create(name = testCityName, lat = coordinates.lat, lon = coordinates.lon)
    private val cities = listOf(city, city)
    private val testError = WeatherError.Unknown("SomeError")


    @Before
    fun setup() {
        searchCity = SearchCityUseCase(cityRepo)
    }

    @Test
    fun `Positive scenario - SearchCityUseCaseTest success`() = runTest {

        coEvery { cityRepo.searchCity(testCityName) } returns flowOf(DataResult.Success(cities))

        searchCity(testCityName).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Success)
            assertEquals(cities, result.data)
            awaitComplete()
        }
    }


    @Test
    fun `Negative scenario - SearchCityUseCaseTest search error`() = runTest {
        coEvery { cityRepo.searchCity(testCityName) } returns flowOf(DataResult.Error(testError))

        searchCity(testCityName).test {
            val result = awaitItem()
            assertTrue(result is DataResult.Error)
            assertEquals(testError, result.error)
            awaitComplete()
        }
    }
}
