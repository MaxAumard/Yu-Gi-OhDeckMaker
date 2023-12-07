package fr.uha.aumard.deckbuilder.ui.card

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import fr.uha.aumard.android.ui.OutlinedEnumRadioGroup
import fr.uha.aumard.android.ui.PictureField
import fr.uha.aumard.deckbuilder.DeckBuilderFileProvider
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Type

@Composable
fun SuccessPersonScreen(
    card: CardViewModel.CardUIState,
    uiCB: CardViewModel.CardUICallback
) {
    val context = LocalContext.current

    Column(
    ) {
        OutlinedTextField(
            value = card.nameState.current ?: "",
            onValueChange = { uiCB.onEvent(CardViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.card_name)) },
            supportingText = { if (card.nameState.errorId != null) Text(stringResource(id = card.nameState.errorId)) },
            isError = card.nameState.errorId != null,
        )
        OutlinedTextField(
            value = card.descriptionState.current ?: "",
            onValueChange = { uiCB.onEvent(CardViewModel.UIEvent.DescriptionChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.description)) },
            supportingText = { if (card.descriptionState.errorId != null) Text(stringResource(id = card.descriptionState.errorId)) },
            isError = card.descriptionState.errorId != null,
        )
        OutlinedTextField(
            value = card.descriptionState.current ?: "",
            onValueChange = { uiCB.onEvent(CardViewModel.UIEvent.LevelChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text(stringResource(id = R.string.phone)) },
            supportingText = { if (card.descriptionState.errorId != null) Text(stringResource(id = card.descriptionState.errorId)) },
            isError = card.descriptionState.errorId != null,
        )
        OutlinedEnumRadioGroup(
            value = card.typeState.current,
            onValueChange = { uiCB.onEvent(CardViewModel.UIEvent.TypeChanged(Type.valueOf(it))) },
            modifier = Modifier.fillMaxWidth(),
            items = Type.values(),
            labelId = R.string.gender,
            errorId = card.typeState.errorId,
        )
        PictureField(
            value = card.pictureState.current,
            onValueChange = { uiCB.onEvent(CardViewModel.UIEvent.PictureChanged(it)) },
            newImageUriProvider = { DeckBuilderFileProvider.getImageUri(context) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.picture,
            errorId = card.pictureState.errorId
        )
    }
}
