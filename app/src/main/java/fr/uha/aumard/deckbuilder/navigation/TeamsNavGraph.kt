package fr.uha.aumard.deckbuilder.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import androidx.navigation.navigation
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Deck
import fr.uha.aumard.deckbuilder.ui.deck.CreateTeamScreen
import fr.uha.aumard.deckbuilder.ui.deck.EditTeamScreen
import fr.uha.aumard.deckbuilder.ui.deck.ListTeamsScreen

private sealed class TeamNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all teams
    object Teams: TeamNavGraphEntry(
        route = "teams",
        title = R.string.action_decks,
    )

    // to create a team
    object Create: TeamNavGraphEntry(
        route = "team",
        title = R.string.action_team_create,
    )

    // to edit a team
    object Edit: TeamNavGraphEntry(
        route = "team/{did}",
        title = R.string.action_team_edit,
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
            ListTeamsScreen(
                onCreate = { navController.navigate(TeamNavGraphEntry.Create.route) },
                onEdit = { t : Deck -> navController.navigate(TeamNavGraphEntry.Edit.to(t.did)) }
            )
        }
        composable(route = TeamNavGraphEntry.Create.route) {
            CreateTeamScreen (back = { navController.popBackStack() } )
        }
        composable(
            route = TeamNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("did") { type = NavType.LongType })
        ) {
            backStackEntry ->
            EditTeamScreen(tid = backStackEntry.arguments?.getLong("did")!!, back = { navController.popBackStack() } )
        }
    }
}
