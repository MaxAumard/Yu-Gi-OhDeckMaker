package fr.uha.aumard.deckbuilder.ui.deck

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Comparators
import fr.uha.aumard.deckbuilder.model.Deck
import fr.uha.aumard.deckbuilder.model.FullDeck
import fr.uha.aumard.deckbuilder.repository.DeckRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
@HiltViewModel
class DeckViewModel @Inject constructor(
    private val repository: DeckRepository
) : ViewModel() {

    var isLaunched: Boolean = false

    @Immutable
    sealed interface DeckState {
        data class Success(val deckBuilder: FullDeck) : DeckState
        object Loading : DeckState
        object Error : DeckState
    }

    data class FieldWrapper<T>(
        val current: T? = null,
        val errorId: Int? = null
    ) {
        companion object {
            fun buildName(newValue: String): FieldWrapper<String> {
                val errorId: Int? = DeckUIValidator.validateNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildStartDate(newValue: Date): FieldWrapper<Date> {
                val errorId: Int? = DeckUIValidator.validateStartDayChange(newValue)
                return FieldWrapper(newValue, errorId)
            }


            fun buildCards(state: DeckUIState, newValue: List<Card>?): FieldWrapper<List<Card>> {
                val errorId: Int? = DeckUIValidator.validateCardsChange(state, newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _startDayState = MutableStateFlow(FieldWrapper<Date>())
    private val _cardsState = MutableStateFlow(FieldWrapper<List<Card>>())

    private val _deckId: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialDeckState: StateFlow<DeckState> = _deckId
        .flatMapLatest { id -> repository.getDeckById(id) }
        .map { t ->
            if (t != null) {
                _nameState.emit(FieldWrapper.buildName(t.deck.name))
                _startDayState.emit(FieldWrapper.buildStartDate(t.deck.creationDate))
                _cardsState.emit(FieldWrapper.buildCards(uiState.value, t.cards))
                DeckState.Success(deckBuilder = t)
            } else {
                DeckState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), DeckState.Loading)

    private fun canAddCard(cardToAdd: Card): Boolean {
        val currentCards = _cardsState.value.current ?: return true
        val countOfThisCard = currentCards.count { it.cid == cardToAdd.cid }
        return countOfThisCard < 3
    }

    private val _addCardId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _delCardId: MutableSharedFlow<Long> = MutableSharedFlow(0)

    init {
        _addCardId
            .flatMapLatest { id -> repository.getCardById(id) }
            .map { cardToAdd ->
                if (cardToAdd != null && canAddCard(cardToAdd)) {
                    val updatedCards = (_cardsState.value.current ?: mutableListOf()).toMutableList()
                    updatedCards.add(cardToAdd)
                    _cardsState.emit(FieldWrapper.buildCards(uiState.value, updatedCards))
                }
            }
            .launchIn(viewModelScope)

        _delCardId
            .map {
                var mm: MutableList<Card> = mutableListOf()
                _cardsState.value.current?.forEach { c ->
                    if (c.cid != it) mm.add(c)
                }
                _cardsState.emit(FieldWrapper.buildCards(uiState.value, mm))
            }
            .launchIn(viewModelScope)
    }

    data class DeckUIState(
        val initialState: DeckState,
        val name: FieldWrapper<String>,
        val startDay: FieldWrapper<Date>,
        val card: FieldWrapper<List<Card>>,
    ) {
        private fun _isModified(): Boolean? {
            if (initialState !is DeckState.Success) return null
            if (name.current != initialState.deckBuilder.deck.name) return true
            if (name.current != initialState.deckBuilder.deck.name) return true
            if (startDay.current != initialState.deckBuilder.deck.creationDate) return true
            if (!Comparators.shallowEqualsListCards(
                    card.current,
                    initialState.deckBuilder.cards
                )
            ) return true
            return false
        }

        private fun _hasError(): Boolean? {
            if (name.errorId != null) return true
            if (startDay.errorId != null) return true
            if (card.errorId != null) return true
            return false
        }

        fun isModified(): Boolean {
            val isModified = _isModified()
            if (isModified == null) return false
            return isModified
        }

        fun isSavable(): Boolean {
            val hasError = _hasError()
            if (hasError == null) return false
            val isModified = _isModified()
            if (isModified == null) return false
            return ! hasError && isModified
        }
    }

    val uiState : StateFlow<DeckUIState> = combine (
        _initialDeckState,
        _nameState, _startDayState,
        _cardsState
    ) { initial, n, s, mm -> DeckUIState(initial, n, s, mm) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = DeckUIState(
            DeckState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String): UIEvent()
        data class DateChanged(val newValue: java.util.Date): UIEvent()
        data class CardAdded(val newValue: Long): UIEvent()
        data class CardDeleted(val newValue: Card): UIEvent()
    }

    data class DeckUICallback(
        val onEvent : (UIEvent) -> Unit,
    )

    val uiCallback = DeckUICallback(
        onEvent = {
            viewModelScope.launch {
                when (it) {
                    is UIEvent.NameChanged -> _nameState.emit(FieldWrapper.buildName(it.newValue))
                    is UIEvent.DateChanged -> _startDayState.emit(FieldWrapper.buildStartDate(it.newValue))
                    is UIEvent.CardAdded -> _addCardId.emit(it.newValue)
                    is UIEvent.CardDeleted -> _delCardId.emit(it.newValue.cid)
                }
            }
        }
    )

    fun edit(cid: Long) = viewModelScope.launch {
        _deckId.emit(cid)
    }

    fun create(deck: Deck) = viewModelScope.launch(Dispatchers.IO)  {
        val cid: Long = repository.createDeck(deck)
        _deckId.emit(cid)
    }

    fun save() = viewModelScope.launch(Dispatchers.IO) {
        if (_initialDeckState.value !is DeckState.Success) return@launch
        val oldDeck = _initialDeckState.value as DeckState.Success
        val deck = FullDeck (
            Deck(
                did = _deckId.value,
                name = _nameState.value.current!!,
                creationDate = _startDayState.value.current!!
            ),
            cards = _cardsState.value.current!!
        )
        isDeckSaved = true
        repository.saveDeck(oldDeck.deckBuilder, deck)
    }

    var isDeckSaved = false

    fun deleteUnsavedDeck() {
        if (!isDeckSaved && _deckId.value != 0L) {
            delete(_deckId.value)
        }
    }

    fun delete(did: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteDeckById(did)
        }
    }
}
