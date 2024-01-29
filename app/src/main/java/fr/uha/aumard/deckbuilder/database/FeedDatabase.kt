package fr.uha.aumard.deckbuilder.database

import fr.uha.aumard.deckbuilder.model.Deck
import fr.uha.aumard.deckbuilder.model.DeckCardAssociation

class FeedDatabase {
    private val cardDao: CardDao = DeckBuilderDatabase.get().cardDao
    private val deckDao: DeckDao = DeckBuilderDatabase.get().deckDao

    suspend fun feedDecks() {
        repeat(3) {
            val deckId = deckDao.create(Deck(name=deckNames.random()))

            val mainDeckCards = cardDao.getRandomMainDeckCards()
            val extraDeckCards = cardDao.getRandomExtraDeckCards()


            val allCards = mainDeckCards + extraDeckCards

            val associations = allCards.map { card ->
                DeckCardAssociation(deckId, card.cid)
            }

            deckDao.addDeckCard(associations)
        }
    }


    companion object {
        private val deckNames: Array<String> = arrayOf(
            "Zeus",
            "Héra",
            "Hestia",
            "Déméter",
            "Apollon",
            "Artémis",
            "Héphaïstos",
            "Athéna",
            "Arès",
            "Aphrodite",
            "Hermès",
            "Dionysos"
        )

    }
}