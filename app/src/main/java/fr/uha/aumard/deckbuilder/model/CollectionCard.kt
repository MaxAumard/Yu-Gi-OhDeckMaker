package fr.uha.aumard.deckbuilder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "collection_cards")
data class CollectionCard(
    @PrimaryKey(autoGenerate = true) val ccid: Long = 0,
    val condition: String,
    val language: String,
    val rarity: String,
    val quantity: Int = 0
)