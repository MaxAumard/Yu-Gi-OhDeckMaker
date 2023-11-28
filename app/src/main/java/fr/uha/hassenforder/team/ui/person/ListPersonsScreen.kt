package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.FilterNone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.ChipDefaults
import coil.compose.AsyncImage
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Card
import fr.uha.hassenforder.team.model.CardWithDetails
import fr.uha.hassenforder.team.model.Type

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPersonsScreen(
    vm: ListPersonsViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onEdit: (p: Card) -> Unit,
) {
    val persons = vm.persons.collectAsStateWithLifecycle(initialValue = emptyList())

    var selectedFilter by remember { mutableStateOf(Type.ALL) }

    val filteredPersons = when (selectedFilter) {
        Type.ALL -> persons.value
        else -> persons.value.filter { it.card.type == selectedFilter }
    }


    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = { vm.feed() }),
        AppMenuEntry.OverflowEntry(title = R.string.clean, listener = { vm.clean() })
    )

    Scaffold(topBar = {
        TopAppBar(title = {
            AppTitle(pageTitleId = R.string.card_list)
        }, actions = { AppMenu(entries = menuEntries) })
    }, floatingActionButton = {
        FloatingActionButton(onClick = onCreate) {
            Icon(imageVector = Icons.Filled.Add, contentDescription = null)
        }
    }) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == Type.ALL,
                onClick = { selectedFilter = Type.ALL },
                label = { Text("All") },
                enabled = true,
                leadingIcon = {
                    Icon(
                        imageVector = Icons.Outlined.FilterNone,
                        contentDescription = null
                    )
                },
                border = null,
                interactionSource = remember { MutableInteractionSource() }
            )
        }
        LazyVerticalStaggeredGrid(
                columns = StaggeredGridCells.Fixed(3),
                modifier = Modifier.padding(innerPadding)
            ) {
                items(persons.value) { item ->
                    SwipeableItem(
                        onEdit = { onEdit(item.card) },
                        onDelete = { vm.delete(item.card) }) {
                        CardItem(item)
                    }
                }
            }
    }
}

@Composable
fun CardItem(card: CardWithDetails) {

    if (card.card.picture != null) {
        ListItem(
            headlineContent = {
                AsyncImage(
                    model = card.card.picture,
                    contentScale = ContentScale.Crop,
                    contentDescription = "Selected image",
                    error = rememberVectorPainter(Icons.Outlined.Error),
                    placeholder = rememberVectorPainter(Icons.Outlined.Casino)
                )
            },
        )
    } else {
        ListItem(headlineContent = {
            Text(card.card.name, modifier = Modifier.padding(end = 4.dp))
        })
    }
}



