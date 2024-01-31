package fr.uha.aumard.deckbuilder.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.uha.aumard.deckbuilder.ui.collection.CollectionScreen
import fr.uha.aumard.deckbuilder.ui.collection.CreateCollectionCardScreen
import fr.uha.aumard.deckbuilder.ui.collection.EditCollectionCardScreen

private sealed class CollectionNavGraphEntry(
    val route: String,
) {

    object Collection : CollectionNavGraphEntry(
        route = "collection",
    )

    object Create : CollectionNavGraphEntry(
        route = "addCollectionCardBuilder/{cardId}",
    ) {
        fun to(cardId: Long): String {
            return route.replace("{cardId}", cardId.toString())
        }
    }

    object Edit : CollectionNavGraphEntry(
        route = "collectionCardBuilder/{ccid}",
    ) {
        fun to(ccid: Long): String {
            return route.replace("{ccid}", ccid.toString())
        }
    }
}

fun NavGraphBuilder.collectionNavGraph(navController: NavHostController) {
    navigation(
        startDestination = CollectionNavGraphEntry.Collection.route,
        route = BottomBarNavGraphEntry.Collection.route
    ) {
        composable(route = CollectionNavGraphEntry.Collection.route) {
            CollectionScreen(
                onCreate = { cardId: Long ->
                    navController.navigate(CollectionNavGraphEntry.Create.to(cardId))
                },
                onEdit = { ccid: Long ->
                    navController.navigate(CollectionNavGraphEntry.Edit.to(ccid))
                }
            )
        }
        composable(
            route = CollectionNavGraphEntry.Create.route,
            arguments = listOf(navArgument("cardId") { type = NavType.LongType })
        ) { backStackEntry ->
            val cardId = backStackEntry.arguments?.getLong("cardId") ?: throw IllegalStateException(
                "Card ID is required"
            )
            CreateCollectionCardScreen(cardId = cardId, back = { navController.popBackStack() })
        }
        composable(
            route = CollectionNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("ccid") { type = NavType.LongType })
        ) { backStackEntry ->
            EditCollectionCardScreen(
                ccid = backStackEntry.arguments?.getLong("ccid")!!,
                back = { navController.popBackStack() })
        }
    }
}
