package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM persons")
    fun getAll () : Flow<List<Person>>

    @Query("SELECT * " +
            ", (SELECT COUNT(*) FROM teams T WHERE T.leaderId = P.pid) AS leaderCount" +
            ", (SELECT COUNT(*) FROM tpas TPA WHERE TPA.pid = P.pid) AS memberCount" +
            " FROM persons AS P")
    fun getAllWithDetails () : Flow<List<PersonWithDetails>>

    @Query("SELECT * FROM persons WHERE pid = :id")
    fun getPersonById (id : Long) : Flow<Person?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (person : Person) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (person : Person) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (person : Person) : Long

    @Delete
    fun delete (person : Person)
}