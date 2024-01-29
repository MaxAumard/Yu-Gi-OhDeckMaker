package fr.uha.aumard.deckbuilder.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Extension
import fr.uha.aumard.deckbuilder.model.ExtensionCardAssociation
import kotlinx.coroutines.flow.Flow

@Dao
interface ExtensionDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(extensions: List<Extension>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addExtensionCard(associations: List<ExtensionCardAssociation>)


    @Query("SELECT setName, setName FROM extensions GROUP BY setName")
    fun getExtensions(): Flow<List<Extension>>

    @Transaction
    @Query("SELECT * FROM extension_card_associations WHERE setName = :setName")
    fun getExtensionCardAssociations(setName: String): Flow<List<ExtensionCardAssociation>>

    @Transaction
    @Query("SELECT * FROM cards INNER JOIN extension_card_associations ON cards.cid = extension_card_associations.cid WHERE extension_card_associations.setName = :setName")
    fun getCardsInExtension(setName: String): Flow<List<Card>>
}