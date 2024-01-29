import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.wear.compose.material.MaterialTheme
import coil.compose.AsyncImage
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Type
import fr.uha.aumard.deckbuilder.ui.card.CardViewModel


@Composable
fun CardInfoScreen(
    vm: CardViewModel = hiltViewModel(),
    cid: Long
) {
    val card by vm.getCardById(cid).collectAsStateWithLifecycle(initialValue = null)
    val iconId = when (card?.type) {
        Type.MAGIC -> R.drawable.ic_magic
        Type.TRAP -> R.drawable.ic_trap
        Type.MONSTER -> R.drawable.ic_monster
        else -> null
    }
    card?.let { c ->
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                CardImage(card = c)
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(
                        text = c.name,
                        style = MaterialTheme.typography.title2,
                        modifier = Modifier.padding(bottom = 8.dp)
                    )
                    if (c.type != Type.ALL) {
                        Row {
                            Icon(
                                painter = painterResource(id = iconId!!),
                                contentDescription = "Card Type Icon",
                                modifier = Modifier
                                    .padding(end = 8.dp)
                                    .size(24.dp),
                                tint = Color.Unspecified,
                            )
                            Text(
                                c.type.toString().lowercase().replaceFirstChar { it.titlecase() },
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(4.dp))

                    LevelStars(level = c.level)
                    Spacer(modifier = Modifier.height(4.dp))
                    c.attack?.let { attack -> Text(stringResource(R.string.card_attack, attack)) }
                    Spacer(modifier = Modifier.height(4.dp))
                    c.defense?.let { defense ->
                        Text(
                            stringResource(
                                R.string.card_defense,
                                defense
                            )
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    ExtraMainDeckIcon(isExtraDeck = c.isExtraDeck)
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = c.description)
        }
    } ?: Text(stringResource(R.string.loading_card_details))
}


@Composable
fun CardImage(card: Card) {
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp
    val imageWidth = screenWidth / 3

    AsyncImage(
        model = card.picture,
        contentDescription = stringResource(R.string.card_image),
        modifier = Modifier
            .width(imageWidth)
            .heightIn(min = 200.dp),
        contentScale = ContentScale.Fit
    )
}

@Composable
fun LevelStars(level: String?) {
    val levelInt = level?.toIntOrNull() ?: 0

    Row {
        repeat(levelInt) {
            Icon(
                imageVector = Icons.Default.Star,
                contentDescription = stringResource(R.string.card_level_star),
                tint = Color.Yellow,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ExtraMainDeckIcon(isExtraDeck: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        val (icon, label) = if (isExtraDeck) {
            Pair(Icons.Default.Add, R.string.extra_deck)
        } else {
            Pair(Icons.Default.Home, R.string.main_deck)
        }
        Icon(
            imageVector = icon,
            contentDescription = stringResource(label)
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(text = stringResource(label))
    }
}
