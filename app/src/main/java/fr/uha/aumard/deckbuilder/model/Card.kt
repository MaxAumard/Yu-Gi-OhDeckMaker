package fr.uha.aumard.deckbuilder.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cards")
data class Card(
    @PrimaryKey(autoGenerate = true)
    val cid: Long = 0,
    val name: String,
    val description: String,
    val level: String?,
    val type: Type,
    val isExtraDeck: Boolean,
    val picture: Uri?,
    val attack: String?,
    val defense: String?,
) {
}