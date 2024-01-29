package fr.uha.aumard.deckbuilder.ui.card

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.repository.CardRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ListCardsViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {
    private var currentPage = 0
    private val pageSize = 21
    private var isLoading = false

    // Filter and search properties
    private val currentSearchQuery = MutableStateFlow("")

    private val _cards = MutableStateFlow<List<Card>>(emptyList())
    val cards: StateFlow<List<Card>> = _cards.asStateFlow()

    init {
        loadMoreCards()
    }

    fun loadMoreCards() {
        if (isLoading) return

        isLoading = true
        viewModelScope.launch {
            val query = currentSearchQuery.value
            val newCards = if (query.isEmpty()) {
                repository.getCardsPage(currentPage, pageSize)
            } else {
                repository.getCardsPageFiltered(currentPage, pageSize, query)
            }
            newCards.collect { cards ->
                _cards.value = _cards.value + cards
                currentPage++
                isLoading = false
            }
        }
    }



    fun updateSearchQuery(query: String) {
        currentSearchQuery.value = query
        resetPaginationAndLoad()
    }

    private fun resetPaginationAndLoad() {
        currentPage = 0
        _cards.value = emptyList()
        isLoading = false
        loadMoreCards() // Use the current search query
    }


    val persons = repository.getAll()

}