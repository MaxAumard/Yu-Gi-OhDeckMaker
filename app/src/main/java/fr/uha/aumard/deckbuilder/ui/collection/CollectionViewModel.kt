package fr.uha.aumard.deckbuilder.ui.collection

import androidx.compose.runtime.Immutable
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.CollectionCard
import fr.uha.aumard.deckbuilder.model.CollectionCardAssociation
import fr.uha.aumard.deckbuilder.model.Condition
import fr.uha.aumard.deckbuilder.model.Extension
import fr.uha.aumard.deckbuilder.model.Language
import fr.uha.aumard.deckbuilder.model.Rarity
import fr.uha.aumard.deckbuilder.repository.CollectionCardRepository
import fr.uha.aumard.deckbuilder.repository.ExtensionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionCardRepository: CollectionCardRepository,
    private val extensionRepository: ExtensionRepository,

    ) : ViewModel() {
    private val _selectedExtension = MutableStateFlow<Extension?>(null)

    val selectedExtension: StateFlow<Extension?> = _selectedExtension.asStateFlow()
    val extensions: Flow<List<Extension>> = extensionRepository.getExtensions()
    var isLaunched: Boolean = false

    init {
        viewModelScope.launch {
            val initialExtensions = extensionRepository.getExtensions().first()
            if (initialExtensions.isNotEmpty()) {
                _selectedExtension.value = initialExtensions.first()
            }
        }
    }

    @Immutable
    sealed interface CollectionCardState {

        object Loading : CollectionCardState
        object Error : CollectionCardState
        data class Success(val collectionData: CollectionCard) : CollectionCardState
    }

    data class FieldWrapper<T>(
        val current: T? = null,
        val errorId: Int? = null
    ) {
        companion object {

            fun buildCondition(newValue: Condition): FieldWrapper<Condition> {
                val errorId: Int? = CollectionUIValidator.validateConditionChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildLanguage(newValue: Language): FieldWrapper<Language> {
                val errorId: Int? = CollectionUIValidator.validateLanguageChange(newValue)
                return FieldWrapper(newValue, errorId)
            }

            fun buildRarity(newValue: Rarity): FieldWrapper<Rarity> {
                val errorId: Int? = CollectionUIValidator.validateRarityChange(newValue)
                return FieldWrapper(newValue, errorId)
            }
        }
    }

    private val _conditionState = MutableStateFlow(FieldWrapper<Condition>())
    private val _languageState = MutableStateFlow(FieldWrapper<Language>())
    private val _rarityState = MutableStateFlow(FieldWrapper<Rarity>())

    private val _ccId: MutableStateFlow<Long> = MutableStateFlow(0)


    @OptIn(ExperimentalCoroutinesApi::class)
    private val _initialCollectionCardState: StateFlow<CollectionCardState> = _ccId
        .flatMapLatest { id -> collectionCardRepository.getCollectionCard(id) }
        .map { cc: CollectionCard? ->
            if (cc != null) {
                _conditionState.emit(FieldWrapper.buildCondition(cc.condition))
                _languageState.emit(FieldWrapper.buildLanguage(cc.language))
                _rarityState.emit(FieldWrapper.buildRarity(cc.rarity))
                CollectionCardState.Success(cc)
            } else {
                CollectionCardState.Error
            }
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), CollectionCardState.Loading)

    data class CollectionUIState(
        val initialState: CollectionCardState,
        val condition: FieldWrapper<Condition>,
        val language: FieldWrapper<Language>,
        val rarity: FieldWrapper<Rarity>,
    ) {
        private fun _isModified(): Boolean? {
            if (initialState !is CollectionCardState.Success) return null
            if (condition.current != initialState.collectionData.condition) return true
            if (language.current != initialState.collectionData.language) return true
            if (rarity.current != initialState.collectionData.rarity) return true
            return false
        }

        private fun _hasError(): Boolean {
            if (condition.errorId != null) return true
            if (language.errorId != null) return true
            if (rarity.errorId != null) return true
            return false
        }

        fun isModified(): Boolean {
            val isModified = _isModified()
            if (isModified == null) return false
            return isModified
        }

        fun isSavable(): Boolean {
            val hasError = _hasError()
            val isModified = _isModified() ?: return false
            return !hasError && isModified
        }
    }


    val uiState: StateFlow<CollectionUIState> = combine(
        _initialCollectionCardState,
        _conditionState, _languageState, _rarityState
    ) { initial, n, s, mm -> CollectionUIState(initial, n, s, mm) }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = CollectionUIState(
            CollectionCardState.Loading,
            FieldWrapper(),
            FieldWrapper(),
            FieldWrapper(),
        )
    )

    sealed class UIEvent {
        data class ConditionChanged(val newValue: Condition) : UIEvent()
        data class LanguageChanged(val newValue: Language) : UIEvent()
        data class RarityChanged(val newValue: Rarity) : UIEvent()
    }

    data class CollectionUICallback(
        val onEvent: (UIEvent) -> Unit,
    )

    val uiCallback = CollectionUICallback(
        onEvent = {
            viewModelScope.launch {
                when (it) {
                    is UIEvent.ConditionChanged -> _conditionState.emit(
                        FieldWrapper.buildCondition(
                            it.newValue
                        )
                    )

                    is UIEvent.LanguageChanged -> _languageState.emit(
                        FieldWrapper.buildLanguage(
                            it.newValue
                        )
                    )

                    is UIEvent.RarityChanged -> _rarityState.emit(
                        FieldWrapper.buildRarity(
                            it.newValue
                        )
                    )
                }
            }
        }
    )

    fun edit(ccid: Long) = viewModelScope.launch {
        _ccId.emit(ccid)
    }

    fun create(cardId: Long, collectionCard: CollectionCard) =
        viewModelScope.launch(Dispatchers.IO) {
            val newCcid = collectionCardRepository.createCollectionCard(collectionCard)
            val association = CollectionCardAssociation(cardId, newCcid)
            collectionCardRepository.createAssociation(association)
            _ccId.emit(newCcid)
        }

    fun save() = viewModelScope.launch(Dispatchers.IO) {
        if (_initialCollectionCardState.value !is CollectionCardState.Success) return@launch
        val collectionCard = CollectionCard(
            ccid = _ccId.value,
            condition = _conditionState.value.current!!,
            language = _languageState.value.current!!,
            rarity = _rarityState.value.current!!
        )
        collectionCardRepository.saveCollectionCard(collectionCard)
    }

    fun delete(ccid: Long) {
        viewModelScope.launch(Dispatchers.IO) {
            collectionCardRepository.deleteCollectionCard(ccid)
            _selectedExtension.value?.let { selectExtension(it) }
        }
    }

    fun selectExtension(extension: Extension) {
        _selectedExtension.value = extension
    }

    fun getCardsInExtension(setName: String): Flow<List<Card>> {
        return extensionRepository.getCardsInExtension(setName)
    }

    fun isCardInCollection(cardId: Long): Flow<Boolean> {
        return flow {
            val count = collectionCardRepository.countCardInCollection(cardId)
            emit(count > 0)
        }.flowOn(Dispatchers.IO)
    }

    fun getCollectionCardId(cardId: Long): Flow<Long?> {
        return collectionCardRepository.getCollectionCardId(cardId)
    }


}
