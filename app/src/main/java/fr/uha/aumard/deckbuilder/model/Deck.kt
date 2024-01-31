package fr.uha.aumard.deckbuilder.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "decks")
data class Deck(
    @PrimaryKey(autoGenerate = true)
    val did: Long = 0,
    val name: String = "",
    val creationDate: Date = Date()
)
