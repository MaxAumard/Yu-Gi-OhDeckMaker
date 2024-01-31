package fr.uha.hassenforder.team.ui.team

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.ui.*
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class PersonPickerViewModel @Inject constructor(private val dao: PersonDao) : ViewModel() {

    val persons: Flow<List<Person>> = dao.getAll()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PersonPicker(
    vm: PersonPickerViewModel = hiltViewModel(),
    @StringRes title: Int?,
    onSelect: (person: Person?) -> Unit,
) {
    val list = vm.persons.collectAsStateWithLifecycle(initialValue = emptyList())
    Dialog(onDismissRequest = { onSelect(null) }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppTitle(pageTitleId = title ?: R.string.person_select) },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(
                    items = list.value,
                    key = { person -> person.pid }
                ) { item ->
                    PersonItem(item, onSelect)
                }
            }
        }
    }
}

@Composable
private fun PersonItem(person: Person, onSelect: (person: Person?) -> Unit) {
    val gender: ImageVector =
        when (person.gender) {
            Gender.NO -> Icons.Outlined.DoNotDisturb
            Gender.GIRL -> Icons.Outlined.Female
            Gender.BOY -> Icons.Outlined.Male
        }
    ListItem(
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable(onClick = { onSelect(person) }),
        headlineContent = {
            Row() {
                Text(person.firstname, modifier = Modifier.padding(end = 8.dp))
                Text(person.lastname)
            }
        },
        /*

                        supportingContent = {
                            Row() {
                                Icon(imageVector = Icons.Outlined.Phone, contentDescription = "phone", modifier = Modifier.padding(end = 8.dp))
                                Text(person.phone, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                            }
                },
                 */
        /*
                leadingContent = {
                    if (person.picture != null) {
                        AsyncImage(
                            model = person.picture,
                            modifier = Modifier.size(64.dp),
                            contentDescription = null,
                            error = rememberVectorPainter(Icons.Outlined.Error),
                            placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                        )
                    }
        },
         */
        /*
                trailingContent = {
                    Icon(imageVector = gender, contentDescription = "gender", modifier = Modifier.size(48.dp) )
                },
         */
    )
}
