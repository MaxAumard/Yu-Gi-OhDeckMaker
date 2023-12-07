package fr.uha.aumard.deckbuilder.ui.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.deckbuilder.database.FeedDatabase
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.repository.CardRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ListCardsViewModel @Inject constructor(private val repository: CardRepository) : ViewModel() {
    private var currentPage = 0
    private val pageSize = 21
    private var isLoading = false

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    init {
        loadMoreCards()
    }

    fun loadMoreCards(searchQuery: String = "") {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            val newCards = if (searchQuery.isEmpty()) {
                repository.getCardsPage(currentPage, pageSize)
            } else {
                repository.getCardsPageFiltered(currentPage, pageSize, searchQuery)
            }
            newCards.collect { cards ->
                _cards.value = _cards.value + cards
                currentPage++
                isLoading = false
            }
        }
    }

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