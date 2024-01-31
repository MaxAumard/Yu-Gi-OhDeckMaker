package fr.uha.aumard.deckbuilder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection_cards")
data class CollectionCard(
    @PrimaryKey(autoGenerate = true) val ccid: Long = 0,
    val condition: Condition = Condition.MINT,
    val language: Language = Language.FRENCH,
    val rarity: Rarity = Rarity.COMMON,
)