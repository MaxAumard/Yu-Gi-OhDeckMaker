package fr.uha.aumard.deckbuilder.repository

import androidx.annotation.WorkerThread
import fr.uha.aumard.android.database.DeltaUtil
import fr.uha.aumard.deckbuilder.database.CardDao
import fr.uha.aumard.deckbuilder.database.DeckDao
import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Comparators
import fr.uha.aumard.deckbuilder.model.Deck
import fr.uha.aumard.deckbuilder.model.DeckCardAssociation
import fr.uha.aumard.deckbuilder.model.FullDeck
import kotlinx.coroutines.flow.Flow

class DeckRepository(
    private val deckDao: DeckDao,
    private val cardDao: CardDao
) {
    fun getAll(): Flow<List<Deck>> {
        return deckDao.getAll()
    }

    fun getDeckById(id: Long): Flow<FullDeck?> {
        return deckDao.getDeckById(id)
    }

    fun getCardById(id: Long): Flow<Card?> {
        return cardDao.getCardById(id)
    }

    @WorkerThread
    suspend fun createDeck(deck: Deck): Long {
        return deckDao.upsert(deck)
    }

    @WorkerThread
    suspend fun saveDeck(oldDeck: FullDeck, newDeck: FullDeck): Long {
        var deckToSave: Deck? = null
        if (!Comparators.shallowEqualsDeck(oldDeck.deck, newDeck.deck)) {
            deckToSave = newDeck.deck
        }
        val DeckId: Long = newDeck.deck.did
        val delta: DeltaUtil<Card, DeckCardAssociation> =
            object : DeltaUtil<Card, DeckCardAssociation>() {
                override fun getId(input: Card): Long {
                    return input.cid
                }

                override fun same(initial: Card, now: Card): Boolean {
                    return true
                }

                override fun createFor(input: Card): DeckCardAssociation {
                    return DeckCardAssociation(DeckId, input.cid)
                }
            }
        val oldList = oldDeck.cards
        val newList = newDeck.cards
        delta.calculate(oldList, newList)

        if (deckToSave != null) deckDao.upsert(deckToSave)
        deckDao.removeDeckCard(delta.toRemove)
        deckDao.addDeckCard(delta.toAdd)

        return DeckId
    }

    suspend fun delete(deck: Deck) {
        deckDao.delete(deck)
        deckDao.deleteDeckCards(deck.did)
    }

}