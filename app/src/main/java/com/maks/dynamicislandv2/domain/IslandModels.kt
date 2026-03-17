package com.maks.dynamicislandv2.domain

enum class IslandMode {
    IDLE,
    TEST,
    NOTIFICATION,
    EXPANDED,
    MEDIA
}

data class IslandState(
    val enabled: Boolean = true,
    val widthDp: Float = 160f,
    val heightDp: Float = 44f,
    val topOffsetDp: Float = 28f,
    val hideSensitive: Boolean = false,
    val autoCollapseMs: Long = 3500L,
    val mode: IslandMode = IslandMode.IDLE,
    val title: String = "Dynamic Island",
    val content: String = "Ready"
)

data class PermissionStatus(
    val overlayGranted: Boolean = false,
    val notificationAccessGranted: Boolean = false,
    val batteryOptimizationIgnored: Boolean = false,
    val overlayServiceRunning: Boolean = false
)
