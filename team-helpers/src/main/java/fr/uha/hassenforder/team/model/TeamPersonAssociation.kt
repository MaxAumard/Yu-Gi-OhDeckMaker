package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.Index

@Entity(tableName = "tpas",
        primaryKeys = ["tid", "pid"],
        indices = [Index("tid"), Index("pid")]
)
class TeamPersonAssociation (
        val tid: Long,
        val pid: Long
)
