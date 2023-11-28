package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "tpas",
        primaryKeys = ["did", "cid"],
        indices = [Index("did"), Index("cid")]
)
class DeckCardAssociation (
        val did: Long,
        val cid: Long
)
