package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.hassenforder.android.ui.ErrorScreen
import fr.uha.hassenforder.android.ui.LoadingScreen
import fr.uha.hassenforder.team.database.TeamDatabase
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.repository.PersonRepository

import fr.uha.hassenforder.team.R

@Composable
fun CreatePersonScreen (
    vm : PersonViewModel = PersonViewModel(PersonRepository(TeamDatabase.get().personDAO))
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = vm.isLaunched) {
        if (!vm.isLaunched) {
            val person = Person(0, "michel", "hassenforder", "0123456789", Gender.BOY)
            vm.create(person)
            vm.isLaunched = true
        }
    }

    Scaffold (

    )
    {
        Column (
            modifier = Modifier.padding(it)
        ) {
            when (state.initialState) {
                PersonViewModel.PersonState.Loading ->
                    LoadingScreen(text = stringResource(id = R.string.loading))
                PersonViewModel.PersonState.Error ->
                    ErrorScreen(text = stringResource(id = R.string.error))
                is PersonViewModel.PersonState.Success ->
                    SuccessPersonScreen(state, vm.uiCallback )
            }
        }
    }
}

