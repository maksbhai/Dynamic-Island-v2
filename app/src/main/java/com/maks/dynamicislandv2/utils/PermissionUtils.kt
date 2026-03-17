package com.maks.dynamicislandv2.utils

import android.content.ComponentName
import android.content.Context
import android.os.PowerManager
import android.provider.Settings

object PermissionUtils {
    fun canDrawOverlays(context: Context): Boolean = Settings.canDrawOverlays(context)

    fun isNotificationListenerEnabled(context: Context): Boolean {
        val enabled = Settings.Secure.getString(
            context.contentResolver,
            "enabled_notification_listeners"
        ) ?: return false
        val expected = ComponentName(context, com.maks.dynamicislandv2.notifications.IslandNotificationListenerService::class.java)
            .flattenToString()
        return enabled.split(':').any { it.equals(expected, ignoreCase = true) }
    }

    fun isIgnoringBatteryOptimizations(context: Context): Boolean {
        val powerManager = context.getSystemService(Context.POWER_SERVICE) as PowerManager
        return powerManager.isIgnoringBatteryOptimizations(context.packageName)
    }
}
