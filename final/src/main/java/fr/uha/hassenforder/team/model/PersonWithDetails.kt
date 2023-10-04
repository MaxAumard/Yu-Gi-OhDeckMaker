package fr.uha.hassenforder.team.model

import androidx.room.Embedded

class PersonWithDetails (
        @Embedded
        var person: Person,

        var teamLeadingCount : Int,
        var teamMemberCount : Int,
)
