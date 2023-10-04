package fr.uha.hassenforder.android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.selection.selectable
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp

class RadioButtonEntry (
    val label : String? = null,
    val icon: ImageVector? = null,
    val iconFocused: ImageVector? = null,
    val value : String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T : Enum<T>> OutlinedEnumRadioGroup(
    value : T? = null,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    items : Array<T>,
    labelId : Int?,
    errorId : Int? = null,
    itemLabels : Array<String>? = null,
    itemIcons : Array<ImageVector>? = null,
    itemIconsFocused : Array<ImageVector>? = null
) {
    val entries : MutableList<RadioButtonEntry> = mutableListOf()
    items.forEach {
        item -> entries.add(
            RadioButtonEntry(
                label = if (itemLabels != null) itemLabels[item.ordinal] else null,
                icon = if (itemIcons != null) itemIcons[item.ordinal] else null,
                iconFocused = if (itemIconsFocused != null) itemIconsFocused[item.ordinal] else null,
                value = item.name
            )
        )
    }
    OutlinedRadioGroup (
        onChanged = onValueChange,
        modifier = modifier,
        selected = value?.name,
        errorId = errorId,
        labelId = labelId,
        entries = entries,
    )
}

@Composable
fun OutlinedRadioGroup (
    entries : List<RadioButtonEntry>,
    onChanged: (String) -> Unit,
    modifier : Modifier = Modifier,
    selected: String? = null,
    @StringRes errorId : Int? = null,
    @StringRes labelId: Int? = null,
) {
    Column (
        modifier = modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .border(BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground), MaterialTheme.shapes.extraSmall)
            .padding(start = 16.dp),
    ) {
        val color = if (errorId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        if (labelId != null) {
            Text(text = stringResource(id = labelId), color = color)
        }
        Row(modifier = Modifier.padding(top = 4.dp)) {
            entries.forEach { entry -> OutlinedRadioButton(onChanged, selected, entry) }
        }
        if (errorId != null){
            Text(
                text = stringResource(id = errorId),
                color = color,
            )
        }
    }
}

@Composable
private fun RowScope.OutlinedRadioButton (
    onSelectedChanged: (String) -> Unit,
    selectedValue: String? = null,
    entry : RadioButtonEntry,
) {
    val selected = entry.value == selectedValue
    val background =
        if (entry.icon == null && entry.iconFocused != null && selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent
    val contentColor =
        if (entry.icon == null && entry.iconFocused != null && selected) Color.White else MaterialTheme.colorScheme.onBackground

    Row (
        modifier = Modifier
            .selectable(
                selected = selected,
                onClick = { onSelectedChanged(entry.value) }
            )
            .background(background),
        verticalAlignment = Alignment.CenterVertically
    ) {
        when {
            entry.icon != null && entry.iconFocused != null -> {
                IconToggleButton(
                    checked = selected,
                    onCheckedChange = { onSelectedChanged(entry.value) }
                ) {
                    Icon(
                        painter = rememberVectorPainter(
                            if (selected) entry.iconFocused else entry.icon
                        ),
                        contentDescription = null,
                        tint = contentColor,
                        modifier = Modifier.size(48.dp)
                    )
                }
            }
            entry.icon != null && entry.iconFocused == null -> {
                RadioButton(
                    selected = selected,
                    onClick = { onSelectedChanged(entry.value) }
                )
                Icon(
                    painter = rememberVectorPainter(entry.icon),
                    contentDescription = null,
                    tint = contentColor
                )
            }
            entry.icon == null && entry.iconFocused != null -> {
                Icon(
                    painter = rememberVectorPainter(entry.iconFocused),
                    contentDescription = null,
                    tint = contentColor,
                    modifier = Modifier.size(48.dp)
                )
            }
            entry.icon == null && entry.iconFocused == null -> {
                RadioButton(
                    selected = selected,
                    onClick = { onSelectedChanged(entry.value) }
                )
            }
        }
        Text(
            text = entry.label ?: entry.value,
            color = contentColor,
            modifier = Modifier.padding(horizontal = 4.dp)
        )
    }

}