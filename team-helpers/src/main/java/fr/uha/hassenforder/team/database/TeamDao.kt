package fr.uha.hassenforder.team.database

import androidx.room.*
import fr.uha.hassenforder.team.model.*
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Transaction
    @Query("SELECT * FROM teams AS T ")
    fun getAll(): Flow<List<Team>>

    @Transaction
    @Query("SELECT * FROM teams WHERE tid = :id")
    fun getFullTeamById(id: Long): Flow<FullTeam>

    @Transaction
    @Query("SELECT * FROM teams WHERE tid = :id")
    fun getTeamById(id: Long): Flow<Team>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun upsert(team: Team): Long

    @Delete
    suspend fun delete(team: Team)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeamPerson(member: TeamPersonAssociation)

    @Delete
    suspend fun removeTeamPerson(member: TeamPersonAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeamPerson(members: List<TeamPersonAssociation>)

    @Delete
    suspend fun removeTeamPerson(members: List<TeamPersonAssociation>)

    @Query ("DELETE FROM tpas WHERE tid = :tid")
    fun deleteTeamPersons(tid: Long)

}