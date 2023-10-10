package fr.uha.hassenforder.team.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import fr.uha.hassenforder.android.database.DatabaseTypeConverters
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.model.TeamPersonAssociation

@Database(
    entities = [
        Person::class,
        Team::class,
        TeamPersonAssociation::class
    ],
    version=1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class TeamDatabase : RoomDatabase() {

    abstract val personDao : PersonDao

    abstract val teamDao : TeamDao

    companion object {
        private lateinit var instance : TeamDatabase

        @Synchronized
        fun create (context : Context) : TeamDatabase {
            instance = Room.databaseBuilder(context, TeamDatabase::class.java, "team.db").build()
            return instance
        }

        @Synchronized
        fun get () : TeamDatabase {
            return instance
        }

    }

}