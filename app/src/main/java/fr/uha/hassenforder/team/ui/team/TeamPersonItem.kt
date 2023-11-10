package fr.uha.hassenforder.team.ui.team

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Casino
import androidx.compose.material.icons.outlined.DoNotDisturb
import androidx.compose.material.icons.outlined.Error
import androidx.compose.material.icons.outlined.Female
import androidx.compose.material.icons.outlined.Male
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person

@Composable
fun TeamPersonItem (person : Person) {
    val gender : ImageVector =
        when (person.gender) {
            Gender.NO -> Icons.Outlined.DoNotDisturb
            Gender.GIRL -> Icons.Outlined.Female
            Gender.BOY -> Icons.Outlined.Male
        }
    ListItem (
        headlineContent = {
            Row() {
                Text(person.firstname, modifier = Modifier.padding(end = 8.dp))
                Text(person.lastname)
            }
        },
        supportingContent = {
            Row() {
                Icon(imageVector = Icons.Outlined.Phone, contentDescription = "phone", modifier = Modifier.padding(end = 8.dp))
                Text(person.phone, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }
        },
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
        trailingContent = {
            Icon(imageVector = gender, contentDescription = "gender", modifier = Modifier.size(48.dp) )
        },
    )
}
