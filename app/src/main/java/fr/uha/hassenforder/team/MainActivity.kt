package fr.uha.hassenforder.team

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import fr.uha.hassenforder.team.database.TeamDatabase
import fr.uha.hassenforder.team.ui.person.CreatePersonScreen
import fr.uha.hassenforder.team.ui.theme.Team2023Theme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        TeamDatabase.create(applicationContext)
        setContent {
            Team2023Theme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    CreatePersonScreen()
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
    Team2023Theme {
        Greeting("Android")
    }
}