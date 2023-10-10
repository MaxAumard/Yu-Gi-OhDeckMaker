package fr.uha.hassenforder.team.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity(tableName = "teams")
data class Team (
    @PrimaryKey(autoGenerate = true)
    val tid: Long = 0,
    val name: String = "",
    val startDay: Date = Date(),
    val duration: Int = 0,
    val leaderId: Long = 0
)
