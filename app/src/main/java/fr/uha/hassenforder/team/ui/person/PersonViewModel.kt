package fr.uha.hassenforder.team.ui.person

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.hassenforder.android.kotlin.combine
import fr.uha.hassenforder.team.model.Gender
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.repository.PersonRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.util.*
import javax.inject.Inject

@HiltViewModel
class PersonViewModel @Inject constructor (
    private val repository: PersonRepository
): ViewModel() {

    var isLaunched: Boolean = false

    @Immutable
    sealed interface PersonState {
        data class Success (val person : Person) : PersonState
        object Loading : PersonState
        object Error : PersonState
    }

    data class FieldWrapper<T> (
        val current: T?=null,
        val errorId: Int? = null
    ) {
        companion object {
            fun buildFirstname(state : PersonUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = PersonUIValidator.validateFirstnameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }
            fun buildLastname(state : PersonUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = PersonUIValidator.validateLastnameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }
            fun buildPhone(state : PersonUIState, newValue: String): FieldWrapper<String> {
                val errorId : Int? = PersonUIValidator.validatePhoneChange(newValue)
                return FieldWrapper(newValue, errorId)
            }
            fun buildGender(state : PersonUIState, newValue: Gender?): FieldWrapper<Gender?> {
                val errorId : Int? = PersonUIValidator.validateGenderChange(newValue)
                return FieldWrapper(newValue, errorId)
            }
            fun buildPicture(state : PersonUIState, newValue: Uri?): FieldWrapper<Uri?> {
                val errorId : Int? = PersonUIValidator.validatePictureChange(newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _firstnameState = MutableStateFlow(FieldWrapper<String>())
    private val _lastnameState = MutableStateFlow(FieldWrapper<String>())
    private val _phoneState = MutableStateFlow(FieldWrapper<String>())
    private val _genderState = MutableStateFlow(FieldWrapper<Gender?>())
    private val _pictureState = MutableStateFlow(FieldWrapper<Uri?>())

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialPersonState: StateFlow<PersonState> = _id
        .flatMapLatest { id -> repository.getPersonById(id) }
        .map {
            p -> if (p != null) {
                _firstnameState.emit(FieldWrapper.buildFirstname(uiState.value, p.firstname))
                _lastnameState.emit(FieldWrapper.buildLastname(uiState.value, p.lastname))
                _phoneState.emit(FieldWrapper.buildPhone(uiState.value, p.phone))
                _genderState.emit(FieldWrapper.buildGender(uiState.value, p.gender))
//                _pictureState.emit(FieldWrapper.buildPicture(uiState.value, p.picture))
                _pictureState.emit(FieldWrapper.buildPicture(uiState.value, null))
                PersonState.Success(person = p)
            } else {
                PersonState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PersonState.Loading)

    data class PersonUIState (
        val initialState: PersonState,
        val firstnameState : FieldWrapper<String>,
        val lastnameState : FieldWrapper<String>,
        val phoneState : FieldWrapper<String>,
        val genderState : FieldWrapper<Gender?>,
        val pictureState : FieldWrapper<Uri?>,
    ) {
        private fun _isModified (): Boolean? {
            if (initialState !is PersonState.Success) return null
            if (firstnameState.current != initialState.person.firstname) return true
            if (lastnameState.current != initialState.person.lastname) return true
            if (phoneState.current != initialState.person.phone) return true
            if (genderState.current != initialState.person.gender) return true
//            if (pictureState.current != initialState.person.picture) return true
            if (pictureState.current != null) return true
            return false
        }

        private fun _hasError (): Boolean? {
            if (firstnameState.errorId != null) return true
            if (lastnameState.errorId != null) return true
            if (phoneState.errorId != null) return true
            if (genderState.errorId != null) return true
            if (pictureState.errorId != null) return true
            return false
        }

        fun isModified() : Boolean {
            val isModified = _isModified()
            if (isModified == null) return false
            return isModified
        }

        fun isSavable (): Boolean {
            val hasError = _hasError()
            if (hasError == null) return false
            val isModified = _isModified()
            if (isModified == null) return false
            return ! hasError && isModified
        }
    }

    val uiState : StateFlow<PersonUIState> = combine (
        _initialPersonState, _firstnameState, _lastnameState, _phoneState, _genderState, _pictureState
    ) { i, f, l, p, g, a -> PersonUIState(i, f, l, p, g, a) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = PersonUIState(
            PersonState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class FirstnameChanged(val newValue: String): UIEvent()
        data class LastnameChanged(val newValue: String): UIEvent()
        data class PhoneChanged(val newValue: String): UIEvent()
        data class GenderChanged(val newValue: Gender): UIEvent()
        data class PictureChanged(val newValue: Uri?): UIEvent()
    }

    data class PersonUICallback (
        val onEvent : (UIEvent) -> Unit,
    )

    val uiCallback = PersonUICallback (
        onEvent = {
            viewModelScope.launch {
                when (it) {
                    is UIEvent.FirstnameChanged -> _firstnameState.emit(FieldWrapper.buildFirstname(uiState.value, it.newValue))
                    is UIEvent.LastnameChanged -> _lastnameState.emit(FieldWrapper.buildLastname(uiState.value, it.newValue))
                    is UIEvent.PhoneChanged -> _phoneState.emit(FieldWrapper.buildPhone(uiState.value, it.newValue))
                    is UIEvent.GenderChanged -> _genderState.emit(FieldWrapper.buildGender(uiState.value, it.newValue))
                    is UIEvent.PictureChanged -> _pictureState.emit(FieldWrapper.buildPicture(uiState.value, it.newValue))
                }
            }
        }
    )

    fun edit (pid : Long) = viewModelScope.launch {
        _id.emit(pid)
    }

    fun create(person: Person) = viewModelScope.launch {
        val pid : Long = repository.create(person)
        _id.emit(pid)
    }
/*
    fun save() = viewModelScope.launch {
        if (_initialPersonState.value !is PersonState.Success) return@launch
        val oldPerson = _initialPersonState.value as PersonState.Success
        val person = Person (
            _id.value,
            _firstnameState.value.current!!,
            _lastnameState.value.current!!,
            _phoneState.value.current!!,
            _genderState.value.current!!,
//            _pictureState.value.current
        )
        repository.update(oldPerson.person, person)
    }
*/
}
