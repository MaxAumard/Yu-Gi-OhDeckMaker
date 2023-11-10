package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.android.ui.AppMenu
import fr.uha.hassenforder.android.ui.AppMenuEntry
import fr.uha.hassenforder.android.ui.AppTitle
import fr.uha.hassenforder.android.ui.SwipeableItem
import fr.uha.hassenforder.team.Greeting
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails
import fr.uha.hassenforder.team.ui.theme.Team2023Theme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPersonsScreen(
    vm : ListPersonsViewModel = hiltViewModel(),
    onCreate : () -> Unit,
    onEdit : (p : Person) -> Unit,
) {
    val persons = vm.persons.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf(
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = {vm.feed() } ),
        AppMenuEntry.OverflowEntry(title = R.string.clean, listener = {vm.clean() } )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    AppTitle(pageTitleId = R.string.person_list,)
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
                items = persons.value,
                key = { person -> person.person.pid }
            ) {
                item -> SwipeableItem (
                    onEdit = { onEdit(item.person) },
                    onDelete = {},
                ) {
                    PersonItem(item)
                }
            }
        }
    }

}

@Composable
fun PersonItem(person : PersonWithDetails) {
    val gender : ImageVector = when(person.person.gender) {
        Gender.NO -> Icons.Outlined.DoNotDisturb
        Gender.GIRL -> Icons.Outlined.Female
        Gender.BOY -> Icons.Outlined.Male
    }

    ListItem (
        headlineContent = {
            Row() {
                Text(person.person.firstname, modifier = Modifier.padding(end = 4.dp))
                Text(person.person.lastname)
            }
       },
        leadingContent = {
            AsyncImage(
                model = person.person.picture,
                modifier = Modifier.size(64.dp),
                contentDescription = "Selected image",
                error = rememberVectorPainter(Icons.Outlined.Error),
                placeholder = rememberVectorPainter(Icons.Outlined.Casino),
            )
        },
        trailingContent = {
            Icon(imageVector = gender, contentDescription = null, modifier = Modifier.size(48.dp))
        },
        supportingContent = {
            Column {
                Row() {
                    Icon(imageVector = Icons.Outlined.Phone, contentDescription = null)
                    Text(person.person.phone, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
                Text(person.leaderCount.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Text(person.memberCount.toString(), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        }
    )
}
