package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.model.Person
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class PersonRepository(private val personDao: PersonDao)
{
    fun getAll () : Flow<List<Person>> {
        return personDao.getAll()
    }

    fun getPersonById (id : Long) : Flow<Person?> {
        return personDao.getPersonById(id)
    }

    suspend fun create (person : Person) : Long = withContext(Dispatchers.IO) {
        return@withContext personDao.create(person)
    }

    suspend fun update (oldPerson : Person, person : Person) : Long = withContext(Dispatchers.IO) {
        return@withContext personDao.update(person)
    }

    suspend fun upsert (person : Person) : Long = withContext(Dispatchers.IO) {
        return@withContext personDao.upsert(person)
    }

    suspend fun delete (person : Person) = withContext(Dispatchers.IO) {
        personDao.delete(person)
    }

}