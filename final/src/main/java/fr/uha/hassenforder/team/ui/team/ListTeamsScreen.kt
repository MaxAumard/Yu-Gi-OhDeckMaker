package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.android.ui.*
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Team

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListTeamsScreen(
    vm: ListTeamsViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onEdit: (team : Team) -> Unit,
    ) {
    val list = vm.teams.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf (
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = { vm.populateDatabase() }),
        AppMenuEntry.OverflowEntry(title = R.string.clear, listener = { vm.clearDatabase() }),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppTitle (pageTitleId = R.string.title_teams) },
                actions = { AppMenu(menuEntries) },
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = onCreate) {
                Icon(Icons.Filled.Add, contentDescription = "add")
            }
        }
    ) {
        innerPadding -> LazyColumn(
            modifier = Modifier.padding(innerPadding)
        ) {
            items(
                items = list.value,
                key = { team -> team.tid }
            ) { item ->
                SwipeableItem(
                    onEdit = { onEdit(item) },
                    onDelete = { vm.delete(item) }
                ) { TeamItem(item) }
            }
        }
    }
}

@Composable
private fun TeamItem (team: Team) {
    ListItem (
        headlineContent = {
            Text(team.name)
        },
        supportingContent = {
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                Icon(imageVector = Icons.Outlined.Start, contentDescription = "start", modifier = Modifier.padding(end = 8.dp))
                Text(
                    Converter.convert(team.startDay),
                    fontWeight = FontWeight.Bold
                )
                Text(stringResource(id = R.string.duration))
                Text(
                    team.duration.toString(),
                    fontWeight = FontWeight.Bold
                )
            }
        },
    )
}
