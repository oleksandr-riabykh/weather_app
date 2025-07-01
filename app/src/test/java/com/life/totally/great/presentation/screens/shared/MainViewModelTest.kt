package com.life.totally.great.presentation.screens.shared

import app.cash.turbine.test
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.usecases.LoadCurrentLocationUseCase
import com.life.totally.great.domain.usecases.LoadWeatherUseCase
import com.life.totally.great.domain.usecases.SearchCityUseCase
import com.life.totally.great.presentation.screens.details.DetailsSideEffect
import com.life.totally.great.presentation.screens.weather.WeatherSideEffect
import com.life.totally.great.utils.factories.GeoLocationFactory
import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class MainViewModelTest {

    private val searchCityUseCase = mockk<SearchCityUseCase>()
    private val loadWeatherUseCase = mockk<LoadWeatherUseCase>()
    private val loadLocationUseCase = mockk<LoadCurrentLocationUseCase>()

    private lateinit var viewModel: MainViewModel
    private val testDispatcher = StandardTestDispatcher()

    private val cityName = "Berlin"
    private val date = "2025-07-01"
    private val city = GeoLocationFactory.create(name = cityName)
    private val cities = listOf(city, city)

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = MainViewModel(
            searchCityUseCase,
            loadWeatherUseCase,
            loadLocationUseCase
        )
    }

    @Test
    fun `SearchCity updates searchState is loading`() = runTest {
        coEvery { searchCityUseCase(cityName) } returns flowOf(DataResult.Success(cities))
        viewModel.processIntent(MainIntent.SearchCity(cityName))

        viewModel.searchState.test {
            val loading = awaitItem()
            assertTrue(loading is MainUiState.Idle)

            val success = awaitItem()
            assertTrue(success is MainUiState.Loading)
            cancelAndIgnoreRemainingEvents()
        }
    }

    @Test
    fun `CloseDetails emits close details effect`() = runTest {
        viewModel.processIntent(MainIntent.CloseDetails)
        viewModel.detailsEffect.test {
            val effect = awaitItem()
            assertEquals(DetailsSideEffect.CloseDetails, effect)
        }
    }

    @Test
    fun `ForecastItemClicked emits navigation effect`() = runTest {
        viewModel.processIntent(MainIntent.ForecastItemClicked(date))

        viewModel.effect.test {
            val effect = awaitItem()
            assertEquals(WeatherSideEffect.NavigateToDetails(date), effect)
        }
    }
}
