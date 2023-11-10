package fr.uha.hassenforder.team.model

import androidx.room.Embedded

class PersonWithDetails (
        @Embedded
        val person: Person,

        val leaderCount : Int,
        val memberCount : Int,
)