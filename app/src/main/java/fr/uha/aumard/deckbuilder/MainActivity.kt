package fr.uha.aumard.deckbuilder

import DatabaseInitializer
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.lifecycleScope
import dagger.hilt.android.AndroidEntryPoint
import fr.uha.aumard.deckbuilder.ui.DeckBuilderAppScreen
import fr.uha.aumard.deckbuilder.ui.theme.DeckBuilderTheme
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        lifecycleScope.launch {
            DatabaseInitializer.initializeDatabase(applicationContext)
        }

        setContent {
            DeckBuilderTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    DeckBuilderAppScreen()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name !",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeckBuilderTheme {
        Greeting("Android")
    }
}