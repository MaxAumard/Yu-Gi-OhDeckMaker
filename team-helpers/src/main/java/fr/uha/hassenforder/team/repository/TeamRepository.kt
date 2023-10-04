package fr.uha.hassenforder.team.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.android.database.DeltaUtil
import fr.uha.hassenforder.team.model.FullTeam
import fr.uha.hassenforder.team.model.Team
import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.database.TeamDao
import fr.uha.hassenforder.team.model.*
import kotlinx.coroutines.flow.Flow

class TeamRepository (
    private val teamDao : TeamDao,
    private val personDao : PersonDao,
) {

    fun getAll() : Flow<List<Team>> {
        return teamDao.getAll()
    }

    fun getTeamById(id: Long): Flow<FullTeam> {
        return teamDao.getFullTeamById(id);
    }

    fun getPersonById(id: Long): Flow<Person?> {
        return personDao.getPersonById(id);
    }

    @WorkerThread
    suspend fun createTeam(team: Team): Long {
        return teamDao.upsert(team)
    }

    @WorkerThread
    suspend fun saveTeam(oldTeam: FullTeam, newTeam: FullTeam): Long {
        var teamToSave : Team? = null
        if (! Comparators.shallowEqualsTeam(oldTeam.team, newTeam.team)) {
            teamToSave = newTeam.team
        }
        val teamId: Long = newTeam.team.tid
        val delta: DeltaUtil<Person, TeamPersonAssociation> = object : DeltaUtil<Person, TeamPersonAssociation>() {
            override fun getId(input: Person): Long {
                return input.pid
            }
            override fun same(initial: Person, now: Person): Boolean {
                return true
            }
            override fun createFor(input: Person): TeamPersonAssociation {
                return TeamPersonAssociation(teamId, input.pid)
            }
        }
        val oldList = oldTeam.members
        val newList = newTeam.members
        delta.calculate(oldList, newList)

        if (teamToSave != null) teamDao.upsert(teamToSave)
        teamDao.removeTeamPerson(delta.toRemove)
        teamDao.addTeamPerson(delta.toAdd)

        return teamId
    }

    suspend fun delete(team: Team) {
        teamDao.delete(team)
        teamDao.deleteTeamPersons (team.tid)
    }

}