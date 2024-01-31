import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Condition
import fr.uha.aumard.deckbuilder.model.Language
import fr.uha.aumard.deckbuilder.model.Rarity
import fr.uha.aumard.deckbuilder.ui.collection.CollectionViewModel

@Composable
fun SuccessCollectionCardScreen(
    collectionCard: CollectionViewModel.CollectionUIState,
    uiCB: CollectionViewModel.CollectionUICallback
) {
    var expandedCondition by remember { mutableStateOf(false) }
    var expandedLanguage by remember { mutableStateOf(false) }
    var expandedRarity by remember { mutableStateOf(false) }

    Column {
        // Condition Selection
        SelectButton(
            text = collectionCard.condition.current?.name
                ?: stringResource(R.string.condition),
            onClick = { expandedCondition = true }
        )
        DropdownMenu(
            expanded = expandedCondition,
            onDismissRequest = { expandedCondition = false }
        ) {
            Condition.values().forEach { condition ->
                DropdownMenuItem({
                    Text(text = condition.name)
                },
                    onClick = {
                        uiCB.onEvent(CollectionViewModel.UIEvent.ConditionChanged(condition))
                        expandedCondition = false
                    }
                )
            }
        }

        // Language Selection
        SelectButton(
            text = collectionCard.language.current?.name
                ?: stringResource(R.string.language),
            onClick = { expandedLanguage = true }
        )
        DropdownMenu(
            expanded = expandedLanguage,
            onDismissRequest = { expandedLanguage = false }
        ) {
            Language.values().forEach { language ->
                DropdownMenuItem(
                    {
                        Text(text = language.name)
                    },
                    onClick = {
                        uiCB.onEvent(CollectionViewModel.UIEvent.LanguageChanged(language))
                        expandedLanguage = false
                    }
                )
            }
        }

        // Rarity Selection
        SelectButton(
            text = collectionCard.rarity.current?.name ?: stringResource(R.string.rarity),
            onClick = { expandedRarity = true }
        )
        DropdownMenu(
            expanded = expandedRarity,
            onDismissRequest = { expandedRarity = false }
        ) {
            Rarity.values().forEach { rarity ->
                DropdownMenuItem({
                    Text(text = rarity.name)
                },
                    onClick = {
                        uiCB.onEvent(CollectionViewModel.UIEvent.RarityChanged(rarity))
                        expandedRarity = false
                    }
                )
            }
        }
    }
}

@Composable
fun SelectButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(text)
        Spacer(Modifier.width(8.dp))
        Icon(Icons.Default.ArrowDropDown, contentDescription = null)
    }
}