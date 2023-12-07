package fr.uha.aumard.deckbuilder.ui.card

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.aumard.android.ui.AppMenu
import fr.uha.aumard.android.ui.AppMenuEntry
import fr.uha.aumard.android.ui.AppTitle
import fr.uha.aumard.android.ui.SwipeableItem
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Type

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPersonsScreen(
    vm: ListCardsViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onCardClick: (p: Card) -> Unit,
) {
    val cards by vm.cards.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf(Type.ALL) }
    val filteredCards = when (selectedFilter) {
        Type.ALL -> cards
        else -> cards.filter { it.type == selectedFilter }
    }

    // Add this line to create a state for the search query
    var searchQuery by remember { mutableStateOf("") }

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
        Column {
            // Add this TextField as the search bar
            TextField(
                value = searchQuery,
                onValueChange = { newValue ->
                    searchQuery = newValue
                    vm.loadMoreCards(searchQuery) // Call loadMoreCards with the new search query
                },
                label = { Text("Search") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            )
        }
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(3),
            modifier = Modifier.padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(4.dp), // Horizontal spacing between items
        ) {
            items(filteredCards) { card ->
                SwipeableItem(
                    onDelete = { vm.delete(card) }) {
                    CardItem(card, onCardClick)
                }
            }

            item {
                LoadMoreIndicator(onLoadMore = { vm.loadMoreCards(searchQuery) })
            }
        }
    }
}

@Composable
fun LoadMoreIndicator(onLoadMore: () -> Unit) {
    Text("Loading cards...")
    onLoadMore()
}

@Composable
fun CardItem(card: Card, onCardClick: (p: Card) -> Unit) {
    if (card.picture != null) {
        AsyncImage(
            model = card.picture,
            contentDescription = "Selected image",
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
                .clickable { onCardClick(card) }, // Making the image clickable,
            contentScale = ContentScale.FillWidth,
            error = rememberVectorPainter(Icons.Outlined.Error),
            placeholder = rememberVectorPainter(Icons.Outlined.Casino)
        )
    } else {
        Text(
            text = card.name,
            modifier = Modifier
                .padding(4.dp)
                .fillMaxWidth()
        )
    }
}
