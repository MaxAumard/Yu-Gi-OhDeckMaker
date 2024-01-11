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
import fr.uha.aumard.deckbuilder.ui.collection.ListCollectionScreen

private sealed class DeckNavGraphEntry(
    val route: String,
    val title: Int,
) {

    object Decks : DeckNavGraphEntry(
        route = "decks",
        title = R.string.action_decks,
    )

    object Create : DeckNavGraphEntry(
        route = "deckBuilder",
        title = R.string.action_deck_create,
    )

    object Edit : DeckNavGraphEntry(
        route = "deckBuilder/{did}",
        title = R.string.action_deck_edit,
    ) {
        fun to(tid: Long): String {
            return route.replace("{did}", tid.toString())
        }
    }

}

fun NavGraphBuilder.decksNavGraph(
    navController: NavHostController
) {
    navigation(DeckNavGraphEntry.Decks.route, BottomBarNavGraphEntry.Decks.route) {
        composable(route = DeckNavGraphEntry.Decks.route) {
            ListCollectionScreen(
                onCreate = { navController.navigate(DeckNavGraphEntry.Create.route) },
                onEdit = { t: Deck -> navController.navigate(DeckNavGraphEntry.Edit.to(t.did)) }
            )
        }
        composable(route = DeckNavGraphEntry.Create.route) {
            CreateDeckScreen(back = { navController.popBackStack() })
        }
        composable(
            route = DeckNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("did") { type = NavType.LongType })
        ) { backStackEntry ->
            EditDeckScreen(
                tid = backStackEntry.arguments?.getLong("did")!!,
                back = { navController.popBackStack() })
        }
    }
}
