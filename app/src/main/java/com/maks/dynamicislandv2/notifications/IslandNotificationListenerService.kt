package com.maks.dynamicislandv2.notifications

import android.service.notification.NotificationListenerService
import android.service.notification.StatusBarNotification

class IslandNotificationListenerService : NotificationListenerService() {

    override fun onNotificationPosted(sbn: StatusBarNotification?) {
        if (sbn == null) return
        val extras = sbn.notification.extras
        val title = extras.getCharSequence("android.title")?.toString().orEmpty()
        val text = extras.getCharSequence("android.text")?.toString().orEmpty()
        val appName = packageManager.getApplicationLabel(
            packageManager.getApplicationInfo(sbn.packageName, 0)
        ).toString()

        if (title.isBlank() && text.isBlank()) return
        NotificationBridge.post(
            IslandNotificationEvent(appName = appName, title = title, text = text)
        )
    }
}
