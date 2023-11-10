package fr.uha.hassenforder.team.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.ui.team.CreateTeamScreen
import fr.uha.hassenforder.team.ui.team.EditTeamScreen
import fr.uha.hassenforder.team.ui.team.ListTeamsScreen

private sealed class TeamNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all teams
    object Teams: TeamNavGraphEntry(
        route = "teams",
        title = R.string.action_teams,
    )

    // to create a team
    object Create: TeamNavGraphEntry(
        route = "team",
        title = R.string.action_team_create,
    )

    // to edit a team
    object Edit: TeamNavGraphEntry(
        route = "team/{tid}",
        title = R.string.action_team_edit,
    ) {
        fun to (tid : Long) : String {
            return route.replace("{tid}", tid.toString())
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
                onEdit = { t : Team -> navController.navigate(TeamNavGraphEntry.Edit.to(t.tid)) }
            )
        }
        composable(route = TeamNavGraphEntry.Create.route) {
            CreateTeamScreen (back = { navController.popBackStack() } )
        }
        composable(
            route = TeamNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("tid") { type = NavType.LongType })
        ) {
            backStackEntry ->
            EditTeamScreen(tid = backStackEntry.arguments?.getLong("tid")!!, back = { navController.popBackStack() } )
        }
    }
}
