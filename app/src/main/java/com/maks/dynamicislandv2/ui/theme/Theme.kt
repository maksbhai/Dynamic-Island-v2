package com.maks.dynamicislandv2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkScheme = darkColorScheme(
    primary = Color(0xFF7E8BFF),
    surface = Color(0xFF0F0F10),
    background = Color(0xFF090909)
)

private val LightScheme = lightColorScheme(
    primary = Color(0xFF3D4AD8),
    surface = Color(0xFFF3F3F3),
    background = Color(0xFFFFFFFF)
)

@Composable
fun DynamicIslandV2Theme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkScheme else LightScheme,
        content = content
    )
}
