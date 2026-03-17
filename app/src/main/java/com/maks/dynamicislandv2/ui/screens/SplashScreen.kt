package com.maks.dynamicislandv2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun SplashScreen(onContinue: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Dynamic Island v2", style = MaterialTheme.typography.headlineMedium)
        Text("by Maks", style = MaterialTheme.typography.bodyMedium)
        Button(onClick = onContinue) { Text("Launch") }
    }
}
