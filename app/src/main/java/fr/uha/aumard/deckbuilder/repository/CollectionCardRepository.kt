package fr.uha.aumard.deckbuilder.repository

import fr.uha.aumard.deckbuilder.database.CollectionCardDao
import fr.uha.aumard.deckbuilder.model.CollectionCard
import fr.uha.aumard.deckbuilder.model.CollectionCardAssociation
import kotlinx.coroutines.flow.Flow

class CollectionCardRepository(
    private val collectionCardDao: CollectionCardDao,

    ) {

    fun getCollectionCard(id: Long): Flow<CollectionCard?> {
        return collectionCardDao.getCollectionCardById(id)
    }

    fun createCollectionCard(collectionCard: CollectionCard): Long {
        return collectionCardDao.upsert(collectionCard)
    }

    fun saveCollectionCard(collectionCard: CollectionCard) {
        collectionCardDao.upsert(collectionCard)
    }

    fun createAssociation(association: CollectionCardAssociation) {
        collectionCardDao.insertAssociation(association)
    }

    fun getCollectionCardId(cid: Long): Flow<Long?> {
        return collectionCardDao.getCollectionCardId(cid)
    }

    fun countCardInCollection(cardId: Long): Int {
        return collectionCardDao.countCardInCollection(cardId)
    }


}
