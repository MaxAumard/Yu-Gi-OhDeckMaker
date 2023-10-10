package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.uha.hassenforder.team.model.FullTeam
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.Team
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Query("SELECT * FROM teams")
    fun getAll () : Flow<List<Team>>

    @Query("SELECT * FROM teams WHERE tid = :id")
    @Transaction
    fun getTeamById (id : Long) : Flow<FullTeam?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (team : Team) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (team : Team) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (team : Team) : Long

    @Delete
    fun delete (team : Team)
}