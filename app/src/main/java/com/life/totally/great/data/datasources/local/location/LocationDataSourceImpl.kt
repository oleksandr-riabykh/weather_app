package com.life.totally.great.data.datasources.local.location

import android.annotation.SuppressLint
import android.content.Context
import android.location.Location
import android.os.Looper
import com.google.android.gms.location.LocationCallback
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationResult
import com.google.android.gms.location.LocationServices
import com.google.android.gms.location.Priority
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.conflate
import javax.inject.Inject

class LocationDataSourceImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocationDataSource {

    @SuppressLint("MissingPermission")
    override fun getLocationFlow(): Flow<Location> = callbackFlow {
        val provider = LocationServices.getFusedLocationProviderClient(context)

        val callback = object : LocationCallback() {
            override fun onLocationResult(result: LocationResult) {
                result.lastLocation?.let { trySend(it).isSuccess }
            }
        }

        val request = LocationRequest.Builder(Priority.PRIORITY_HIGH_ACCURACY, 50_000)
            .setMinUpdateIntervalMillis(20_000)
            .build()

        provider.requestLocationUpdates(request, callback, Looper.getMainLooper())
        awaitClose { provider.removeLocationUpdates(callback) }
    }.conflate()
}
