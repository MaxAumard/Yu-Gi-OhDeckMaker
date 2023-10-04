package fr.uha.hassenforder.android.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import fr.uha.hassenforder.android.R
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedSpinnerField (
    value : String?,
    onValueChange : (String) -> Unit,
    modifier : Modifier = Modifier,
    label : Int,
    option_views : Array<String>,
    option_values : Array<String>,
    errorId : Int?
)
{
    val options = option_views zip option_values
    val selected = options.firstNotNullOfOrNull { pair -> pair.takeIf() { pair.second == value } }
    var selectedOption by remember { mutableStateOf(selected?: options.first()) }
    var expanded by remember { mutableStateOf(false) }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = modifier
    ) {
        OutlinedTextField(
            value = selectedOption.first,
            readOnly = true,
            onValueChange = { },
            modifier = modifier.fillMaxWidth().menuAnchor(),
            label = { Text (text = stringResource(label)) },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            supportingText = { if (errorId != null) Text(stringResource(id = errorId)) },
            isError = errorId != null,
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.first) },
                    onClick = {
                        selectedOption = option
                        expanded = false
                        onValueChange(option.second)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun OutlinedSpinnerFieldPreview() {
    OutlinedSpinnerField (
        value = "1",
        onValueChange = { },
        label = R.string.duration,
        option_views = arrayOf("0", "1", "3", "5"),
        option_values = arrayOf("0", "1", "3", "5"),
        errorId = null
    )
}