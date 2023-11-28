package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.model.Card
import fr.uha.hassenforder.team.model.CardWithDetails
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class PersonRepository(private val personDao: PersonDao)
{
    fun getAll () : Flow<List<CardWithDetails>> {
        return personDao.getAllWithDetails()
    }

    fun getPersonById (id : Long) : Flow<Card?> {
        return personDao.getCardById(id)
    }

    suspend fun create (card : Card) : Long = withContext(Dispatchers.IO) {
        return@withContext personDao.create(card)
    }

    suspend fun update (oldCard : Card, card : Card) : Long = withContext(Dispatchers.IO) {
        return@withContext personDao.update(card)
    }

    suspend fun upsert (card : Card) : Long = withContext(Dispatchers.IO) {
        return@withContext personDao.upsert(card)
    }

    suspend fun delete (card : Card) = withContext(Dispatchers.IO) {
        personDao.delete(card)
    }

}