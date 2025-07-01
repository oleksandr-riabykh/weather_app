package com.life.totally.great.domain.usecases

import app.cash.turbine.test
import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.LocationRepository
import com.life.totally.great.domain.scheduler.LocationUpdateScheduler
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.just
import io.mockk.mockk
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.currentTime
import kotlinx.coroutines.test.runTest
import org.junit.After
import org.junit.Before
import org.junit.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class LoadCurrentLocationUseCaseTest {

    private val repository: LocationRepository = mockk()
    private val locationScheduler: LocationUpdateScheduler = mockk()
    private lateinit var useCase: LoadCurrentLocationUseCase

    @Before
    fun setUp() {
        useCase = LoadCurrentLocationUseCase(repository, locationScheduler)
    }

    @After
    fun tearDown() {
        clearAllMocks()
    }

    @Test
    fun `invoke schedules updates and returns coordinates after delay`() = runTest {
        val testCoordinates = mockk<Coordinates>()
        val testFlow = flowOf(DataResult.Success(testCoordinates))

        coEvery { locationScheduler.schedulePeriodic() } just Runs
        coEvery { repository.observeCoordinates() } returns testFlow

        useCase().test {
            // Verify schedulePeriodic is called
            coVerify(exactly = 1) { locationScheduler.schedulePeriodic() }

            // Should emit the coordinates after delay
            val result = awaitItem()
            assert(result is DataResult.Success)
            assertEquals(testCoordinates, (result as DataResult.Success).data)
            awaitComplete()
        }
    }

    @Test
    fun `invoke returns error flow from repository`() = runTest {
        val testError = mockk<WeatherError>()
        val testFlow = flowOf(DataResult.Error(testError))

        coEvery { locationScheduler.schedulePeriodic() } just Runs
        coEvery { repository.observeCoordinates() } returns testFlow

        useCase().test {
            val result = awaitItem()
            assert(result is DataResult.Error)
            assertEquals(testError, (result as DataResult.Error).error)
            awaitComplete()
        }
    }

    @Test
    fun `invoke delays before emitting`() = runTest {
        val testCoordinates = mockk<Coordinates>()
        val testFlow = flowOf(DataResult.Success(testCoordinates))

        coEvery { locationScheduler.schedulePeriodic() } just Runs
        coEvery { repository.observeCoordinates() } returns testFlow

        val startTime = currentTime
        useCase().test {
            awaitItem()
            val elapsed = currentTime - startTime
            assert(elapsed >= 500) // 500ms delay should have passed
            awaitComplete()
        }
    }
}

