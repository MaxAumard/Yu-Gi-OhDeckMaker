package fr.uha.hassenforder.team.ui.person

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.repository.PersonRepository
import javax.inject.Inject

@HiltViewModel
class ListPersonsViewModel @Inject constructor (
    private val repository: PersonRepository
) : ViewModel() {

    val persons = repository.getAll()

}
