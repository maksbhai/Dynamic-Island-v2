package com.maks.dynamicislandv2.services

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

object OverlayServiceTracker {
    private val _running = MutableStateFlow(false)
    val running: StateFlow<Boolean> = _running

    fun setRunning(running: Boolean) {
        _running.value = running
    }
}
