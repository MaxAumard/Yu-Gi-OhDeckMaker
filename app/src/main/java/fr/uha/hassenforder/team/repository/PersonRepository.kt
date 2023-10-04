package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.model.Person
import kotlinx.coroutines.flow.Flow

class PersonRepository(private val personDao: PersonDao)
{
    fun getAll () : Flow<List<Person>> {
        return personDao.getAll()
    }

    fun getPersonById (id : Long) : Flow<Person?> {
        return personDao.getPersonById(id)
    }

    fun create (person : Person) : Long {
        return personDao.create(person)
    }

    fun update (person : Person) : Long {
        return personDao.update(person)
    }

    fun upsert (person : Person) : Long {
        return personDao.upsert(person)
    }

    fun delete (person : Person) {
        personDao.delete(person)
    }

}