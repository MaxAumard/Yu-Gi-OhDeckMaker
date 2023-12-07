package fr.uha.aumard.deckbuilder.repository

import fr.uha.aumard.deckbuilder.database.CardDao
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.CardWithDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class CardRepository(private val cardDao: CardDao) {
    fun getAll(): Flow<List<CardWithDetails>> {
        return cardDao.getAllWithDetails()
    }

    fun getCardById(id: Long): Flow<Card?> {
        return cardDao.getCardById(id)
    }

    suspend fun create(card: Card): Long = withContext(Dispatchers.IO) {
        return@withContext cardDao.create(card)
    }

    suspend fun update(oldCard: Card, card: Card): Long = withContext(Dispatchers.IO) {
        return@withContext cardDao.update(card)
    }

    suspend fun upsert(card: Card): Long = withContext(Dispatchers.IO) {
        return@withContext cardDao.upsert(card)
    }

    suspend fun delete(card: Card) = withContext(Dispatchers.IO) {
        cardDao.delete(card)
    }

    fun getCardsPage(page: Int, pageSize: Int): Flow<List<Card>> {
        val offset = page * pageSize
        return cardDao.getCardsPage(limit = pageSize, offset = offset)
    }

    fun getCardsPageFiltered(
        page: Int,
        pageSize: Int,
        searchQuery: String,
    ): Flow<List<Card>> {
        return cardDao.getCardsPageFiltered(
            limit = pageSize,
            offset = page * pageSize,
            searchQuery = searchQuery,
        )
    }
}