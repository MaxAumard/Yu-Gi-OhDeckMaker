package fr.uha.hassenforder.team.ui.person

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import fr.uha.hassenforder.android.ui.*
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListPersonsScreen(
    vm: ListPersonsViewModel = hiltViewModel(),
    onCreate: () -> Unit,
    onEdit: (person : Person) -> Unit,
    ) {
    val list = vm.persons.collectAsStateWithLifecycle(initialValue = emptyList())

    val menuEntries = listOf (
        AppMenuEntry.OverflowEntry(title = R.string.populate, listener = { vm.populateDatabase() }),
        AppMenuEntry.OverflowEntry(title = R.string.clear, listener = { vm.clearDatabase() }),
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { AppTitle (pageTitleId = R.string.title_persons) },
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
                key = { person -> person.person.pid }
            ) { item ->
                SwipeableItem(
                    onEdit = { onEdit(item.person) },
                    onDelete = { vm.delete(item.person) }
                ) { PersonItem(item) }
            }
        }
    }
}

@Composable
private fun PersonItem (person : PersonWithDetails) {
    val gender : ImageVector =
            when (person.person.gender) {
                Gender.NO -> Icons.Outlined.DoNotDisturb
                Gender.GIRL -> Icons.Outlined.Female
                Gender.BOY -> Icons.Outlined.Male
            }
    ListItem (
        headlineContent = {
            Row() {
                Text(person.person.firstname, modifier = Modifier.padding(end = 8.dp))
                Text(person.person.lastname)
            }
        },
        supportingContent = {
            Column() {
                Row() {
                    Icon(
                        imageVector = Icons.Outlined.Phone,
                        contentDescription = "phone",
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Text(person.person.phone, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    Icon(
                        imageVector = Icons.Outlined.Leaderboard,
                        contentDescription = "leader",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    )
                    Text(person.teamLeadingCount.toString())
                    Icon(
                        imageVector = Icons.Outlined.Group,
                        contentDescription = "group",
                        modifier = Modifier.padding(start = 8.dp, end = 8.dp)
                    )
                    Text(person.teamMemberCount.toString())
                }
            }
        },
        leadingContent = {
            if (person.person.picture != null) {
                AsyncImage(
                    model = person.person.picture,
                    modifier = Modifier.size(64.dp),
                    contentDescription = null,
                    error = rememberVectorPainter(Icons.Outlined.Error),
                    placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                )
            }
        },
        trailingContent = {
            Icon(imageVector = gender, contentDescription = "gender", modifier = Modifier.size(48.dp) )
        },
    )
}
/*
@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    PersonItem (Person(-1, "mimi", "Hassenforder", "3614", Gender.BOY))
}
*/