package fr.uha.aumard.deckbuilder.ui.deck

import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.deckbuilder.database.FeedDatabase
import fr.uha.aumard.deckbuilder.repository.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListDecksViewModel @Inject constructor(
    private val repository: DeckRepository
) : ViewModel() {
    fun feed() = viewModelScope.launch {
        withContext(Dispatchers.IO) {
            FeedDatabase().feedDecks();
        }
    }


    val decks = repository.getAll()
    fun getFirstCardImageUri(deckId: Long): Flow<Uri?> = flow {
        val firstCardUri = repository.getFirstCard(deckId)?.picture
        emit(Uri.parse(firstCardUri.toString()))
    }.flowOn(Dispatchers.IO)

    fun deleteDeckById(deckId: Long) {
        viewModelScope.launch {
            repository.deleteDeckById(deckId)
        }
    }


}
