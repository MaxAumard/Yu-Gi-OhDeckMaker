package fr.uha.aumard.deckbuilder.ui.collection

import SuccessCollectionCardScreen
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
import fr.uha.aumard.android.ui.AppMenu
import fr.uha.aumard.android.ui.AppMenuEntry
import fr.uha.aumard.android.ui.AppTitle
import fr.uha.aumard.android.ui.ErrorScreen
import fr.uha.aumard.android.ui.LoadingScreen
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.CollectionCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateCollectionCardScreen(
    cardId: Long,
    vm: CollectionViewModel = hiltViewModel(),
    back: () -> Unit,
) {
    val uiState by vm.uiState.collectAsStateWithLifecycle()

    LaunchedEffect(vm.isLaunched) {
        if (!vm.isLaunched) {
            vm.create(cardId, CollectionCard())
            vm.isLaunched = true
        }
    }

    val menuEntries = listOf(
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
                title = {
                    AppTitle(
                        pageTitleId = R.string.title_collection_card_create,
                        isModified = uiState.isModified()
                    )
                },
                actions = { AppMenu(menuEntries) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            when (uiState.initialState) {
                CollectionViewModel.CollectionCardState.Loading -> {
                    LoadingScreen(text = stringResource(R.string.loading))
                }

                CollectionViewModel.CollectionCardState.Error -> {
                    ErrorScreen(text = stringResource(R.string.error))
                }

                is CollectionViewModel.CollectionCardState.Success -> {
                    SuccessCollectionCardScreen(uiState, vm.uiCallback)
                }
            }
        }
    }
}
