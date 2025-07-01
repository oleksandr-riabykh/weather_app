package com.life.totally.great.data.datasources.local.location

import android.Manifest
import android.content.Context
import android.util.Log
import androidx.annotation.RequiresPermission
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.google.android.gms.location.LocationServices
import com.life.totally.great.data.persistance.LocationStoreManagerImpl
import com.life.totally.great.di.data.dataStore
import kotlinx.coroutines.tasks.await

class LocationUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {


    private val fusedLocationProviderClient =
        LocationServices.getFusedLocationProviderClient(appContext)
    private val locationStoreManager = LocationStoreManagerImpl(appContext.dataStore)

    @RequiresPermission(allOf = [Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION])
    override suspend fun doWork(): Result {
        return try {
            val loc = fusedLocationProviderClient.lastLocation.await()
            if (loc != null) {
                locationStoreManager.saveCoordinates(loc.latitude, loc.longitude)
            }
            Log.e("Coordinates tracking", "LocationUpdateWorker, saved coordinates: $loc")
            Result.success()
        } catch (e: Exception) {
            Result.retry()
        }
    }

    companion object {
        const val WORK_NAME = "LocationUpdateWorker"
    }
}
