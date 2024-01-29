import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.KeyboardType
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.ui.collection.CollectionViewModel
import fr.uha.aumard.deckbuilder.ui.deck.DeckViewModel

@Composable
fun SuccessCollectionCardScreen(
    collection: CollectionViewModel.CollectionUIState.Success,
    uiCB: CollectionViewModel.UIEvent
) {
    Column {
        OutlinedTextField(
            value = collectionCard.condition.current ?: "",
            onValueChange = { uiCB.onEvent(uiCB.ConditionChanged(it)) },
            label = { Text(text = stringResource(R.string.condition)) }
        )
        OutlinedTextField(
            value = collectionCard.language.current ?: "",
            onValueChange = { uiCB.onEvent(uiCB.LanguageChanged(it)) },
            label = { Text(text = stringResource(R.string.language)) }
        )
        OutlinedTextField(
            value = collectionCard.rarity.current ?: "",
            onValueChange = { uiCB.onEvent(uiCB.RarityChanged(it)) },
            label = { Text(text = stringResource(R.string.rarity)) }
        )
        NumberField(
            value = collectionCard.quantity.current,
            onValueChange = { uiCB.onEvent(uiCB.QuantityChanged(it)) }
        )
    }
}

@Composable
fun NumberField(value: Int, onValueChange: (Int) -> Unit) {
    TextField(
        value = value.toString(),
        onValueChange = { newValue -> onValueChange(newValue.toIntOrNull() ?: 0) },
        label = { Text("Quantity") },
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
    )
}
