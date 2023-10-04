package fr.uha.hassenforder.android.ui

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun LoadingScreen(
    text : String
) {
    Text(text = text, modifier = Modifier.padding(vertical = 16.dp))
    CircularProgressIndicator()
}

@Composable
fun ErrorScreen(
    text : String
) {
    Text(text = text, modifier = Modifier.padding(vertical = 16.dp), color = MaterialTheme.colorScheme.error)
}
