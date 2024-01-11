package fr.uha.aumard.deckbuilder.ui.deck

import android.net.Uri
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.aumard.android.ui.AppMenu
import fr.uha.aumard.android.ui.AppMenuEntry
import fr.uha.aumard.android.ui.AppTitle
import fr.uha.aumard.android.ui.SwipeableItem
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Deck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListDecksScreen(
    vm : ListDecksViewModel = hiltViewModel(),
    onCreate : () -> Unit,
    onEdit : (p : Deck) -> Unit,
) {
    val decks = vm.decks.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = {vm.feed() } ),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(pageTitleId = R.string.deck_list)
                },
                actions = { AppMenu(entries = menuEntries) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items(
                items = decks.value,
                key = { deck -> deck.did }
            ) { deck ->
                SwipeableItem(
                    onEdit = { onEdit(deck) },
                    onDelete = { vm.deleteDeckById(deck.did) },
                ) {
                    val firstCardImageUri by vm.getFirstCardImageUri(deck.did)
                        .collectAsState(initial = null)
                        DeckItem(deck, firstCardImageUri)
                }
            }
        }
    }

}

@Composable
fun DeckItem(deck: Deck, imageUri: Uri?) {
    ListItem(
        headlineContent = {
            Text(deck.name)
        },
        supportingContent = {
            Row() {
                Icon(imageVector = Icons.Outlined.DateRange, contentDescription = null)
                Text(
                    UIConverter.convert(deck.creationDate),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 4.dp)
                )
            }
        },
        leadingContent = {
            if (imageUri != null) {
                AsyncImage(
                    model = imageUri,
                    contentDescription = "First card image",
                    modifier = Modifier.size(64.dp)
                )
            }
        }
    )
}
