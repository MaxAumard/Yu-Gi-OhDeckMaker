package fr.uha.aumard.deckbuilder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.uha.aumard.deckbuilder.model.Deck
import fr.uha.aumard.deckbuilder.model.DeckCardAssociation
import fr.uha.aumard.deckbuilder.model.FullDeck
import kotlinx.coroutines.flow.Flow

@Dao
interface DeckDao {

    @Query("SELECT * FROM decks")
    fun getAll(): Flow<List<Deck>>

    @Query("SELECT * FROM decks WHERE did = :id")
    @Transaction
    fun getDeckById(id: Long): Flow<FullDeck?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(deck: Deck): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (deck : Deck) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (deck : Deck) : Long

    @Delete
    fun delete (deck : Deck)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDeckCard(member: DeckCardAssociation)

    @Delete
    suspend fun removeDeckCard(member: DeckCardAssociation)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addDeckCard(members: List<DeckCardAssociation>)

    @Delete
    suspend fun removeDeckCard(members: List<DeckCardAssociation>)

    @Query("DELETE FROM tpas WHERE did = :tid")
    fun deleteDeckCards(tid: Long)
}