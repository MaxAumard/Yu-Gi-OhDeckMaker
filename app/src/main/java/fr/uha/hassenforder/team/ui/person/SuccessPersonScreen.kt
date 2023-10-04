package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.android.ui.OutlinedEnumRadioGroup
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Gender

@Composable
fun SuccessPersonScreen(
    person : Person
) {
    Column {
        OutlinedTextField(
            value = person.firstname,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text (stringResource(id = R.string.firstname)) }
        )
        OutlinedTextField(
            value = person.lastname,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text (stringResource(id = R.string.lastname)) }
        )
        OutlinedTextField(
            value = person.phone,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            label = { Text (stringResource(id = R.string.phone)) }
        )
        OutlinedEnumRadioGroup(
            value = person.gender,
            onValueChange = { },
            modifier = Modifier.fillMaxWidth(),
            items =  Gender.values(),
            labelId = R.string.gender
        )
    }
}
