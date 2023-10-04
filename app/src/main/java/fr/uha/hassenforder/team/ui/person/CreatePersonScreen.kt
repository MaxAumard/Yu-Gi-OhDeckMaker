package fr.uha.hassenforder.team.ui.person

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person

@Composable
fun CreatePersonScreen () {
    Scaffold (

    )
    {
        Column (
            modifier = Modifier.padding(it)
        ) {
            SuccessPersonScreen(Person(0, "michel", "hassenforder", "0123456789", Gender.BOY))
        }
    }
}

