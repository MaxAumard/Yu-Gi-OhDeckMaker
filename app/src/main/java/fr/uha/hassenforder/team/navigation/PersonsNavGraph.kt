package fr.uha.hassenforder.team.navigation

import androidx.navigation.*
import androidx.navigation.compose.composable
import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.Person
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
        title = R.string.action_persons,
    )

    // to create a person
    object Create: PersonNavGraphEntry(
        route = "person",
        title = R.string.action_person_create,
    )

    // to edit a person
    object Edit: PersonNavGraphEntry(
        route = "person/{pid}",
        title = R.string.action_person_edit,
    ) {
        fun to (pid : Long) : String {
            return route.replace("{pid}", pid.toString())
        }
    }

}

fun NavGraphBuilder.personsNavGraph (
    navController: NavHostController
) {
    navigation(PersonNavGraphEntry.Persons.route, BottomBarNavGraphEntry.Persons.route) {
        composable(route = PersonNavGraphEntry.Persons.route) {
            ListPersonsScreen(
                onCreate = { navController.navigate(PersonNavGraphEntry.Create.route) },
                onEdit = { p : Person -> navController.navigate(PersonNavGraphEntry.Edit.to(p.pid)) }
            )
        }
        composable(route = PersonNavGraphEntry.Create.route) {
            CreatePersonScreen (back = { navController.popBackStack() } )
        }
        composable(
            route = PersonNavGraphEntry.Edit.route,
            arguments = listOf(navArgument("pid") { type = NavType.LongType })
        ) {
            backStackEntry ->
            EditPersonScreen(pid = backStackEntry.arguments?.getLong("pid")!!, back = { navController.popBackStack() } )
        }
    }
}
