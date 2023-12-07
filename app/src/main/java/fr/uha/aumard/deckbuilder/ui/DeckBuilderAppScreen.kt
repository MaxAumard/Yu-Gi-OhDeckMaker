package fr.uha.aumard.deckbuilder.ui

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.rememberNavController
import fr.uha.aumard.deckbuilder.navigation.BottomBar
import fr.uha.aumard.deckbuilder.navigation.BottomNavGraph
import fr.uha.aumard.deckbuilder.ui.theme.DeckBuilderTheme

@Composable
fun DeckBuilderAppScreen() {
    val navController = rememberNavController()

    DeckBuilderTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
        ) {
            Scaffold(
                bottomBar = { BottomBar(navController = navController) }
            ) { innerPadding ->
                BottomNavGraph(
                    navController = navController,
                    modifier = Modifier.padding(innerPadding)
                )
            }
        }
    }
}
