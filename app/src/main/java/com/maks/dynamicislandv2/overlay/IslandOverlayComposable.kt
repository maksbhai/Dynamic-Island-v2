package com.maks.dynamicislandv2.overlay

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.maks.dynamicislandv2.domain.IslandMode
import com.maks.dynamicislandv2.domain.IslandState

@Composable
fun IslandOverlayComposable(
    state: IslandState,
    onTap: () -> Unit,
    onLongPress: () -> Unit
) {
    val width = if (state.mode == IslandMode.EXPANDED || state.mode == IslandMode.NOTIFICATION) state.widthDp + 100f else state.widthDp
    val height = if (state.mode == IslandMode.EXPANDED) state.heightDp + 72f else state.heightDp
    Row(
        modifier = Modifier
            .width(width.dp)
            .height(height.dp)
            .shadow(14.dp, RoundedCornerShape(999.dp), clip = false)
            .background(Color.Black.copy(alpha = 0.95f), RoundedCornerShape(999.dp))
            .pointerInput(state.mode) {
                detectTapGestures(
                    onTap = { onTap() },
                    onLongPress = { onLongPress() }
                )
            }
            .padding(horizontal = 14.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Center
    ) {
        AnimatedContent(
            targetState = state.mode,
            transitionSpec = { fadeIn() togetherWith fadeOut() },
            label = "islandMode"
        ) { mode ->
            when (mode) {
                IslandMode.IDLE -> Text("•", color = Color.White)
                IslandMode.TEST -> Text("Test Island Visible", color = Color.White, style = MaterialTheme.typography.labelLarge)
                IslandMode.MEDIA, IslandMode.NOTIFICATION, IslandMode.EXPANDED -> {
                    Column(horizontalAlignment = Alignment.Start) {
                        Text(
                            text = state.title,
                            color = Color.White,
                            style = MaterialTheme.typography.labelLarge,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = state.content,
                            color = Color(0xFFDDDDDD),
                            style = MaterialTheme.typography.bodySmall,
                            maxLines = if (mode == IslandMode.EXPANDED) 3 else 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        }
    }
}
