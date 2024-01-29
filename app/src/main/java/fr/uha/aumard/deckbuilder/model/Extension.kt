package fr.uha.aumard.deckbuilder.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "extensions")
data class Extension(
    @PrimaryKey val setName: String,
)