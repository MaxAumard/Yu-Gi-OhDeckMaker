package fr.uha.hassenforder.team.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.database.FeedDatabase
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails
import fr.uha.hassenforder.team.repository.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListPersonsViewModel @Inject constructor (
    private val repository: PersonRepository
): ViewModel() {

    val persons: Flow<List<PersonWithDetails>> = repository.getAllDetailed()

    fun delete(person: Person) = viewModelScope.launch {
        repository.delete(person)
    }

    fun populateDatabase () = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase().populate()
        }
    }

    fun clearDatabase () = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase().clear()
        }
    }

}