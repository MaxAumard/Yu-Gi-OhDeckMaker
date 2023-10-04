package fr.uha.hassenforder.team.ui.team

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.team.database.FeedDatabase
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.repository.TeamRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListTeamsViewModel @Inject constructor (
    private val repository: TeamRepository
): ViewModel() {

    val teams: Flow<List<Team>> = repository.getAll()

    fun delete(team: Team) = viewModelScope.launch {
        repository.delete(team)
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