package fr.uha.aumard.deckbuilder.navigation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.currentBackStackEntryAsState
import fr.uha.aumard.deckbuilder.R

sealed class BottomBarNavGraphEntry(
    val route: String,
    val icon: Int,
    val iconFocused: Int
) {

    // for cards
    object Cards: BottomBarNavGraphEntry(
        route = "tl_cards",
        icon = R.drawable.ic_outlined_card,
        iconFocused = R.drawable.ic_filled_card
    )

    // for decks
    object Decks : BottomBarNavGraphEntry(
        route = "tl_decks",
        icon = R.drawable.ic_outlined_deck,
        iconFocused = R.drawable.ic_filled_deck
    )

    object Collection : BottomBarNavGraphEntry(
        route = "tl_collection",
        icon = R.drawable.ic_outlined_collection,
        iconFocused = R.drawable.ic_filled_collection
    )
}

@Composable
fun BottomNavGraph (
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = BottomBarNavGraphEntry.Cards.route,
        modifier = modifier
     ) {
        cardsNavGraph(navController = navController)
        decksNavGraph(navController = navController)
        collectionNavGraph(navController = navController)
    }
}

@Composable
fun BottomBar(navController: NavHostController) {

    val screens = listOf(
        BottomBarNavGraphEntry.Cards,
        BottomBarNavGraphEntry.Decks,
        BottomBarNavGraphEntry.Collection
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
private fun AddItem(
    screen: BottomBarNavGraphEntry,
    currentDestination: NavDestination?,
    navController: NavHostController
) {
    val selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true


    val iconPainter = painterResource(id = if (selected) screen.iconFocused else screen.icon)

    Box(
        modifier = Modifier
            .height(40.dp)
            .clip(CircleShape)
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
                painter = iconPainter,
                contentDescription = "icon",
            )
        }
    }
}
