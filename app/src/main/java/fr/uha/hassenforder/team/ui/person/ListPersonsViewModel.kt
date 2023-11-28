package fr.uha.hassenforder.team.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.database.FeedDatabase
import fr.uha.hassenforder.team.model.Card
import fr.uha.hassenforder.team.repository.PersonRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListPersonsViewModel @Inject constructor (
    private val repository: PersonRepository
) : ViewModel() {
    fun feed() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase().populate()
        }
    }

    fun clean() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase().clear()
        }
    }
    fun delete(card: Card) = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            repository.delete(card)
        }
    }

    val persons = repository.getAll()

}
