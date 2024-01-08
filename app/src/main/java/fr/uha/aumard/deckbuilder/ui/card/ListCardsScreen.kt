package fr.uha.aumard.deckbuilder.ui.card

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import fr.uha.aumard.android.ui.AppTitle
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Type

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListCardsScreen(
    vm: ListCardsViewModel = hiltViewModel(),
    onCreate: () -> Unit = {},
    onCardClick: (Card) -> Unit = {},
    isPickerMode: Boolean = false,
    onCardPicked: (Card) -> Unit = {}
) {
    val cards by vm.cards.collectAsStateWithLifecycle()
    var selectedFilter by remember { mutableStateOf(Type.ALL) }
    var searchQuery by remember { mutableStateOf("") }
    var isSearchVisible by remember { mutableStateOf(false) }

    val filteredCards = when (selectedFilter) {
        Type.ALL -> cards
        else -> cards.filter { it.type == selectedFilter }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { if (!isSearchVisible) AppTitle(pageTitleId = R.string.card_list) },
                actions = {
                    if (isSearchVisible) {
                        SearchBar(searchQuery) { query ->
                            searchQuery = query
                            vm.updateSearchQuery(query)
                        }
                    } else {
                        IconButton(onClick = { isSearchVisible = true }) {
                            Icon(Icons.Default.Search, contentDescription = "Search")
                        }
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyVerticalStaggeredGrid(
            columns = StaggeredGridCells.Fixed(3),
            modifier = Modifier.padding(innerPadding),
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            items(filteredCards) { card ->
                if (isPickerMode) {
                    Log.d("TEST","Card selected : $card")
                    CardItem(
                        card,
                        onCardClick = { onCardPicked(card) })
                } else {
                    CardItem(card, onCardClick)
                }
            }
            item {
                LoadMoreIndicator { vm.loadMoreCards() }
            }

            items(filteredCards) { card ->
                Log.d("TEST","Card added : $card")

                if (isPickerMode) {
                    Log.d("TEST","Card selected : $card")
                    CardItem(
                        card,
                        onCardClick = { onCardPicked(card) })
                } else {
                    CardItem(card, onCardClick)
                }
            }
        }
    }
}

@Composable
fun SearchBar(searchQuery: String, onSearchChanged: (String) -> Unit) {
    TextField(
        value = searchQuery,
        onValueChange = onSearchChanged,
        singleLine = true,
        placeholder = { Text("Search cards") },
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun LoadMoreIndicator(onLoadMore: () -> Unit) {
    Text("Loading cards...")
    onLoadMore()
}

@Composable
fun CardItem(card: Card, onCardClick: (c: Card) -> Unit) {
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
