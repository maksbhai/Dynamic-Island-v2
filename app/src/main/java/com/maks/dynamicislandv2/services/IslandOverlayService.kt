package com.maks.dynamicislandv2.services

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.IBinder
import androidx.core.app.NotificationCompat
import com.maks.dynamicislandv2.MainActivity
import com.maks.dynamicislandv2.R
import com.maks.dynamicislandv2.data.SettingsDataStore
import com.maks.dynamicislandv2.domain.IslandMode
import com.maks.dynamicislandv2.domain.IslandState
import com.maks.dynamicislandv2.notifications.NotificationBridge
import com.maks.dynamicislandv2.overlay.IslandOverlayController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch

class IslandOverlayService : Service() {

    private val scope = CoroutineScope(Dispatchers.Main + Job())
    private lateinit var controller: IslandOverlayController
    private var currentState = IslandState()

    override fun onCreate() {
        super.onCreate()
        controller = IslandOverlayController(this)
        startForeground(41, foregroundNotification())
        OverlayServiceTracker.setRunning(true)

        SettingsDataStore(this).settingsFlow.onEach { persisted ->
            currentState = currentState.copy(
                enabled = persisted.enabled,
                widthDp = persisted.widthDp,
                heightDp = persisted.heightDp,
                topOffsetDp = persisted.topOffsetDp,
                hideSensitive = persisted.hideSensitive,
                autoCollapseMs = persisted.autoCollapseMs
            )
            render()
        }.launchIn(scope)

        NotificationBridge.events.onEach { event ->
            showNotificationState(event.appName, event.title, event.text)
        }.launchIn(scope)

        render()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        when (intent?.action) {
            ACTION_SHOW_TEST -> {
                currentState = currentState.copy(
                    enabled = true,
                    mode = IslandMode.TEST,
                    title = "Dynamic Island",
                    content = "Test island is now visible"
                )
                render()
            }

            ACTION_RESET_POSITION -> {
                currentState = IslandState(enabled = true)
                render()
            }

            ACTION_COLLAPSE_IDLE -> {
                currentState = currentState.copy(mode = IslandMode.IDLE, title = "Dynamic Island", content = "Ready")
                render()
            }
        }
        return START_STICKY
    }

    override fun onDestroy() {
        controller.hide()
        OverlayServiceTracker.setRunning(false)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun render() {
        if (!currentState.enabled) {
            controller.hide()
            return
        }
        controller.show(
            state = currentState,
            onTap = { currentState = currentState.copy(mode = IslandMode.IDLE); render() },
            onLongPress = { currentState = currentState.copy(mode = IslandMode.EXPANDED); render() }
        )
    }

    private fun showNotificationState(appName: String, title: String, text: String) {
        currentState = currentState.copy(
            mode = IslandMode.NOTIFICATION,
            title = appName.ifBlank { title.ifBlank { "Notification" } },
            content = if (currentState.hideSensitive) "Content hidden" else text.ifBlank { "New activity" }
        )
        render()
        scope.launch {
            delay(currentState.autoCollapseMs)
            currentState = currentState.copy(mode = IslandMode.IDLE, title = "Dynamic Island", content = "Ready")
            render()
        }
    }

    private fun foregroundNotification(): Notification {
        val manager = getSystemService(NotificationManager::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            manager.createNotificationChannel(
                NotificationChannel(CHANNEL_ID, "Dynamic Island Overlay", NotificationManager.IMPORTANCE_LOW)
            )
        }
        val intent = Intent(this, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            this,
            0,
            intent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )
        return NotificationCompat.Builder(this, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle("Dynamic Island v2")
            .setContentText("Overlay is active")
            .setContentIntent(pendingIntent)
            .setOngoing(true)
            .build()
    }

    companion object {
        const val ACTION_SHOW_TEST = "com.maks.dynamicislandv2.action.SHOW_TEST"
        const val ACTION_RESET_POSITION = "com.maks.dynamicislandv2.action.RESET_POSITION"
        const val ACTION_COLLAPSE_IDLE = "com.maks.dynamicislandv2.action.COLLAPSE_IDLE"
        private const val CHANNEL_ID = "overlay_channel"
    }
}
