package fr.uha.hassenforder.android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

@Composable
fun AppTitle (
    @StringRes appNameId: Int? = null,
    color: Color = Color.White,
    @StringRes pageTitleId: Int? = null,
    isModified : Boolean = false
) {
    Row() {
        if (appNameId != null) {
            Text(stringResource(id = appNameId), color = color)
            Text("-", Modifier.padding(horizontal = 8.dp), color = color)
        }
        if (pageTitleId != null) {
            Text(stringResource(id = pageTitleId), color = color)
        }
        if (isModified) {
            Text(text = "*", Modifier.padding(horizontal = 8.dp), color = color)
        }
    }
}