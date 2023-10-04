package fr.uha.hassenforder.team.ui.person

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.repository.PersonRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class PersonViewModel (private val repository: PersonRepository) : ViewModel() {

    private val _id : MutableStateFlow<Long> = MutableStateFlow(0)

    fun create (person : Person) = viewModelScope.launch {
        val pid : Long = repository.create(person)
        _id.emit(pid)
    }

    sealed interface PersonState {
        data class Success (val person: Person) : PersonState
        object Loading : PersonState
        object Error : PersonState
    }

    val personState : StateFlow<PersonState> = _id
        .flatMapLatest { id -> repository.getPersonById(id) }
        .map {
            p -> if (p != null) {
                PersonState.Success(p)
            } else {
                PersonState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PersonState.Loading)

}