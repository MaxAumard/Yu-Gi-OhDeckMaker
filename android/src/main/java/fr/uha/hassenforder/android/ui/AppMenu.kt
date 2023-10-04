package fr.uha.hassenforder.android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
//import androidx.compose.material.ContentAlpha
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource

sealed interface AppMenuEntry {
    data class OverflowEntry (
        @StringRes val title: Int,
        val enabled: Boolean = true,
        val listener : () -> Unit
    ) : AppMenuEntry
    data class ActionEntry (
        @StringRes val title: Int,
        val icon: ImageVector,
        val enabled: Boolean = true,
        val listener : () -> Unit
    ) : AppMenuEntry
}

@Composable
fun AppMenu (entries : List<AppMenuEntry>) {
    var showMenu by remember { mutableStateOf(false) }
    Row (modifier = Modifier){
        entries.filterIsInstance<AppMenuEntry.ActionEntry>().forEach { entry ->
            ActionMenuItem(entry.title, entry.icon, entry.enabled) { entry.listener() }
        }
        if (entries.filterIsInstance<AppMenuEntry.OverflowEntry>().isNotEmpty()) {
            ActionMenuItem("more", Icons.Filled.MoreVert, true) { showMenu = !showMenu }
            DropdownMenu(
                expanded = showMenu,
                onDismissRequest = { showMenu = false }
            ) {
                entries.filterIsInstance<AppMenuEntry.OverflowEntry>().forEach { entry ->
                    OverflowMenuItem(entry.title) { showMenu = false; entry.listener() }
                }
            }
        }
    }
}

@Composable
private fun OverflowMenuItem (
    @StringRes title : Int,
    listener : () -> Unit
) {
    DropdownMenuItem(
        text = { Text(text = stringResource(id = title), color = Color.White) },
        onClick = listener
    )
}

@Composable
private fun ActionMenuItem (
    @StringRes title : Int,
    icon : ImageVector,
    enabled : Boolean = true,
    listener : () -> Unit
) {
    IconButton(onClick = listener, enabled = enabled) {
        Icon(
            imageVector = icon,
            contentDescription = stringResource(id = title),
//            tint = if(enabled) Color.White else Color.White.copy(ContentAlpha.disabled)
        )
    }
}

@Composable
private fun ActionMenuItem (
    title : String,
    icon : ImageVector,
    enabled : Boolean = true,
    listener : () -> Unit
) {
    IconButton(onClick = listener, enabled = enabled) {
        Icon(
            imageVector = icon,
            contentDescription = title,
//            tint = if(enabled) Color.White else Color.White.copy(ContentAlpha.disabled)
        )
    }
}