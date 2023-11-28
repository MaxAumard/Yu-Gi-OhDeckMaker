package fr.uha.hassenforder.team.repository

import androidx.annotation.WorkerThread
import fr.uha.hassenforder.android.database.DeltaUtil
import fr.uha.hassenforder.team.database.PersonDao
import fr.uha.hassenforder.team.database.TeamDao
import fr.uha.hassenforder.team.model.Comparators
import fr.uha.hassenforder.team.model.FullDeck
import fr.uha.hassenforder.team.model.Card
import fr.uha.hassenforder.team.model.Deck
import fr.uha.hassenforder.team.model.DeckCardAssociation
import kotlinx.coroutines.flow.Flow

class TeamRepository(
    private val teamDao: TeamDao,
    private val personDao: PersonDao
)
{
    fun getAll () : Flow<List<Deck>> {
        return teamDao.getAll()
    }

    fun getTeamById (id : Long) : Flow<FullDeck?> {
        return teamDao.getTeamById(id)
    }

    fun getPersonById (id : Long) : Flow<Card?> {
        return personDao.getCardById(id)
    }

    @WorkerThread
    suspend fun createTeam(deck: Deck): Long {
        return teamDao.upsert(deck)
    }

    @WorkerThread
    suspend fun saveTeam(oldTeam: FullDeck, newTeam: FullDeck): Long {
        var deckToSave : Deck? = null
        if (! Comparators.shallowEqualsTeam(oldTeam.deck, newTeam.deck)) {
            deckToSave = newTeam.deck
        }
        val teamId: Long = newTeam.deck.did
        val delta: DeltaUtil<Card, DeckCardAssociation> = object : DeltaUtil<Card, DeckCardAssociation>() {
            override fun getId(input: Card): Long {
                return input.cid
            }
            override fun same(initial: Card, now: Card): Boolean {
                return true
            }
            override fun createFor(input: Card): DeckCardAssociation {
                return DeckCardAssociation(teamId, input.cid)
            }
        }
        val oldList = oldTeam.members
        val newList = newTeam.members
        delta.calculate(oldList, newList)

        if (deckToSave != null) teamDao.upsert(deckToSave)
        teamDao.removeTeamPerson(delta.toRemove)
        teamDao.addTeamPerson(delta.toAdd)

        return teamId
    }

    suspend fun delete(deck: Deck) {
        teamDao.delete(deck)
        teamDao.deleteTeamPersons (deck.did)
    }

}