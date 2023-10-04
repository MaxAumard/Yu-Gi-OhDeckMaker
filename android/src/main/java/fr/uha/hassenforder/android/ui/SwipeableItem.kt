package fr.uha.hassenforder.android.ui

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.DismissDirection
import androidx.compose.material3.DismissValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SwipeToDismiss
import androidx.compose.material3.rememberDismissState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SwipeableItem(
    onEdit: (() -> Unit)? = null,
    onDelete: (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    content: @Composable() (RowScope.() -> Unit)
) {
    val dismissState = rememberDismissState(
        confirmValueChange = {
            when (it) {
                DismissValue.DismissedToEnd -> {
                    if (onEdit != null) {
                        onEdit()
                        true
                    }
                    false
                }
                DismissValue.DismissedToStart -> {
                    if (onDelete != null) {
                        onDelete()
                        true
                    }
                    false
                }
                DismissValue.Default -> {
                    false
                }
            }
        }
    )
    val directions: Set<DismissDirection> = when {
        onEdit != null && onDelete != null -> setOf(DismissDirection.StartToEnd, DismissDirection.EndToStart)
        onEdit != null && onDelete == null -> setOf(DismissDirection.StartToEnd)
        onEdit == null && onDelete != null -> setOf(DismissDirection.EndToStart)
        else -> setOf()
    }
    SwipeToDismiss(
        state = dismissState,
        modifier = modifier,
        directions = directions,
        background = {
            val direction = dismissState.dismissDirection ?: return@SwipeToDismiss
            if (onEdit == null && direction == DismissDirection.StartToEnd) return@SwipeToDismiss
            if (onDelete == null && direction == DismissDirection.EndToStart) return@SwipeToDismiss
            val color by animateColorAsState(
                targetValue = when (dismissState.targetValue) {
                    DismissValue.Default -> Color.LightGray
                    DismissValue.DismissedToStart -> Color.Red
                    DismissValue.DismissedToEnd -> Color.Green
                }
            )
            val icon = when (direction) {
                DismissDirection.StartToEnd-> Icons.Default.Edit
                DismissDirection.EndToStart -> Icons.Default.Delete
            }
            val scale = animateFloatAsState(
                targetValue = if (dismissState.targetValue == DismissValue.Default) 0.5f else 1.2f
            )
            val alignment = when (direction) {
                DismissDirection.StartToEnd -> Alignment.CenterStart
                DismissDirection.EndToStart -> Alignment.CenterEnd
            }
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .background(color)
                    .padding(start = 12.dp, end = 12.dp),
                contentAlignment = alignment
            ) {
                Icon (icon, contentDescription = "icon", modifier = Modifier.scale(scale.value))
            }
        },
        dismissContent = content
    )
}
