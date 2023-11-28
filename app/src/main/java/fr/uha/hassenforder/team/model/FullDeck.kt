package fr.uha.hassenforder.team.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class FullDeck (
        @Embedded
        val deck: Deck,

        @Relation(parentColumn = "did", entityColumn = "cid", associateBy = Junction(DeckCardAssociation::class))
        val members: List<Card>,
)