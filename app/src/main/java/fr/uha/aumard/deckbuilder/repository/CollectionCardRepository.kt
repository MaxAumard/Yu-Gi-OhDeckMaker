package fr.uha.aumard.deckbuilder.repository

import fr.uha.aumard.deckbuilder.database.CardDao
import fr.uha.aumard.deckbuilder.database.CollectionCardDao
import fr.uha.aumard.deckbuilder.database.ExtensionDao
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.CollectionCard
import fr.uha.aumard.deckbuilder.model.CollectionCardAssociation
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.withContext

class CollectionCardRepository(
    private val collectionCardDao: CollectionCardDao,

) {
    suspend fun addCardToCollection(collectionCard: CollectionCard) {
        withContext(Dispatchers.IO) {
        }
    }
    suspend fun editCardInCollection(collectionCard: CollectionCard) {
        withContext(Dispatchers.IO) {
        }
    }

    suspend fun updateCollectionCard(card: CollectionCard) {
        collectionCardDao.update(card)
    }

    fun getAllCollectionCards(): Flow<List<CollectionCard>> {
        return collectionCardDao.getAll()
    }
}
