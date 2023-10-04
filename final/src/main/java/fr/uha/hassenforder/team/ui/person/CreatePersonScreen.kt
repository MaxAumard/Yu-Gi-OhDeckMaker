package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.ErrorScreen
import fr.uha.hassenforder.android.ui.LoadingScreen
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePersonScreen (
    vm: PersonViewModel = hiltViewModel(),
    back: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm.isLaunched) {
        if(!vm.isLaunched) {
            val person = Person (firstname = "Michel", lastname = "Hassenforder", phone="0123456789", gender = Gender.BOY)
            vm.create(person)
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
                title = { AppTitle(pageTitleId = R.string.title_person_create, isModified = uiState.isModified()) },
                actions = { AppMenu(menuEntries) },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (uiState.initialState) {
                PersonViewModel.PersonState.Loading -> {
                    LoadingScreen(text = stringResource(R.string.loading))
                }
                PersonViewModel.PersonState.Error -> {
                    ErrorScreen(text = stringResource(R.string.error))
                }
                is PersonViewModel.PersonState.Success -> {
                    SuccessPersonScreen(uiState, vm.uiCallback )
                }
            }
        }
    }
}
