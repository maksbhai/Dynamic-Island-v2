package com.maks.dynamicislandv2.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.maks.dynamicislandv2.domain.IslandState

@Composable
fun SettingsScreen(
    state: IslandState,
    onSetSize: (Float, Float, Float) -> Unit,
    onHideSensitive: (Boolean) -> Unit,
    onAutoCollapse: (Long) -> Unit,
    onShowTestIsland: () -> Unit,
    onReset: () -> Unit,
    onBack: () -> Unit
) {
    var width by remember(state.widthDp) { mutableStateOf(state.widthDp.toString()) }
    var height by remember(state.heightDp) { mutableStateOf(state.heightDp.toString()) }
    var topOffset by remember(state.topOffsetDp) { mutableStateOf(state.topOffsetDp.toString()) }
    var collapse by remember(state.autoCollapseMs) { mutableStateOf(state.autoCollapseMs.toString()) }

    Column(
        modifier = Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Text("Essential settings")
        OutlinedTextField(value = width, onValueChange = { width = it }, label = { Text("Island width dp") })
        OutlinedTextField(value = height, onValueChange = { height = it }, label = { Text("Island height dp") })
        OutlinedTextField(value = topOffset, onValueChange = { topOffset = it }, label = { Text("Top offset dp") })
        OutlinedTextField(value = collapse, onValueChange = { collapse = it }, label = { Text("Auto-collapse ms") })

        androidx.compose.foundation.layout.Row(horizontalArrangement = Arrangement.SpaceBetween, modifier = Modifier.fillMaxWidth()) {
            Text("Hide sensitive content")
            Switch(checked = state.hideSensitive, onCheckedChange = onHideSensitive)
        }

        Button(onClick = {
            onSetSize(width.toFloatOrNull() ?: state.widthDp, height.toFloatOrNull() ?: state.heightDp, topOffset.toFloatOrNull() ?: state.topOffsetDp)
            onAutoCollapse(collapse.toLongOrNull() ?: state.autoCollapseMs)
        }, modifier = Modifier.fillMaxWidth()) { Text("Apply") }
        Button(onClick = onShowTestIsland, modifier = Modifier.fillMaxWidth()) { Text("Show Test Island") }
        Button(onClick = onReset, modifier = Modifier.fillMaxWidth()) { Text("Reset Island Position") }
        Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
    }
}
