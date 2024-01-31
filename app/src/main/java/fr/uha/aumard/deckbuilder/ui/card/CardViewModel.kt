package fr.uha.aumard.deckbuilder.ui.card

import android.net.Uri
import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.android.kotlin.combine
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Type
import fr.uha.aumard.deckbuilder.repository.CardRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import java.util.*
import javax.inject.Inject

@HiltViewModel
class CardViewModel @Inject constructor(
    private val repository: CardRepository
) : ViewModel() {

    var isLaunched: Boolean = false

    @Immutable
    sealed interface CardState {
        data class Success(val card: Card) : CardState
        object Loading : CardState
        object Error : CardState
    }

    data class FieldWrapper<T>(
        val current: T? = null,
        val errorId: Int? = null
    ) {
        companion object {
            fun buildName(state: CardUIState, newValue: String): FieldWrapper<String> {
                val errorId: Int? = CardUIValidator.validateNameChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildDescription(state: CardUIState, newValue: String): FieldWrapper<String> {
                val errorId: Int? = CardUIValidator.validateDescriptionChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildType(state: CardUIState, newValue: Type?): FieldWrapper<Type?> {
                val errorId: Int? = CardUIValidator.validateTypeChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildPicture(state: CardUIState, newValue: Uri?): FieldWrapper<Uri?> {
                val errorId: Int? = CardUIValidator.validatePictureChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildLevel(state: CardUIState, newValue: String): FieldWrapper<String?> {
                val errorId: Int? = CardUIValidator.validateLevelChange(newValue)
                return FieldWrapper(newValue.takeIf { errorId == null }, errorId)
            }
        }
    }

    private val _nameState = MutableStateFlow(FieldWrapper<String>())
    private val _descriptionState = MutableStateFlow(FieldWrapper<String>())
    private val _levelState = MutableStateFlow(FieldWrapper<String?>())
    private val _typeState = MutableStateFlow(FieldWrapper<Type?>())
    private val _isExtraDeckState = MutableStateFlow(FieldWrapper<Boolean>())
    private val _pictureState = MutableStateFlow(FieldWrapper<Uri?>())
    private val _attackState = MutableStateFlow(FieldWrapper<String?>())
    private val _defenseState = MutableStateFlow(FieldWrapper<String?>())

    private val _id: MutableStateFlow<Long> = MutableStateFlow(0)

    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialCardState: StateFlow<CardState> = _id
        .flatMapLatest { id -> repository.getCardById(id) }
        .map { c ->
            if (c != null) {
                _nameState.emit(FieldWrapper.buildName(uiState.value, c.name))
                _descriptionState.emit(FieldWrapper.buildDescription(uiState.value, c.description))
                _typeState.emit(FieldWrapper.buildType(uiState.value, c.type))
                _pictureState.emit(FieldWrapper.buildPicture(uiState.value, c.picture))
                _isExtraDeckState.emit(FieldWrapper(c.isExtraDeck))
                _attackState.emit(FieldWrapper(c.attack))
                _defenseState.emit(FieldWrapper(c.defense))
                _levelState.emit(FieldWrapper(c.level))
                CardState.Success(card = c)
            } else {
                CardState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CardState.Loading)

    data class CardUIState(
        val initialState: CardState,
        val nameState: FieldWrapper<String>,
        val descriptionState: FieldWrapper<String>,
        val levelState: FieldWrapper<String?>,
        val typeState: FieldWrapper<Type?>,
        val pictureState: FieldWrapper<Uri?>,
        val isExtraDeckState: FieldWrapper<Boolean>,
        val attackState: FieldWrapper<String?>,
        val defenseState: FieldWrapper<String?>,
    ) {
        private fun _isModified(): Boolean? {
            if (initialState !is CardState.Success) return null
            if (nameState.current != initialState.card.name) return true
            if (descriptionState.current != initialState.card.description) return true
            if (levelState.current != initialState.card.level) return true
            if (typeState.current != initialState.card.type) return true
            if (pictureState.current != initialState.card.picture) return true
            if (isExtraDeckState.current != initialState.card.isExtraDeck) return true
            if (attackState.current != initialState.card.attack) return true
            if (defenseState.current != initialState.card.defense) return true
            return false
        }

        private fun _hasError(): Boolean? {
            if (nameState.errorId != null) return true
            if (descriptionState.errorId != null) return true
            if (levelState.errorId != null) return true
            if (typeState.errorId != null) return true
            if (pictureState.errorId != null) return true
            if (isExtraDeckState.errorId != null) return true
            if (attackState.errorId != null) return true
            if (defenseState.errorId != null) return true
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
            return !hasError && isModified
        }
    }

    val uiState: StateFlow<CardUIState> = combine(
        _initialCardState,
        _nameState,
        _descriptionState,
        _levelState,
        _typeState,
        _pictureState,
        _isExtraDeckState,
        _attackState,
        _defenseState,
    ) { i, f, l, p, g, a, isExtraDeck, attack, defense ->
        CardUIState(i, f, l, p, g, a, isExtraDeck, attack, defense)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CardUIState(
            CardState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class NameChanged(val newValue: String) : UIEvent()
        data class DescriptionChanged(val newValue: String) : UIEvent()
        data class LevelChanged(val newValue: String) : UIEvent()
        data class TypeChanged(val newValue: Type) : UIEvent()
        data class PictureChanged(val newValue: Uri?) : UIEvent()
    }

    data class CardUICallback(
        val onEvent: (UIEvent) -> Unit,
    )


    fun getCardById(cid: Long): Flow<Card?> {
        return repository.getCardById(cid)
    }

}
