package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.android.ui.*
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateTeamScreen(
    vm: TeamViewModel = hiltViewModel(),
    back: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val epoxy =  remember { mutableStateOf(false) }

    LaunchedEffect(vm.isLaunched) {
        if(!vm.isLaunched) {
            vm.create(Team())
            vm.isLaunched = true
        }
    }

    val menuEntries = listOf (
        AppMenuEntry.ActionEntry(
            title = R.string.save,
            icon = Icons.Filled.Save,
            enabled = uiState.isSavable(),
            listener = { vm.save(); back() }
        ),
    )
    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppTitle (pageTitleId = R.string.title_team_create, isModified = uiState.isModified()) },
                actions = { AppMenu(menuEntries) }
            )
        }
    ) { innerPadding -> Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (uiState.initialState) {
                TeamViewModel.TeamState.Loading -> {
                    LoadingScreen(text = stringResource(R.string.loading))
                }
                TeamViewModel.TeamState.Error -> {
                    ErrorScreen(text = stringResource(R.string.error))
                }
                is TeamViewModel.TeamState.Success -> {
                    SuccessTeamScreen(uiState, vm.uiCallback)
                }
            }
        }
    }
}
