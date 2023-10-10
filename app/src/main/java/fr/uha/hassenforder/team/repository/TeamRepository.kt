package fr.uha.hassenforder.team.repository

import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.database.TeamDao
import fr.uha.hassenforder.team.model.FullTeam
import fr.uha.hassenforder.team.model.Person
import fr.uha.hassenforder.team.model.Team
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class TeamRepository(private val teamDao: TeamDao)
{
    fun getAll () : Flow<List<Team>> {
        return teamDao.getAll()
    }

    fun getPersonById (id : Long) : Flow<FullTeam?> {
        return teamDao.getTeamById(id)
    }

    suspend fun create (team : Team) : Long = withContext(Dispatchers.IO) {
        return@withContext teamDao.create(team)
    }

    suspend fun update (oldTeam : Team, team : Team) : Long = withContext(Dispatchers.IO) {
        return@withContext teamDao.update(team)
    }

    suspend fun upsert (team : Team) : Long = withContext(Dispatchers.IO) {
        return@withContext teamDao.upsert(team)
    }

    suspend fun delete (team : Team) = withContext(Dispatchers.IO) {
        teamDao.delete(team)
    }

}