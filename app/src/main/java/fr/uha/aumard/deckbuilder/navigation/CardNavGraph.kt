package fr.uha.aumard.deckbuilder.navigation

import CardInfoScreen
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.ui.card.ListCardsScreen

private sealed class CardNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all cards
    object Cards : CardNavGraphEntry(
        route = "cards",
        title = R.string.action_cards,
    )

    // to create a person
    object Create : CardNavGraphEntry(
        route = "card",
        title = R.string.action_card_create,
    )

    // to show a card
    object Show : CardNavGraphEntry(
        route = "card/{cid}",
        title = R.string.action_card_show,
    ) {
        fun to(cid: Long): String {
            return route.replace("{cid}", cid.toString())
        }
    }

}

fun NavGraphBuilder.personsNavGraph(
    navController: NavHostController
) {
    navigation(CardNavGraphEntry.Cards.route, BottomBarNavGraphEntry.Cards.route) {
        composable(route = CardNavGraphEntry.Cards.route) {
            ListCardsScreen(
                onCreate = { navController.navigate(CardNavGraphEntry.Create.route) },
                onCardClick = { c: Card -> navController.navigate(CardNavGraphEntry.Show.to(c.cid)) }
            )
        }
        composable(
            route = CardNavGraphEntry.Show.route,
            arguments = listOf(navArgument("cid") { type = NavType.LongType })
        ) { backStackEntry ->
            CardInfoScreen(
                cid = backStackEntry.arguments?.getLong("cid")!!
            )
        }
    }
}
