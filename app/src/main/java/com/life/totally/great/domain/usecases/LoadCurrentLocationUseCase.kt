package com.life.totally.great.domain.usecases

import android.util.Log
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
        Log.e("Coordinates tracking", " locationScheduler.schedulePeriodic()")
        locationScheduler.isTracking = true
        locationScheduler.schedulePeriodic()
        delay(Duration.ofMillis(500L))
        val observeCoordinates = repository.observeCoordinates()
        Log.e("Coordinates tracking", " observeCoordinates, observeCoordinates coordinates: $observeCoordinates")
        return observeCoordinates
    }
}