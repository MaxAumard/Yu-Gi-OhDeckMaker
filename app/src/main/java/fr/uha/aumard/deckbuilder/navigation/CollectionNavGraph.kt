package fr.uha.aumard.deckbuilder.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.navigation
import fr.uha.aumard.deckbuilder.ui.collection.CollectionScreen

private sealed class CollectionNavGraphEntry(
    val route: String,
) {

    object Collection : CollectionNavGraphEntry(
        route = "tl_collection",
    )

}

fun NavGraphBuilder.collectionNavGraph(navController: NavHostController) {
    navigation(startDestination = "collection", route = BottomBarNavGraphEntry.Collection.route) {
        composable(route = "collection") {
            CollectionScreen(
                onCardClick = { ccid: Long ->
                    navController.navigate("create_collection_card/$ccid")
                }

            )

        }
    }
}
