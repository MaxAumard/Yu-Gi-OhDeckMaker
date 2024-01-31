package fr.uha.aumard.deckbuilder.ui.deck

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.aumard.android.ui.SwipeableItem
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.ui.card.ListCardsScreen

@Composable
fun ListCardsField(
    value: List<Card>?,
    modifier: Modifier = Modifier,
    onAdd: (Card) -> Unit,
    onDelete: (Card) -> Unit,
    errorId: Int?,
) {
    val showPicker = remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val showSnackbar = remember { mutableStateOf(false) }

    if (showPicker.value) {
        ListCardsScreen(
            isPickerMode = true,
            onCardPicked = { selectedCard ->
                val count = value?.count { it.cid == selectedCard.cid } ?: 0
                if (count < 3) {
                    onAdd(selectedCard)
                } else {
                    showSnackbar.value = true

                }
                showPicker.value = false
            }
        )
    }
    LaunchedEffect(showSnackbar.value) {
        if (showSnackbar.value) {
            snackbarHostState.showSnackbar(
                message = "You can only have up to 3 of the same card in a deck",
                duration = SnackbarDuration.Short
            )
            showSnackbar.value = false
        }
    }

    val mainDeckCards = value?.filter { !it.isExtraDeck } ?: listOf()
    val extraDeckCards = value?.filter { it.isExtraDeck } ?: listOf()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(onClick = { showPicker.value = true }) {
                Icon(Icons.Filled.Add, contentDescription = "add")
            }
        }
    ) { innerPadding ->
        LazyColumn(modifier = modifier.padding(innerPadding)) {
            item {
                Text("Main Deck (" + mainDeckCards.size + ")", modifier = Modifier.padding(16.dp))
            }
            itemsIndexed(
                mainDeckCards,
                key = { index, card -> card.cid.toString() + index }) { _, card ->
                SwipeableItem(onDelete = { onDelete(card) }) {
                    DeckCardItem(card)
                }
            }

            item {
                Text("Extra Deck (" + extraDeckCards.size + ")", modifier = Modifier.padding(16.dp))
            }
            itemsIndexed(
                extraDeckCards,
                key = { index, card -> card.cid.toString() + index }) { _, card ->
                SwipeableItem(onDelete = { onDelete(card) }) {
                    DeckCardItem(card)
                }
            }

            if (errorId != null) {
                item {
                    Text(
                        text = stringResource(id = errorId),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        color = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
