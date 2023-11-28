package fr.uha.hassenforder.team.ui.person

import android.net.Uri
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
import fr.uha.hassenforder.android.ui.ErrorScreen
import fr.uha.hassenforder.android.ui.LoadingScreen
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.team.model.Type
import fr.uha.hassenforder.team.model.Card

import fr.uha.hassenforder.team.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreatePersonScreen(
    vm: PersonViewModel = hiltViewModel(),
    back: () -> Unit
) {
    val state by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(key1 = vm.isLaunched) {
        if (!vm.isLaunched) {
            val card = Card(
                0,
                "michel",
                "hassenforder",
                "0123456789",
                Type.TRAP,
                Uri.parse("content://media/picker/0/com.android.providers.media.photopicker/media/1000000039")
            )
            vm.create(card)
            vm.isLaunched = true
        }
    }

    val menuEntries = listOf(
        AppMenuEntry.ActionEntry(
            title = R.string.save,
            icon = Icons.Filled.Save,
            enabled = state.isSavable(),
            listener = { vm.save() }
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(
                        appNameId = R.string.app_name,
                        pageTitleId = R.string.person_create,
                        isModified = state.isModified()
                    )
                },
                actions = { AppMenu(entries = menuEntries) }
            )
        }
    )
    {
        Column(
            modifier = Modifier.padding(it)
        ) {
            when (state.initialState) {
                PersonViewModel.PersonState.Loading ->
                    LoadingScreen(text = stringResource(id = R.string.loading))

                PersonViewModel.PersonState.Error ->
                    ErrorScreen(text = stringResource(id = R.string.error))

                is PersonViewModel.PersonState.Success ->
                    SuccessPersonScreen(state, vm.uiCallback)
            }
        }
    }
}

