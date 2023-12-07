package fr.uha.aumard.deckbuilder.ui.deck

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Start
import androidx.compose.material.icons.outlined.Timer
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import fr.uha.aumard.android.ui.AppMenu
import fr.uha.aumard.android.ui.AppMenuEntry
import fr.uha.aumard.android.ui.AppTitle
import fr.uha.aumard.android.ui.SwipeableItem
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Deck

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTeamsScreen(
    vm : ListTeamsViewModel = hiltViewModel(),
    onCreate : () -> Unit,
    onEdit : (p : Deck) -> Unit,
) {
    val teams = vm.teams.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = {vm.feed() } ),
        AppMenuEntry.OverflowEntry(title = R.string.clean, listener = {vm.clean() } )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(pageTitleId = R.string.team_list,)
                },
                actions = { AppMenu(entries = menuEntries) }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = null)
            }
        }
    ) {
        innerPadding -> LazyColumn(modifier = Modifier.padding(innerPadding)) {
            items (
                items = teams.value,
                key = { team -> team.did }
            ) {
                item -> SwipeableItem (
                    onEdit = { onEdit(item) },
                    onDelete = {},
                ) {
                    teamItem(item)
                }
            }
        }
    }

}

@Composable
fun teamItem(deck : Deck) {

    ListItem (
        headlineContent = {
            Text(deck.name)
        },
        supportingContent = {
            Row () {
                Icon(imageVector = Icons.Outlined.Start, contentDescription = null)
                Text(UIConverter.convert(deck.creationDate), fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
                Icon(imageVector = Icons.Outlined.Timer, contentDescription = null)
                Text(UIConverter.convert(deck.creationDate), fontSize = 16.sp, fontWeight = FontWeight.Bold, modifier = Modifier.padding(horizontal = 4.dp))
            }
        }
    )
}
