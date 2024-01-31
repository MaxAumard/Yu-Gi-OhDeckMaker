package fr.uha.aumard.deckbuilder.model

import androidx.room.Embedded

class CardWithDetails(
    @Embedded
    val card: Card,
)