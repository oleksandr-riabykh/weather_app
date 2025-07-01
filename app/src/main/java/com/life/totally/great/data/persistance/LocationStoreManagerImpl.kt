package com.life.totally.great.data.persistance

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import com.life.totally.great.data.models.Coordinates
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class LocationStoreManagerImpl @Inject constructor(
    private val dataStore: DataStore<Preferences>
) : LocationStoreManager {
    private val mLat = doublePreferencesKey("lat")
    private val mLon = doublePreferencesKey("lon")

    override val coordsFlow: Flow<Coordinates?> = dataStore.data.map { prefs ->
        val lat = prefs[mLat]
        val lon = prefs[mLon]
        if (lat != null && lon != null) Coordinates(lon, lat) else null
    }

    override suspend fun saveCoordinates(lat: Double, lon: Double) {
        dataStore.edit { prefs ->
            prefs[mLat] = lat
            prefs[mLon] = lon
        }
    }
}