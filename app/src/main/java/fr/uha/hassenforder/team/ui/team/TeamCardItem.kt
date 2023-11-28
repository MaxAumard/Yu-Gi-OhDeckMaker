package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.Error
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
import fr.uha.hassenforder.team.model.Card

@Composable
fun TeamCardItem (card : Card) {
    ListItem (
        headlineContent = {
            Row() {
                Text(card.name, modifier = Modifier.padding(end = 8.dp))
                Text(card.description)
            }
        },
        supportingContent = {
            Row() {
                Icon(imageVector = Icons.Outlined.Phone, contentDescription = "phone", modifier = Modifier.padding(end = 8.dp))
                Text(card.phone, fontSize = 16.sp, fontWeight = FontWeight.Bold)
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
