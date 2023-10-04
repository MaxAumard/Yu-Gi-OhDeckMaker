package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.android.ui.OutlinedEnumRadioGroup
import fr.uha.hassenforder.android.ui.PictureField
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.TeamFileProvider
import fr.uha.hassenforder.team.model.Gender

@Composable
fun SuccessPersonScreen(
    person: PersonViewModel.PersonUIState,
    uiCB: PersonViewModel.PersonUICallback
) {
    val context = LocalContext.current
    Column(
    ) {
        OutlinedTextField(
            value = person.firstnameState.current ?: "",
            onValueChange = { uiCB.onEvent(PersonViewModel.UIEvent.FirstnameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text (stringResource(id = R.string.firstname)) },
            supportingText = { if (person.firstnameState.errorId != null) Text(stringResource(id = person.firstnameState.errorId)) },
            isError = person.firstnameState.errorId != null,
        )
        OutlinedTextField(
            value = person.lastnameState.current ?: "",
            onValueChange = { uiCB.onEvent(PersonViewModel.UIEvent.LastnameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text (stringResource(id = R.string.lastname)) },
            supportingText = { if (person.lastnameState.errorId != null) Text(stringResource(id = person.lastnameState.errorId)) },
            isError = person.lastnameState.errorId != null,
        )
        OutlinedTextField(
            value = person.phoneState.current ?: "",
            onValueChange = { uiCB.onEvent(PersonViewModel.UIEvent.PhoneChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text (stringResource(id = R.string.phone)) },
            supportingText = { if (person.phoneState.errorId != null) Text(stringResource(id = person.phoneState.errorId)) },
            isError = person.phoneState.errorId != null,
        )
        OutlinedEnumRadioGroup(
            value = person.genderState.current,
            onValueChange = { uiCB.onEvent(PersonViewModel.UIEvent.GenderChanged(Gender.valueOf(it))) },
            modifier = Modifier.fillMaxWidth(),
            items = Gender.values(),
            labelId = R.string.gender,
            errorId = person.genderState.errorId,
        )
        PictureField(
            value = person.pictureState.current,
            onValueChange = { uiCB.onEvent(PersonViewModel.UIEvent.PictureChanged(it)) },
            newImageUriProvider = { TeamFileProvider.getImageUri(context) },
            modifier = Modifier.fillMaxWidth(),
            labelId = R.string.picture,
            errorId = person.pictureState.errorId,
        )
    }
}
