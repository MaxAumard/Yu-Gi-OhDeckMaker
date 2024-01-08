package fr.uha.aumard.deckbuilder.ui.deck

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.uha.aumard.deckbuilder.R

@Composable
fun SuccessDeckScreen(
    deck: DeckViewModel.DeckUIState,
    uiCB: DeckViewModel.DeckUICallback
) {
    val showDialog = remember { mutableStateOf(false) }

    Column(
    ) {
        OutlinedTextField(
            value = deck.name.current ?: "",
            onValueChange = { uiCB.onEvent(DeckViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(text = stringResource(R.string.deckname)) },
            supportingText = { if (deck.name.errorId != null) Text(stringResource(id = deck.name.errorId)) },
            isError = deck.name.errorId != null,
        )
        ListCardsField(
            value = deck.card.current,
            onAdd = { uiCB.onEvent(DeckViewModel.UIEvent.CardAdded(it.cid)) },
            onDelete = { uiCB.onEvent(DeckViewModel.UIEvent.CardDeleted(it)) },
            errorId = deck.card.errorId
        )
    }

}
