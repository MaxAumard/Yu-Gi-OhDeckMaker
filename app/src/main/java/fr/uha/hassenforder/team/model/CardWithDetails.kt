package fr.uha.hassenforder.team.model

import androidx.room.Embedded

class CardWithDetails (
        @Embedded
        val card: Card,
        val memberCount : Int,
)