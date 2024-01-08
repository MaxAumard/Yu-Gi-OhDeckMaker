package fr.uha.aumard.deckbuilder.ui.deck

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
import fr.uha.aumard.android.ui.*
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Deck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateDeckScreen(
    vm: DeckViewModel = hiltViewModel(),
    back: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()
    val epoxy =  remember { mutableStateOf(false) }

    LaunchedEffect(vm.isLaunched) {
        if(!vm.isLaunched) {
            vm.create(Deck())
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
                title = { AppTitle (pageTitleId = R.string.title_deck_create, isModified = uiState.isModified()) },
                actions = { AppMenu(menuEntries) }
            )
        }
    ) { innerPadding -> Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (uiState.initialState) {
                DeckViewModel.DeckState.Loading -> {
                    LoadingScreen(text = stringResource(R.string.loading))
                }

                DeckViewModel.DeckState.Error -> {
                    ErrorScreen(text = stringResource(R.string.error))
                }

                is DeckViewModel.DeckState.Success -> {
                    SuccessDeckScreen(uiState, vm.uiCallback)
                }
            }
        }
    }
}
