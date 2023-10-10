package fr.uha.hassenforder.team.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation

class FullTeam (
        @Embedded
        val team: Team,

        @Relation(parentColumn = "leaderId", entityColumn = "pid")
        val leader: Person?,

        @Relation(parentColumn = "tid", entityColumn = "pid", associateBy = Junction(TeamPersonAssociation::class))
        val members: List<Person>,
)