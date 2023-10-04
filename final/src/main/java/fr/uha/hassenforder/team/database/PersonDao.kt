package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.PersonWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT *  FROM persons")
    fun getAll(): Flow<List<Person>>

    @Transaction
    @Query("SELECT * "
            + ", (SELECT COUNT(*) FROM teams T WHERE T.leaderId = P.pid) AS teamLeadingCount"
            + ", (SELECT COUNT(*) FROM tpas TPA WHERE TPA.pid = P.pid) AS teamMemberCount"
            + " FROM persons AS P ")
    fun getAllDetailed(): Flow<List<PersonWithDetails>>

    @Query("SELECT * FROM persons WHERE pid = :id")
    fun getPersonById(id: Long?): Flow<Person?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(person: Person): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun update(person: Person): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(person: Person): Long

    @Delete
    suspend fun delete(person: Person)

}
