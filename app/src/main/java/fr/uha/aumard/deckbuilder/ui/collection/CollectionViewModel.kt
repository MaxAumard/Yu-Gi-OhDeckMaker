package fr.uha.aumard.deckbuilder.ui.collection

import androidx.compose.foundation.layout.PaddingValues
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.deckbuilder.database.CollectionCardDao
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.CollectionCard
import fr.uha.aumard.deckbuilder.model.CollectionCardAssociation
import fr.uha.aumard.deckbuilder.model.Extension
import fr.uha.aumard.deckbuilder.repository.CollectionCardRepository
import fr.uha.aumard.deckbuilder.repository.ExtensionRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CollectionViewModel @Inject constructor(
    private val collectionCardRepository: CollectionCardRepository,
    private val extensionRepository: ExtensionRepository,
    private val collectionCardDao: CollectionCardDao
) : ViewModel() {
    private val _selectedExtension = MutableStateFlow<Extension?>(null)
    val selectedExtension: StateFlow<Extension?> = _selectedExtension.asStateFlow()
    val extensions: Flow<List<Extension>> = extensionRepository.getExtensions()

    var isLaunched = false
    private val _uiState = MutableStateFlow<CollectionUIState>(CollectionUIState.Loading)
    val uiState: StateFlow<CollectionUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            val initialExtensions = extensionRepository.getExtensions().first()
            if (initialExtensions.isNotEmpty()) {
                _selectedExtension.value = initialExtensions.first()
            }
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
            val count = collectionCardDao.countCardInCollection(cardId)
            emit(count > 0)
        }.flowOn(Dispatchers.IO)
    }


    fun onEvent(event: UIEvent) {
        when (event) {
            is UIEvent.LoadCollection -> loadCollection()
            is UIEvent.CreateCollectionCard -> createCollectionCard(event.cardId)
            // Add other event handlers here
        }
    }

    // Load Collection Data
    private fun loadCollection() {
        viewModelScope.launch {
            _uiState.value = CollectionUIState.Loading
            try {
                val collectionData = collectionCardRepository.getAllCollectionCards().first()
                _uiState.value = CollectionUIState.Success(collectionData)
            } catch (e: Exception) {
                _uiState.value = CollectionUIState.Error
            }
        }
    }

    // Create a new Collection Card
    private fun createCollectionCard(cardId: Long) {
        viewModelScope.launch {
            // Implement the logic to create a new collection card
        }
    }

    // Define UI events for the Collection Screen
    sealed interface UIEvent {
        object LoadCollection : UIEvent
        data class CreateCollectionCard(val cardId: Long) : UIEvent
        // Add other events here
    }

    // Define UI states for the Collection Screen
    sealed interface CollectionUIState {
        object Loading : CollectionUIState
        object Error : CollectionUIState
        data class Success(val collectionData: List<CollectionCard>) : CollectionUIState
    }

}
