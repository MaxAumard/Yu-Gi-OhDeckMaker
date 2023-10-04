package fr.uha.hassenforder.team.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import fr.uha.hassenforder.team.model.Person

@Database(
    entities = [
        Person::class,
    ],
    version=1,
    exportSchema = false
)
abstract class TeamDatabase : RoomDatabase() {

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