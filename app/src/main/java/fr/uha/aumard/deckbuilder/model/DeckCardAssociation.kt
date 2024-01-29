package fr.uha.aumard.deckbuilder.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "deck_card_associations",
        primaryKeys = ["did", "cid"],
        indices = [Index("did"), Index("cid")]
)
class DeckCardAssociation (
        val did: Long,
        val cid: Long
)
