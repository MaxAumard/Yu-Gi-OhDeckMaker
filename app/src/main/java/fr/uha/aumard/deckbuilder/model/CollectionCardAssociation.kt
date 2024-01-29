package fr.uha.aumard.deckbuilder.model

import androidx.room.Entity
import androidx.room.Index


@Entity(
    tableName = "collection_card_associations",
    primaryKeys = ["cid", "ccid"],
    indices = [Index(value = ["cid"]), Index(value = ["ccid"])]
)
data class CollectionCardAssociation(
    val cid: Long,
    val ccid: Long
)