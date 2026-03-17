package com.maks.dynamicislandv2.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.maks.dynamicislandv2.viewmodel.MainUiState

@Composable
fun MainScreen(
    uiState: MainUiState,
    onToggleEnabled: (Boolean) -> Unit,
    onShowTestIsland: () -> Unit,
    onReset: () -> Unit,
    onStartService: () -> Unit,
    onPermissions: () -> Unit,
    onSettings: () -> Unit,
    onAbout: () -> Unit,
    onRefresh: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text("Dynamic Island v2", style = MaterialTheme.typography.headlineSmall)
        Card {
            Column(modifier = Modifier.padding(16.dp), verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text("Preview")
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(70.dp),
                    horizontalArrangement = Arrangement.Center
                ) {
                    Row(
                        modifier = Modifier
                            .background(Color.Black, RoundedCornerShape(999.dp))
                            .padding(horizontal = 24.dp, vertical = 10.dp)
                    ) { Text("Dynamic Island", color = Color.White) }
                }
            }
        }

        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text("Enable Island")
            Switch(checked = uiState.islandState.enabled, onCheckedChange = onToggleEnabled)
        }

        Button(onClick = onStartService, modifier = Modifier.fillMaxWidth()) { Text("Start Overlay Service") }
        Button(onClick = onShowTestIsland, modifier = Modifier.fillMaxWidth()) { Text("Show Test Island") }
        Button(onClick = onReset, modifier = Modifier.fillMaxWidth()) { Text("Reset Position") }

        Card {
            Column(modifier = Modifier.padding(14.dp), verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text("Diagnostics", style = MaterialTheme.typography.titleMedium)
                Text("Overlay permission granted: ${uiState.permissionStatus.overlayGranted}")
                Text("Notification access granted: ${uiState.permissionStatus.notificationAccessGranted}")
                Text("Battery optimization ignored: ${uiState.permissionStatus.batteryOptimizationIgnored}")
                Text("Overlay service running: ${uiState.permissionStatus.overlayServiceRunning}")
                Text("Island enabled: ${uiState.islandState.enabled}")
            }
        }

        Button(onClick = onRefresh, modifier = Modifier.fillMaxWidth()) { Text("Refresh Diagnostics") }
        Button(onClick = onPermissions, modifier = Modifier.fillMaxWidth()) { Text("Permissions / Setup") }
        Button(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Settings") }
        Button(onClick = onAbout, modifier = Modifier.fillMaxWidth()) { Text("About") }
    }
}
