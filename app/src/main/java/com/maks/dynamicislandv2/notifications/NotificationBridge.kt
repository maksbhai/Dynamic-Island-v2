package com.maks.dynamicislandv2.notifications

import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow

object NotificationBridge {
    private val _events = MutableSharedFlow<IslandNotificationEvent>(extraBufferCapacity = 8)
    val events: SharedFlow<IslandNotificationEvent> = _events

    fun post(event: IslandNotificationEvent) {
        _events.tryEmit(event)
    }
}

data class IslandNotificationEvent(
    val appName: String,
    val title: String,
    val text: String
)
