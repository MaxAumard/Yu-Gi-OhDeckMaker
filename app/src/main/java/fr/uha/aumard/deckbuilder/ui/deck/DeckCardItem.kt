package fr.uha.aumard.deckbuilder.ui.deck

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.KeyboardArrowUp
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.uha.aumard.deckbuilder.model.Card

@Composable
fun DeckCardItem (card : Card) {
    ListItem (
        headlineContent = {
            Row() {
                Text(card.name, modifier = Modifier.padding(end = 8.dp))
            }
        },
        supportingContent = {
            Row() {
                Icon(
                    imageVector = Icons.Outlined.KeyboardArrowUp,
                    contentDescription = "level",
                    modifier = Modifier.padding(end = 8.dp)
                )
                card.level?.let {
                    Text(
                        it,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        leadingContent = {
            if (card.picture != null) {
                AsyncImage(
                    model = card.picture,
                    modifier = Modifier.size(64.dp),
                    contentDescription = null,
                    error = rememberVectorPainter(Icons.Outlined.Error),
                    placeholder = rememberVectorPainter(Icons.Outlined.Casino),
                )
            }
        },
    )
}
