package fr.uha.hassenforder.team.ui.team

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListMembersField(
    value : List<Person>?,
    modifier : Modifier = Modifier,
    @StringRes label: Int? = null,
    onAdd: (pid : Long) -> Unit,
    onDelete: (person : Person) -> Unit,
    errorId : Int?,
) {
    val showDialog = remember { mutableStateOf(false) }

    if (showDialog.value) {
        PersonPicker(
            title = R.string.member_select,
            onSelect = { showDialog.value = false; if (it != null) onAdd(it.pid) }
        )
    }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showDialog.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
            Column(
                modifier = modifier
                    .padding(innerPadding)
                    .padding(top = 4.dp, bottom = 4.dp)
                    .fillMaxWidth()
                    .border(BorderStroke(1.dp, Color.Black), MaterialTheme.shapes.extraSmall)
                    .padding(start = 16.dp)
            ) {
                val color =
                    if (errorId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
                if (label != null) {
                    Text(
                        text = stringResource(id = label),
                        modifier = Modifier.fillMaxWidth(),
                        color = color
                    )
                }
                LazyColumn(
                    modifier = Modifier.weight(1.0f).fillMaxWidth()
                ) {
                    items(
                        items = value?: listOf(),
                        key = { person : Person -> person.pid }
                    ) { item : Person ->
                        Divider(color = MaterialTheme.colorScheme.onBackground)
                        SwipeableItem(
                            onDelete = { onDelete(item) }
                        ) {
                            TeamPersonItem(item)
                        }
                    }
                }
                if (errorId != null){
                    Text(
                        text = stringResource(id = errorId),
                        modifier = Modifier.fillMaxWidth(),
                        color = color,
                    )
                }
            }
    }
}
