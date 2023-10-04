package fr.uha.hassenforder.team.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import fr.uha.hassenforder.team.navigation.BottomBar
import fr.uha.hassenforder.team.navigation.BottomNavGraph
import fr.uha.hassenforder.team.ui.theme.Team2023Theme

@Composable
fun TeamAppScreen() {
    val navController = rememberNavController()

    Team2023Theme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                bottomBar = { BottomBar(navController = navController) }
            ) {
                innerPadding -> BottomNavGraph(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
