package fr.uha.aumard.deckbuilder.ui.deck

import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.android.ui.*
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.database.CardDao
import fr.uha.aumard.deckbuilder.model.Card
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CardPickerViewModel @Inject constructor(private val dao: CardDao) : ViewModel() {

    val cards: Flow<List<Card>> = dao.getAll()

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CardPicker (
    vm: CardPickerViewModel = hiltViewModel(),
    @StringRes title : Int?,
    onSelect: (card : Card?) -> Unit,
) {
    val list = vm.cards.collectAsStateWithLifecycle(initialValue = emptyList())
    Dialog(onDismissRequest = { onSelect(null) }) {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = { AppTitle(pageTitleId = title?: R.string.person_select) },
                )
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier.padding(innerPadding)
            ) {
                items(
                    items = list.value,
                    key = { person -> person.cid }
                ) {
                    item -> PersonItem(item, onSelect)
                }
            }
        }
    }
}

@Composable
private fun PersonItem (card : Card, onSelect: (card : Card?) -> Unit) {
    ListItem (
        modifier = Modifier
            .padding(5.dp)
            .fillMaxWidth()
            .clickable(onClick = { onSelect(card) }),
        headlineContent = {
            Row() {
                Text(card.name, modifier = Modifier.padding(end = 8.dp))
                Text(card.description)
            }
        },
        /*

                        supportingContent = {
                            Row() {
                                Icon(imageVector = Icons.Outlined.Phone, contentDescription = "level", modifier = Modifier.padding(end = 8.dp))
                                Text(person.level, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
                    Icon(imageVector = type, contentDescription = "type", modifier = Modifier.size(48.dp) )
                },
         */
    )
}
