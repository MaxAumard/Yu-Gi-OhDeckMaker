package fr.uha.aumard.deckbuilder.ui.deck

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import fr.uha.aumard.deckbuilder.database.CardDao
import fr.uha.aumard.deckbuilder.model.Card
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class CardPickerViewModel @Inject constructor(dao: CardDao) : ViewModel() {
    val cards: Flow<List<Card>> = dao.getAll()
}
