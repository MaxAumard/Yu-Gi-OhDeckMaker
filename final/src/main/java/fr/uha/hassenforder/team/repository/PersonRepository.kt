package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow

class PersonRepository(
    private val personDao: PersonDao,
) {

    fun getAll(): Flow<List<Person>> {
        return personDao.getAll()
    }

    fun getAllDetailed(): Flow<List<PersonWithDetails>> {
        return personDao.getAllDetailed()
    }

    fun getPersonById(id: Long): Flow<Person?> {
        return personDao.getPersonById(id)
    }

    suspend fun create(person: Person): Long {
        return personDao.create(person)
    }

    suspend fun update(oldPerson: Person, newPerson: Person): Long {
        return personDao.update(newPerson)
    }

    suspend fun delete(person: Person) {
        personDao.delete(person)
    }

}
