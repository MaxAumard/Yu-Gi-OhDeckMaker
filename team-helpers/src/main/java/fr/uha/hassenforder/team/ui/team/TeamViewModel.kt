package fr.uha.hassenforder.team.ui.team

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.kotlin.combine
import fr.uha.hassenforder.team.model.Comparators
import fr.uha.hassenforder.team.model.FullTeam
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.repository.TeamRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class TeamViewModel @Inject constructor (
    private val repository : TeamRepository
): ViewModel() {

    var isLaunched: Boolean = false

    @Immutable
    sealed interface TeamState {
        data class Success(val team: FullTeam) : TeamState
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

            fun buildLeader(state : TeamUIState, newValue: Person?): FieldWrapper<Person> {
                val errorId : Int? = TeamUIValidator.validateLeaderChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildMembers(state : TeamUIState, newValue: List<Person>?): FieldWrapper<List<Person>> {
                val errorId : Int? = TeamUIValidator.validateMembersChange(state, newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _startDayState = MutableStateFlow(FieldWrapper<Date>())
    private val _durationState = MutableStateFlow(FieldWrapper<Int>())
    private val _leaderState = MutableStateFlow(FieldWrapper<Person>())
    private val _membersState = MutableStateFlow(FieldWrapper<List<Person>>())

    private val _teamId: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialTeamState: StateFlow<TeamState> = _teamId
        .flatMapLatest { id -> repository.getTeamById(id) }
        .map { t ->
            if (t != null) {
                _nameState.emit(FieldWrapper.buildName(uiState.value, t.team.name))
                _startDayState.emit(FieldWrapper.buildStartDay(uiState.value, t.team.startDay))
                _durationState.emit(FieldWrapper.buildDuration(uiState.value, t.team.duration))
                _leaderState.emit(FieldWrapper.buildLeader(uiState.value, t.leader))
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
        _updateLeaderId
            .flatMapLatest { id -> repository.getPersonById(id) }
            .map { p ->
                if (p != null) {
                    _leaderState.emit(FieldWrapper.buildLeader(uiState.value, p))
                } else {
                    _leaderState.emit(FieldWrapper.buildLeader(uiState.value, null))
                }
            }
            .launchIn(viewModelScope)

        _addMemberId
            .flatMapLatest { id -> repository.getPersonById(id) }
            .map {
                    p -> if (p != null) {
                    var mm : MutableList<Person>? = _membersState.value.current?.toMutableList() ?: mutableListOf()
                    mm?.add(p)
                    _membersState.emit(FieldWrapper.buildMembers(uiState.value, mm))
                }
            }
            .launchIn(viewModelScope)

        _delMemberId
            .map {
                var mm: MutableList<Person> = mutableListOf()
                _membersState.value.current?.forEach { m ->
                    if (m.pid != it) mm.add(m)
                }
                _membersState.emit(FieldWrapper.buildMembers(uiState.value, mm))
            }
            .launchIn(viewModelScope)
    }

    data class TeamUIState(
        val initialState: TeamState,
        val name: FieldWrapper<String>,
        val startDay: FieldWrapper<Date>,
        val duration: FieldWrapper<Int>,
        val leader: FieldWrapper<Person>,
        val members: FieldWrapper<List<Person>>,
    ) {
        private fun _isModified(): Boolean? {
            if (initialState !is TeamState.Success) return null
            if (name.current != initialState.team.team.name) return true
            if (name.current != initialState.team.team.name) return true
            if (startDay.current != initialState.team.team.startDay) return true
            if (duration.current != initialState.team.team.duration) return true
            if (! Comparators.shallowEqualsPerson(leader.current, initialState.team.leader)) return true
            if (! Comparators.shallowEqualsListPersons(members.current, initialState.team.members)) return true
            return false
        }

        private fun _hasError(): Boolean? {
            if (name.errorId != null) return true
            if (startDay.errorId != null) return true
            if (duration.errorId != null) return true
            if (leader.errorId != null) return true
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
        _nameState, _startDayState, _durationState,
        _leaderState,
        _membersState
    ) { initial, n, s, d, l, mm -> TeamUIState(initial, n, s, d, l, mm) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = TeamUIState(
            TeamState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String): UIEvent()
        data class StartDayChanged(val newValue: Date): UIEvent()
        data class DurationChanged(val newValue: Int): UIEvent()
        data class LeaderChanged(val newValue: Long?): UIEvent()
        data class MemberAdded(val newValue: Long): UIEvent()
        data class MemberDeleted(val newValue: Person): UIEvent()
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
                    is UIEvent.DurationChanged -> _durationState.emit(FieldWrapper.buildDuration(uiState.value, it.newValue))
                    is UIEvent.LeaderChanged -> {
                        if (it.newValue != null) _updateLeaderId.emit(it.newValue)
                        else _leaderState.emit(FieldWrapper.buildLeader(uiState.value, null))
                    }
                    is UIEvent.MemberAdded -> _addMemberId.emit(it.newValue)
                    is UIEvent.MemberDeleted -> _delMemberId.emit(it.newValue.pid)
                }
            }
        }
    )

    fun edit(pid: Long) = viewModelScope.launch {
        _teamId.emit(pid)
    }

    fun create(team: Team) = viewModelScope.launch {
        val pid : Long = repository.createTeam(team)
        _teamId.emit(pid)
    }

    fun save() = viewModelScope.launch {
        if (_initialTeamState.value !is TeamState.Success) return@launch
        val oldTeam = _initialTeamState.value as TeamState.Success
        val team = FullTeam (
            Team (
                tid = _teamId.value,
                name = _nameState.value.current!!,
                startDay = _startDayState.value.current!!,
                duration = _durationState.value.current!!,
                leaderId = _leaderState.value.current?.pid ?: 0
            ),
            leader = _leaderState.value.current,
            members = _membersState.value.current!!
        )
        repository.saveTeam(oldTeam.team, team)
    }

}
