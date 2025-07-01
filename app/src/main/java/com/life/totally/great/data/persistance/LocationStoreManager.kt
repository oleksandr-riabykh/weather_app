package com.life.totally.great.data.persistance

import com.life.totally.great.data.models.Coordinates
import kotlinx.coroutines.flow.Flow


interface LocationStoreManager {

    val coordsFlow: Flow<Coordinates?>

    suspend fun saveCoordinates(lat: Double, lon: Double)
}