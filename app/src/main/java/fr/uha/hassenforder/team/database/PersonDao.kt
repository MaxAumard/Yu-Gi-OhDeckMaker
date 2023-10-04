package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.hassenforder.team.model.Person
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM persons")
    fun getAll () : Flow<List<Person>>

    @Query("SELECT * FROM persons WHERE pid = :id")
    fun getPersonById (id : Long) : Flow<Person?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    fun create (person : Person) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (person : Person) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (person : Person) : Long

    @Delete
    fun delete (person : Person)
}