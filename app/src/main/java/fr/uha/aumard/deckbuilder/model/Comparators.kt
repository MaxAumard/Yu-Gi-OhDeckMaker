package fr.uha.aumard.deckbuilder.model

class Comparators {

    companion object {

        fun shallowEqualsCard(oldCard: Card?, newCard: Card?): Boolean {
            if (oldCard == null && newCard == null) return true
            if (oldCard != null && newCard == null) return false
            if (oldCard == null && newCard != null) return false
            oldCard as Card
            newCard as Card
            if (oldCard.cid != newCard.cid) return false
            return true
        }

        fun shallowEqualsListCards(oldCards: List<Card>?, newCards: List<Card>?): Boolean {
            if (oldCards == null && newCards == null) return true
            if (oldCards != null && newCards == null) return false
            if (oldCards == null && newCards != null) return false
            oldCards as List<Card>
            newCards as List<Card>
            if (oldCards.size != newCards.size) return false
            val oldMap = mutableSetOf<Long>()
            oldCards.forEach { p -> oldMap.add(p.cid) }
            newCards.forEach { p -> if (!oldMap.contains(p.cid)) return false }
            val newMap = mutableSetOf<Long>()
            newCards.forEach { p -> newMap.add(p.cid) }
            oldCards.forEach { p -> if (!newMap.contains(p.cid)) return false }
            return true
        }

        fun shallowEqualsDeck(oldDeck: Deck?, newDeck: Deck?): Boolean {
            if (newDeck == null && oldDeck == null) return true
            if (newDeck != null && oldDeck == null) return false
            if (newDeck == null && oldDeck != null) return false
            val safeNew: Deck = newDeck as Deck
            val safeOld: Deck = oldDeck as Deck
            if (safeNew.did != safeOld.did) return false
            if (safeNew.name != safeOld.name) return false
            if (safeNew.creationDate != safeOld.creationDate) return false
            return true
        }

    }

}