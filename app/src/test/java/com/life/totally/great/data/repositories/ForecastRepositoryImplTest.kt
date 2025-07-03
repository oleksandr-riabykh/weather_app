package com.life.totally.great.data.repositories

import app.cash.turbine.test
import com.life.totally.great.data.datasources.remote.forecast.ForecastRemoteDataSource
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.data.models.ForecastResponse
import com.life.totally.great.data.repositores.ForecastRepositoryImpl
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue


class ForecastRepositoryImplTest {

    private val remoteDataSource: ForecastRemoteDataSource = mockk()
    @OptIn(ExperimentalCoroutinesApi::class)
    private val dispatcher = UnconfinedTestDispatcher()
    private lateinit var repository: ForecastRepositoryImpl

    private val lat = 12.34
    private val lon = 56.78

    @Before
    fun setUp() {
        repository = ForecastRepositoryImpl(remoteDataSource, dispatcher)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `getForecast emits success when remoteDataSource returns data`() = runTest {
        val forecastResponse = mockk<ForecastResponse>()
        coEvery { remoteDataSource.getForecast(lat, lon) } returns forecastResponse

        repository.getForecast(lat, lon).test {
            val item = awaitItem()
            assertTrue(item is DataResult.Success)
            assertEquals(forecastResponse, item.data)
            awaitComplete()
        }
    }
}
