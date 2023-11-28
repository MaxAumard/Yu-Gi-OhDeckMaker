package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.uha.hassenforder.team.model.FullDeck
import fr.uha.hassenforder.team.model.Deck
import fr.uha.hassenforder.team.model.DeckCardAssociation
import kotlinx.coroutines.flow.Flow

@Dao
interface TeamDao {

    @Query("SELECT * FROM decks")
    fun getAll () : Flow<List<Deck>>

    @Query("SELECT * FROM decks WHERE did = :id")
    @Transaction
    fun getTeamById (id : Long) : Flow<FullDeck?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (deck : Deck) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (deck : Deck) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (deck : Deck) : Long

    @Delete
    fun delete (deck : Deck)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeamPerson(member: DeckCardAssociation)

    @Delete
    suspend fun removeTeamPerson(member: DeckCardAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTeamPerson(members: List<DeckCardAssociation>)

    @Delete
    suspend fun removeTeamPerson(members: List<DeckCardAssociation>)

    @Query ("DELETE FROM tpas WHERE did = :tid")
    fun deleteTeamPersons(tid: Long)
}