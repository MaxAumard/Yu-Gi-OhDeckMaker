package fr.uha.hassenforder.android.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedIntField(
    value : Int?,
    onValueChange : (Int) -> Unit,
    modifier : Modifier = Modifier,
    label : Int,
    errorId : Int?
) {
    OutlinedTextField(
        value = value?.toString() ?: "",
        onValueChange = { try { val v : Int = it.toInt(); onValueChange(v) } catch (e:Exception) { } },
        modifier = modifier.fillMaxWidth(),
        label = { Text (text = stringResource(label)) },
        supportingText = { if (errorId != null) Text(stringResource(id = errorId)) },
        isError = errorId != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}

