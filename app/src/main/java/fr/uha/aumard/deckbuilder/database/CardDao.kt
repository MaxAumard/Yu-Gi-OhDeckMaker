package fr.uha.aumard.deckbuilder.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.CardWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface CardDao {

    @Query("SELECT * FROM cards")
    fun getAll(): Flow<List<Card>>

    @Query(
        "SELECT * " +
                ", (SELECT COUNT(*) FROM deck_card_associations DCA WHERE DCA.cid = C.cid) AS cardCount" +
                " FROM cards AS C"
    )
    fun getAllWithDetails(): Flow<List<CardWithDetails>>

    @Query("SELECT * FROM cards WHERE cid = :id")
    fun getCardById(id: Long): Flow<Card?>


    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update(card: Card): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(card: Card): Long

    @Delete
    fun delete(card: Card)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(cards: List<Card>)

    @Query("SELECT * FROM cards LIMIT :limit OFFSET :offset")
    fun getCardsPage(limit: Int, offset: Int): Flow<List<Card>>

    @Query("SELECT COUNT(*) FROM cards")
    suspend fun countCards(): Int

    @Query("SELECT * FROM cards WHERE name LIKE '%' || :searchQuery || '%' LIMIT :limit OFFSET :offset")
    fun getCardsPageFiltered(
        limit: Int,
        offset: Int,
        searchQuery: String,
    ): Flow<List<Card>>

    @Query("SELECT * FROM cards WHERE isExtraDeck = 0 ORDER BY RANDOM() LIMIT 40")
    suspend fun getRandomMainDeckCards(): List<Card>

    @Query("SELECT * FROM cards WHERE isExtraDeck = 1 ORDER BY RANDOM() LIMIT 15")
    suspend fun getRandomExtraDeckCards(): List<Card>

}