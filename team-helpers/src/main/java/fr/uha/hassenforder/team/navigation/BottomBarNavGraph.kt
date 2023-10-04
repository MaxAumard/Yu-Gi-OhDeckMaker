package fr.uha.hassenforder.team.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.outlined.Group
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.vector.rememberVectorPainter
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.uha.hassenforder.team.R

sealed class BottomBarNavGraphEntry(
    val route: String,
    val title: Int,
    val icon: ImageVector,
    val iconFocused: ImageVector
) {

    // for persons
    object Persons: BottomBarNavGraphEntry(
        route = "tl_persons",
        title = R.string.action_persons,
        icon = Icons.Outlined.Person,
        iconFocused = Icons.Filled.Person
    )

    // for teams
    object Teams: BottomBarNavGraphEntry(
        route = "tl_teams",
        title = R.string.action_teams,
        icon = Icons.Outlined.Group,
        iconFocused = Icons.Filled.Group
    )

}

@Composable
fun BottomNavGraph (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarNavGraphEntry.Persons.route,
        modifier = modifier
     ) {
        personsNavGraph(navController = navController)
//        teamsNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    val screens = listOf(
        BottomBarNavGraphEntry.Persons,
        BottomBarNavGraphEntry.Teams
    )

    val navStackBackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navStackBackEntry?.destination

    Row(
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp)
            .background(Color.Transparent)
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically
    ) {
        screens.forEach { screen ->
            AddItem(
                screen = screen,
                currentDestination = currentDestination,
                navController = navController
            )
        }
    }

}

@Composable
private fun RowScope.AddItem(
    screen: BottomBarNavGraphEntry,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true

    val background =
        if (selected) MaterialTheme.colorScheme.primary.copy(alpha = 0.6f) else Color.Transparent

    val contentColor =
        if (selected) Color.White else Color.Black

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
            .background(background)
            .clickable(onClick = {
                navController.navigate(screen.route) {
                    popUpTo(navController.graph.findStartDestination().id)
                    launchSingleTop = true
                }
            })
    ) {
        Row(
            modifier = Modifier
                .padding(start = 10.dp, end = 10.dp, top = 8.dp, bottom = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Icon(
                painter = rememberVectorPainter(image = if (selected) screen.iconFocused else screen.icon),
                contentDescription = "icon",
                tint = contentColor
            )
            AnimatedVisibility(visible = selected) {
                Text(
                    text = stringResource(id = screen.title),
                    color = contentColor
                )
            }
        }
    }
}
