package fr.uha.hassenforder.team.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import fr.uha.hassenforder.team.model.Card
import fr.uha.hassenforder.team.model.CardWithDetails
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonDao {

    @Query("SELECT * FROM cards")
    fun getAll () : Flow<List<Card>>

    @Query("SELECT * " +
            ", (SELECT COUNT(*) FROM tpas TPA WHERE TPA.cid = C.cid) AS memberCount" +
            " FROM cards AS C")
    fun getAllWithDetails () : Flow<List<CardWithDetails>>

    @Query("SELECT * FROM cards WHERE cid = :id")
    fun getCardById (id : Long) : Flow<Card?>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun create (card : Card) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun update (card : Card) : Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun upsert (card : Card) : Long

    @Delete
    fun delete (card : Card)
}