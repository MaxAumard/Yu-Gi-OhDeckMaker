package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringArrayResource
import androidx.compose.ui.res.stringResource
import fr.uha.hassenforder.android.ui.OutlinedDateField
import fr.uha.hassenforder.android.ui.OutlinedSpinnerField
import fr.uha.hassenforder.team.R

@Composable
fun SuccessTeamScreen(
    team: TeamViewModel.TeamUIState,
    uiCB: TeamViewModel.TeamUICallback
) {
    val showDialog = remember { mutableStateOf(false) }

    Column(
    ) {
        OutlinedTextField(
            value = team.name.current ?: "",
            onValueChange = { uiCB.onEvent(TeamViewModel.UIEvent.NameChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = { Text (text = stringResource(R.string.teamname)) },
            supportingText = { if (team.name.errorId != null) Text(stringResource(id = team.name.errorId)) },
            isError = team.name.errorId != null,
        )
        OutlinedDateField(
            value = team.startDay.current,
            onValueChange = { uiCB.onEvent(TeamViewModel.UIEvent.StartDayChanged(it)) },
            modifier = Modifier.fillMaxWidth(),
            label = R.string.start_day,
            errorId = team.startDay.errorId
        )
        ListMembersField(
            value = team.members.current,
            onAdd = { uiCB.onEvent(TeamViewModel.UIEvent.MemberAdded(it)) },
            onDelete = { uiCB.onEvent(TeamViewModel.UIEvent.MemberDeleted(it)) },
            errorId = team.members.errorId
        )
    }

}
