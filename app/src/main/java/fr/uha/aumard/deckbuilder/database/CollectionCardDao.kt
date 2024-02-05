package fr.uha.aumard.deckbuilder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import fr.uha.aumard.deckbuilder.model.CollectionCard
import fr.uha.aumard.deckbuilder.model.CollectionCardAssociation
import kotlinx.coroutines.flow.Flow

@Dao
interface CollectionCardDao {

    @Query("SELECT * FROM collection_cards")
    fun getAllCollectionCards(): Flow<List<CollectionCard>>

    @Query(
        """
    SELECT COUNT(collection_cards.ccid)
    FROM collection_cards 
    INNER JOIN collection_card_associations ON collection_cards.ccid = collection_card_associations.ccid
    WHERE collection_card_associations.cid = :cardId
    """
    )
    fun countCardInCollection(cardId: Long): Int

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAssociation(association: CollectionCardAssociation)


    @Update
    suspend fun update(collectionCard: CollectionCard)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert(collectionCard: CollectionCard): Long

    @Query("SELECT * FROM collection_cards WHERE ccid = :ccid")
    fun getCollectionCardById(ccid: Long): Flow<CollectionCard?>

    @Query("SELECT * FROM collection_cards")
    fun getAll(): Flow<List<CollectionCard>>

    @Query("DELETE FROM collection_cards WHERE ccid = :ccid")
    fun delete(ccid: Long)

    @Query(
        """
    SELECT collection_cards.ccid
    FROM collection_cards 
    INNER JOIN collection_card_associations ON collection_cards.ccid = collection_card_associations.ccid
    WHERE collection_card_associations.cid = :cardId
    """
    )
    fun getCollectionCardId(cardId: Long): Flow<Long?>
}