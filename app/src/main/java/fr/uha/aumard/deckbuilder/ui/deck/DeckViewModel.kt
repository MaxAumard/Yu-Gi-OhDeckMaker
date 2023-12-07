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
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class DeckViewModel @Inject constructor(
    private val repository: DeckRepository
) : ViewModel() {

    var isLaunched: Boolean = false

    @Immutable
    sealed interface TeamState {
        data class Success(val team: FullDeck) : TeamState
        object Loading : TeamState
        object Error : TeamState
    }

    data class FieldWrapper<T>(
        val current: T? = null,
        val errorId: Int? = null
    ) {
        companion object {
            fun buildName(state : TeamUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = TeamUIValidator.validateNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildStartDay(state : TeamUIState, newValue: Date): FieldWrapper<Date> {
                val errorId : Int? = TeamUIValidator.validateStartDayChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildDuration(state : TeamUIState, newValue: Int): FieldWrapper<Int> {
                val errorId : Int? = TeamUIValidator.validateDurationChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildLeader(state : TeamUIState, newValue: Card?): FieldWrapper<Card> {
                val errorId : Int? = TeamUIValidator.validateLeaderChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildMembers(state : TeamUIState, newValue: List<Card>?): FieldWrapper<List<Card>> {
                val errorId : Int? = TeamUIValidator.validateMembersChange(state, newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _startDayState = MutableStateFlow(FieldWrapper<Date>())
    private val _membersState = MutableStateFlow(FieldWrapper<List<Card>>())

    private val _teamId: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialTeamState: StateFlow<TeamState> = _teamId
        .flatMapLatest { id -> repository.getDeckById(id) }
        .map { t ->
            if (t != null) {
                _nameState.emit(FieldWrapper.buildName(uiState.value, t.deck.name))
                _startDayState.emit(FieldWrapper.buildStartDay(uiState.value, t.deck.creationDate))
                _membersState.emit(FieldWrapper.buildMembers(uiState.value, t.members))
                TeamState.Success(team = t)
            } else {
                TeamState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), TeamState.Loading)

    private val _updateLeaderId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _addMemberId: MutableSharedFlow<Long> = MutableSharedFlow(0)
    private val _delMemberId: MutableSharedFlow<Long> = MutableSharedFlow(0)

    init {
        _addMemberId
            .flatMapLatest { id -> repository.getPersonById(id) }
            .map {
                    p -> if (p != null) {
                    var mm : MutableList<Card>? = _membersState.value.current?.toMutableList() ?: mutableListOf()
                    mm?.add(p)
                    _membersState.emit(FieldWrapper.buildMembers(uiState.value, mm))
                }
            }
            .launchIn(viewModelScope)

        _delMemberId
            .map {
                var mm: MutableList<Card> = mutableListOf()
                _membersState.value.current?.forEach { m ->
                    if (m.cid != it) mm.add(m)
                }
                _membersState.emit(FieldWrapper.buildMembers(uiState.value, mm))
            }
            .launchIn(viewModelScope)
    }

    data class TeamUIState(
        val initialState: TeamState,
        val name: FieldWrapper<String>,
        val startDay: FieldWrapper<Date>,
        val members: FieldWrapper<List<Card>>,
    ) {
        private fun _isModified(): Boolean? {
            if (initialState !is TeamState.Success) return null
            if (name.current != initialState.team.deck.name) return true
            if (name.current != initialState.team.deck.name) return true
            if (startDay.current != initialState.team.deck.creationDate) return true
            if (!Comparators.shallowEqualsListCards(
                    members.current,
                    initialState.team.members
                )
            ) return true
            return false
        }

        private fun _hasError(): Boolean? {
            if (name.errorId != null) return true
            if (startDay.errorId != null) return true
            if (members.errorId != null) return true
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

    val uiState : StateFlow<TeamUIState> = combine (
        _initialTeamState,
        _nameState, _startDayState,
        _membersState
    ) { initial, n, s, mm -> TeamUIState(initial, n, s, mm) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TeamUIState(
            TeamState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String): UIEvent()
        data class StartDayChanged(val newValue: Date): UIEvent()
        data class MemberAdded(val newValue: Long): UIEvent()
        data class MemberDeleted(val newValue: Card): UIEvent()
    }

    data class TeamUICallback(
        val onEvent : (UIEvent) -> Unit,
    )

    val uiCallback = TeamUICallback(
        onEvent = {
            viewModelScope.launch {
                when (it) {
                    is UIEvent.NameChanged -> _nameState.emit(FieldWrapper.buildName(uiState.value, it.newValue))
                    is UIEvent.StartDayChanged -> _startDayState.emit(FieldWrapper.buildStartDay(uiState.value, it.newValue))
                    is UIEvent.MemberAdded -> _addMemberId.emit(it.newValue)
                    is UIEvent.MemberDeleted -> _delMemberId.emit(it.newValue.cid)
                }
            }
        }
    )

    fun edit(pid: Long) = viewModelScope.launch {
        _teamId.emit(pid)
    }

    fun create(deck: Deck) = viewModelScope.launch {
        val pid: Long = repository.createDeck(deck)
        _teamId.emit(pid)
    }

    fun save() = viewModelScope.launch {
        if (_initialTeamState.value !is TeamState.Success) return@launch
        val oldTeam = _initialTeamState.value as TeamState.Success
        val deck = FullDeck (
            Deck (
                did = _teamId.value,
                name = _nameState.value.current!!,
                creationDate = _startDayState.value.current!!
            ),
            members = _membersState.value.current!!
        )
        repository.saveDeck(oldTeam.team, deck)
    }

}