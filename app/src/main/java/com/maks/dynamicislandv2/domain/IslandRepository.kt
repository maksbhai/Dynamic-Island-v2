package com.maks.dynamicislandv2.domain

import com.maks.dynamicislandv2.data.SettingsDataStore
import kotlinx.coroutines.flow.Flow

class IslandRepository(private val dataStore: SettingsDataStore) {
    val settings: Flow<IslandState> = dataStore.settingsFlow

    suspend fun update(state: IslandState) = dataStore.save(state)

    suspend fun reset() = dataStore.reset()
}
