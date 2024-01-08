package fr.uha.aumard.deckbuilder.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Deck
import fr.uha.aumard.deckbuilder.ui.deck.CreateDeckScreen
import fr.uha.aumard.deckbuilder.ui.deck.EditDeckScreen
import fr.uha.aumard.deckbuilder.ui.deck.ListDecksScreen

private sealed class TeamNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all decks
    object Teams: TeamNavGraphEntry(
        route = "decks",
        title = R.string.action_decks,
    )

    // to create a deckBuilder
    object Create: TeamNavGraphEntry(
        route = "deckBuilder",
        title = R.string.action_deck_create,
    )

    // to edit a deckBuilder
    object Edit: TeamNavGraphEntry(
        route = "deckBuilder/{did}",
        title = R.string.action_deck_edit,
    ) {
        fun to (tid : Long) : String {
            return route.replace("{did}", tid.toString())
        }
    }

}

fun NavGraphBuilder.teamsNavGraph (
    navController: NavHostController
) {
    navigation(TeamNavGraphEntry.Teams.route, BottomBarNavGraphEntry.Teams.route) {
        composable(route = TeamNavGraphEntry.Teams.route) {
            ListDecksScreen(
                onCreate = { navController.navigate(TeamNavGraphEntry.Create.route) },
                onEdit = { t : Deck -> navController.navigate(TeamNavGraphEntry.Edit.to(t.did)) }
            )
        }
        composable(route = TeamNavGraphEntry.Create.route) {
            CreateDeckScreen (back = { navController.popBackStack() } )
        }
        composable(
            route = TeamNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("did") { type = NavType.LongType })
        ) {
            backStackEntry ->
            EditDeckScreen(tid = backStackEntry.arguments?.getLong("did")!!, back = { navController.popBackStack() } )
        }
    }
}
