package fr.uha.hassenforder.team.model

import android.net.Uri
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity (tableName = "cards")
data class Card (
    @PrimaryKey(autoGenerate = true)
    val cid : Long = 0,
    val name : String,
    val description : String,
    val phone : String,
    val type : Type,
    val picture : Uri?
) {
}