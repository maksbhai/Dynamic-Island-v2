package com.maks.dynamicislandv2.viewmodel

import android.app.Application
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.PowerManager
import android.provider.Settings
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.maks.dynamicislandv2.data.SettingsDataStore
import com.maks.dynamicislandv2.domain.IslandRepository
import com.maks.dynamicislandv2.domain.IslandState
import com.maks.dynamicislandv2.domain.PermissionStatus
import com.maks.dynamicislandv2.services.IslandOverlayService
import com.maks.dynamicislandv2.services.OverlayServiceTracker
import com.maks.dynamicislandv2.utils.PermissionUtils
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = IslandRepository(SettingsDataStore(application))
    private val context = application.applicationContext

    private val permissionState = MutableStateFlow(readPermissionStatus())
    val settings = repository.settings.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, IslandState())

    val uiState: StateFlow<MainUiState> = combine(settings, permissionState, OverlayServiceTracker.running) { setting, permission, running ->
        MainUiState(setting, permission.copy(overlayServiceRunning = running))
    }.stateIn(viewModelScope, kotlinx.coroutines.flow.SharingStarted.Eagerly, MainUiState())

    fun refreshPermissions() {
        permissionState.value = readPermissionStatus()
    }

    fun openOverlayPermission(context: Context) {
        val intent = Intent(
            Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
            Uri.parse("package:${context.packageName}")
        )
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun openNotificationAccess(context: Context) {
        val intent = Intent(Settings.ACTION_NOTIFICATION_LISTENER_SETTINGS)
        context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
    }

    fun openBatteryOptimization(context: Context) {
        val pm = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        if (!pm.isIgnoringBatteryOptimizations(context.packageName)) {
            val intent = Intent(
                Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS,
                Uri.parse("package:${context.packageName}")
            )
            context.startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK))
        }
    }

    fun startOverlayService(showTest: Boolean = false) {
        val intent = Intent(context, IslandOverlayService::class.java).apply {
            action = if (showTest) IslandOverlayService.ACTION_SHOW_TEST else null
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    fun showTestIsland() = startOverlayService(showTest = true)

    fun resetIslandPosition() {
        viewModelScope.launch {
            repository.reset()
        }
        sendServiceAction(IslandOverlayService.ACTION_RESET_POSITION)
    }

    fun setEnabled(enabled: Boolean) {
        viewModelScope.launch {
            repository.update(settings.value.copy(enabled = enabled))
        }
    }

    fun setSize(width: Float, height: Float, topOffset: Float) {
        viewModelScope.launch {
            repository.update(
                settings.value.copy(
                    widthDp = width.coerceIn(120f, 360f),
                    heightDp = height.coerceIn(34f, 140f),
                    topOffsetDp = topOffset.coerceIn(12f, 180f)
                )
            )
        }
    }

    fun setHideSensitive(hide: Boolean) {
        viewModelScope.launch { repository.update(settings.value.copy(hideSensitive = hide)) }
    }

    fun setAutoCollapseMs(ms: Long) {
        viewModelScope.launch { repository.update(settings.value.copy(autoCollapseMs = ms.coerceIn(1500L, 10000L))) }
    }

    private fun sendServiceAction(action: String) {
        val intent = Intent(context, IslandOverlayService::class.java).apply { this.action = action }
        context.startService(intent)
    }

    private fun readPermissionStatus(): PermissionStatus = PermissionStatus(
        overlayGranted = PermissionUtils.canDrawOverlays(context),
        notificationAccessGranted = PermissionUtils.isNotificationListenerEnabled(context),
        batteryOptimizationIgnored = PermissionUtils.isIgnoringBatteryOptimizations(context)
    )
}

data class MainUiState(
    val islandState: IslandState = IslandState(),
    val permissionStatus: PermissionStatus = PermissionStatus()
) {
    val setupComplete: Boolean = permissionStatus.overlayGranted && permissionStatus.notificationAccessGranted
}
