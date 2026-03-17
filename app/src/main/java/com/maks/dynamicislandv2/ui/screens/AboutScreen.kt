package com.maks.dynamicislandv2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun AboutScreen(onBack: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Dynamic Island v2")
        Text("Developer: Maks")
        Text("Focused Android overlay app that displays an iPhone-style dynamic island at the top center and reacts to notifications.")
        Text("This app cannot replace all system notifications globally due to Android platform restrictions.")
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
