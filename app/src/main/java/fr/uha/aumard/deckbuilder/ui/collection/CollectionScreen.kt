package fr.uha.aumard.deckbuilder.ui.collection

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import fr.uha.aumard.deckbuilder.model.Card

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CollectionScreen(
    viewModel: CollectionViewModel = hiltViewModel(),
    onCreate: (Long) -> Unit,
    onEdit: (Long) -> Unit,
) {
    val extensions by viewModel.extensions.collectAsState(initial = listOf())
    val selectedExtension = viewModel.selectedExtension.collectAsState().value
    val expanded = remember { mutableStateOf(false) }
    val cards by viewModel.getCardsInExtension(selectedExtension?.setName ?: "")
        .collectAsState(initial = listOf())
    Scaffold(topBar = {
        TopAppBar(title = { Text("My Collection") })
    }) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Button(onClick = { expanded.value = true }) {
                Text(text = selectedExtension?.setName ?: "Select an extension")
            }

            DropdownMenu(expanded = expanded.value, onDismissRequest = { expanded.value = false }) {
                extensions.forEach { extension ->
                    DropdownMenuItem(text = { Text(extension.setName) }, onClick = {
                        viewModel.selectExtension(extension)
                        expanded.value = false
                    })
                }
            }

            LazyColumn {
                items(cards) { card ->
                    val isCardInCollection by viewModel.isCardInCollection(card.cid)
                        .collectAsState(initial = false)
                    val ccid by viewModel.getCollectionCardId(card.cid)
                        .collectAsState(initial = -1)
                    CollectionCardItem(card, isCardInCollection) { cardId, isCollected ->
                        if (isCollected) {
                            onEdit(ccid as Long)
                        } else {
                            onCreate(card.cid)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CollectionCardItem(card: Card, isCardInCollection: Boolean, onClick: (Long, Boolean) -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable { onClick(card.cid, isCardInCollection) }
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Image(
                painter = rememberAsyncImagePainter(card.picture),
                contentDescription = "Card Image",
                modifier = Modifier
                    .size(64.dp)
                    .alpha(if (isCardInCollection) 1f else 0.5f)
            )
            Spacer(Modifier.width(8.dp))
            Column {
                Text(text = card.name, fontWeight = FontWeight.Bold)
                Text(text = if (isCardInCollection) "Owned" else "Not owned")
            }
        }
    }
}