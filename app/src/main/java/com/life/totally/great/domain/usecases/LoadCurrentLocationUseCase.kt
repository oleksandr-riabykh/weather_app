package com.life.totally.great.domain.usecases

import com.life.totally.great.data.exceptions.WeatherError
import com.life.totally.great.data.models.Coordinates
import com.life.totally.great.data.models.DataResult
import com.life.totally.great.domain.repositories.LocationRepository
import com.life.totally.great.domain.scheduler.LocationUpdateScheduler
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.time.delay
import java.time.Duration
import javax.inject.Inject

// we could modify it to load weather and forecast here
class LoadCurrentLocationUseCase @Inject constructor(
    private val repository: LocationRepository,
    private val locationScheduler: LocationUpdateScheduler
) {
    suspend operator fun invoke(): Flow<DataResult<Coordinates, WeatherError>> {
        locationScheduler.schedulePeriodic()
        delay(Duration.ofMillis(500L)) // just to show loading state
        val observeCoordinates = repository.observeCoordinates()
        return observeCoordinates
    }
}