package com.maks.dynamicislandv2.overlay

import android.content.Context
import android.graphics.PixelFormat
import android.os.Build
import android.view.Gravity
import android.view.WindowManager
import androidx.compose.ui.platform.ComposeView
import com.maks.dynamicislandv2.domain.IslandMode
import com.maks.dynamicislandv2.domain.IslandState
import com.maks.dynamicislandv2.ui.theme.DynamicIslandV2Theme

class IslandOverlayController(private val context: Context) {
    private val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
    private var composeView: ComposeView? = null
    private var params: WindowManager.LayoutParams? = null

    fun show(state: IslandState, onTap: () -> Unit, onLongPress: () -> Unit) {
        val clamped = clamp(state)
        val existing = composeView
        if (existing == null) {
            val view = ComposeView(context).apply {
                setContent {
                    DynamicIslandV2Theme {
                        IslandOverlayComposable(clamped, onTap, onLongPress)
                    }
                }
            }
            val type = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY
            } else {
                WindowManager.LayoutParams.TYPE_PHONE
            }
            val lp = WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                type,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE or WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN,
                PixelFormat.TRANSLUCENT
            ).apply {
                gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                y = dpToPx(clamped.topOffsetDp)
            }
            windowManager.addView(view, lp)
            composeView = view
            params = lp
        } else {
            existing.setContent {
                DynamicIslandV2Theme {
                    IslandOverlayComposable(clamped, onTap, onLongPress)
                }
            }
            params?.let {
                it.y = dpToPx(clamped.topOffsetDp)
                windowManager.updateViewLayout(existing, it)
            }
        }
    }

    fun hide() {
        composeView?.let { view ->
            runCatching { windowManager.removeView(view) }
        }
        composeView = null
        params = null
    }

    private fun clamp(state: IslandState): IslandState = state.copy(
        widthDp = state.widthDp.coerceIn(120f, 360f),
        heightDp = state.heightDp.coerceIn(34f, 140f),
        topOffsetDp = state.topOffsetDp.coerceIn(12f, 180f),
        mode = if (state.enabled) state.mode else IslandMode.IDLE
    )

    private fun dpToPx(dp: Float): Int = (dp * context.resources.displayMetrics.density).toInt()
}
