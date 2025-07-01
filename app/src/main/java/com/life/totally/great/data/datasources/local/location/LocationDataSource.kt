package com.life.totally.great.data.datasources.local.location

import android.location.Location
import kotlinx.coroutines.flow.Flow

interface LocationDataSource {
    fun getLocationFlow(): Flow<Location>
}