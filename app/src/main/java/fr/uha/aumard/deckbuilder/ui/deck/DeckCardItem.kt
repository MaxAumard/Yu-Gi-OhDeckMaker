package fr.uha.aumard.deckbuilder.ui.deck

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Type

@Composable
fun DeckCardItem(card: Card) {
    val iconId = when (card.type) {
        Type.MAGIC -> R.drawable.ic_magic
        Type.TRAP -> R.drawable.ic_trap
        Type.MONSTER -> R.drawable.ic_monster
        else -> null
    }
    ListItem(
        headlineContent = {
            Row {
                Text(card.name, modifier = Modifier.padding(end = 8.dp))
            }
        },
        supportingContent = {
            Row {
                iconId?.let {
                    Icon(
                        painter = painterResource(id = it),
                        contentDescription = "Card Type Icon",
                        modifier = Modifier
                            .padding(end = 8.dp)
                            .size(24.dp),
                        tint = Color.Unspecified,
                    )
                }
                if (card.type != Type.ALL) {
                    Text(
                        card.type.toString().lowercase().replaceFirstChar { it.titlecase() },
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