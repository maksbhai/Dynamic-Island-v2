package com.maks.dynamicislandv2.data

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.floatPreferencesKey
import androidx.datastore.preferences.core.longPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.maks.dynamicislandv2.domain.IslandState
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore(name = "island_settings")

class SettingsDataStore(private val context: Context) {

    private object Keys {
        val enabled = booleanPreferencesKey("enabled")
        val widthDp = floatPreferencesKey("width_dp")
        val heightDp = floatPreferencesKey("height_dp")
        val topOffsetDp = floatPreferencesKey("top_offset_dp")
        val hideSensitive = booleanPreferencesKey("hide_sensitive")
        val autoCollapseMs = longPreferencesKey("auto_collapse_ms")
    }

    val settingsFlow: Flow<IslandState> = context.dataStore.data.map { prefs ->
        IslandState(
            enabled = prefs[Keys.enabled] ?: true,
            widthDp = prefs[Keys.widthDp] ?: 160f,
            heightDp = prefs[Keys.heightDp] ?: 44f,
            topOffsetDp = prefs[Keys.topOffsetDp] ?: 28f,
            hideSensitive = prefs[Keys.hideSensitive] ?: false,
            autoCollapseMs = prefs[Keys.autoCollapseMs] ?: 3500L
        )
    }

    suspend fun save(state: IslandState) {
        context.dataStore.edit { prefs ->
            prefs[Keys.enabled] = state.enabled
            prefs[Keys.widthDp] = state.widthDp
            prefs[Keys.heightDp] = state.heightDp
            prefs[Keys.topOffsetDp] = state.topOffsetDp
            prefs[Keys.hideSensitive] = state.hideSensitive
            prefs[Keys.autoCollapseMs] = state.autoCollapseMs
        }
    }

    suspend fun reset() = save(IslandState())
}
