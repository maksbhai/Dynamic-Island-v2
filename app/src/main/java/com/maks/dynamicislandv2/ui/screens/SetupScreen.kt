package com.maks.dynamicislandv2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maks.dynamicislandv2.domain.PermissionStatus

@Composable
fun SetupScreen(
    status: PermissionStatus,
    onRefresh: () -> Unit,
    onGrantOverlay: () -> Unit,
    onGrantNotification: () -> Unit,
    onGrantBattery: () -> Unit,
    onContinue: () -> Unit
) {
    LazyColumn(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item { Text("First-launch setup", style = MaterialTheme.typography.headlineSmall) }
        item {
            PermissionCard(
                title = "Overlay permission",
                granted = status.overlayGranted,
                description = "Required for top-center Dynamic Island window.",
                action = onGrantOverlay
            )
        }
        item {
            PermissionCard(
                title = "Notification access",
                granted = status.notificationAccessGranted,
                description = "Lets Dynamic Island react to incoming notifications.",
                action = onGrantNotification
            )
        }
        item {
            PermissionCard(
                title = "Battery optimization",
                granted = status.batteryOptimizationIgnored,
                description = "Recommended so the overlay service stays alive in background.",
                action = onGrantBattery
            )
        }
        item {
            Button(onClick = onRefresh, modifier = Modifier.fillMaxWidth()) { Text("Refresh Status") }
        }
        item {
            Button(onClick = onContinue, modifier = Modifier.fillMaxWidth()) { Text("Continue") }
        }
    }
}

@Composable
private fun PermissionCard(title: String, description: String, granted: Boolean, action: () -> Unit) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text(title, style = MaterialTheme.typography.titleMedium)
            Text(description, style = MaterialTheme.typography.bodySmall)
            Text(if (granted) "Status: Granted" else "Status: Missing")
            Button(onClick = action) {
                Text(if (granted) "Open setting" else "Grant")
            }
        }
    }
}
