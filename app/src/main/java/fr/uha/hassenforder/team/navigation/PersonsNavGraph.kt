package fr.uha.hassenforder.team.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Card
import fr.uha.hassenforder.team.ui.person.CreatePersonScreen
import fr.uha.hassenforder.team.ui.person.EditPersonScreen
import fr.uha.hassenforder.team.ui.person.ListPersonsScreen

private sealed class PersonNavGraphEntry(
    val route: String,
    val title: Int,
) {

    // to list all persons
    object Persons: PersonNavGraphEntry(
        route = "persons",
        title = R.string.action_cards,
    )

    // to create a person
    object Create: PersonNavGraphEntry(
        route = "person",
        title = R.string.action_person_create,
    )

    // to edit a person
    object Edit: PersonNavGraphEntry(
        route = "person/{cid}",
        title = R.string.action_person_edit,
    ) {
        fun to (pid : Long) : String {
            return route.replace("{cid}", pid.toString())
        }
    }

}

fun NavGraphBuilder.personsNavGraph (
    navController: NavHostController
) {
    navigation(PersonNavGraphEntry.Persons.route, BottomBarNavGraphEntry.Cards.route) {
        composable(route = PersonNavGraphEntry.Persons.route) {
            ListPersonsScreen(
                onCreate = { navController.navigate(PersonNavGraphEntry.Create.route) },
                onEdit = { p : Card -> navController.navigate(PersonNavGraphEntry.Edit.to(p.cid)) }
            )
        }
        composable(route = PersonNavGraphEntry.Create.route) {
            CreatePersonScreen (back = { navController.popBackStack() } )
        }
        composable(
            route = PersonNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("cid") { type = NavType.LongType })
        ) {
            backStackEntry ->
            EditPersonScreen(pid = backStackEntry.arguments?.getLong("cid")!!, back = { navController.popBackStack() } )
        }
    }
}
