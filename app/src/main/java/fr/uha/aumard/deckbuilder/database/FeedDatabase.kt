package fr.uha.aumard.deckbuilder.database

import fr.uha.aumard.deckbuilder.model.Card
import fr.uha.aumard.deckbuilder.model.Deck
import fr.uha.aumard.deckbuilder.model.Type
import java.util.Date
import java.util.Random

class FeedDatabase {

    private suspend fun feedCards(): LongArray {
        val dao: CardDao = DeckBuilderDatabase.get().cardDao
        val ids = LongArray(4)
        ids[0] = dao.create(getRandomCard(Type.TRAP))
        ids[1] = dao.create(getRandomCard(Type.MAGIC))
        ids[2] = dao.create(getRandomCard(Type.MONSTER))
        ids[3] = dao.create(getRandomCard(Type.MONSTER))
        return ids
    }

    private suspend fun feedDecks(pids: LongArray) {
        val dao: DeckDao = DeckBuilderDatabase.get().deckDao
        val deck = getRandomDeck()
        val tid = dao.create(deck)
//        dao.addTeamPerson(TeamPersonAssociation(did, pids.get(0)))
//        dao.addTeamPerson(TeamPersonAssociation(did, pids.get(3)))
    }

    suspend fun populate() {
        val pids = feedCards()
        feedDecks(pids)
    }

    suspend fun clear() {
        DeckBuilderDatabase.get().clearAllTables()
    }

    companion object {
        private val rnd: Random = Random()
        private val maleFirstNames: Array<String> = arrayOf(
            "Alexander",
            "Brendon",
            "Carrol",
            "Davis",
            "Emmerson",
            "Franklin",
            "Gordon",
            "Humphrey",
            "Ike",
            "Jarrod",
            "Kevin",
            "Lionel",
            "Mickey",
            "Nathan",
            "Oswald",
            "Phillip",
            "Quinn",
            "Ralph",
            "Shawn",
            "Terrence",
            "Urban",
            "Vince",
            "Wade",
            "Xan",
            "Yehowah",
            "Zed"
        )
        private val femaleFirstNames: Array<String> = arrayOf(
            "Abigail",
            "Betsy",
            "Carry",
            "Dana",
            "Edyth",
            "Fay",
            "Grace",
            "Hannah",
            "Isabel",
            "Jane",
            "Karrie",
            "Lauren",
            "Maddie",
            "Nanna",
            "Oprah",
            "Pamela",
            "Queen",
            "Rachel",
            "Samanta",
            "Tess",
            "Ursula",
            "Violet",
            "Wendy",
            "Xena",
            "Yvonne",
            "Zoey"
        )
        private val lastNames: Array<String> = arrayOf(
            "Activox",
            "Biseptine",
            "Calendula",
            "Delidose",
            "Eludril",
            "Fervex",
            "Gelox",
            "Hextril",
            "Imurel",
            "Jouvence",
            "Kenzen",
            "Lanzor",
            "Malocide",
            "Nicorette",
            "Oflocet",
            "Paracetamol",
            "Quotane",
            "Rennie",
            "Smecta",
            "Tamiflu",
            "Uniflox",
            "Vectrine",
            "Wellvone",
            "Xanax",
            "Yranol",
            "Zyban"
        )
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

        private fun geRandomName(names: Array<String>): String {
            return names.get(rnd.nextInt(names.size))
        }

        private fun getRandomFirstName(type: Type): String {
            var g = type
            if (type == Type.MONSTER) {
                g = if (rnd.nextInt(1000) > 500) {
                    Type.TRAP
                } else {
                    Type.MAGIC
                }
            }
            when (g) {
                Type.TRAP -> return geRandomName(maleFirstNames)
                Type.MAGIC -> return geRandomName(femaleFirstNames)
                else -> return ""
            }
        }

        private fun getRandomLastName(): String {
            return geRandomName(lastNames)
        }

        private fun getRandomPhone(): String {
            val tmp = StringBuilder()
            if (rnd.nextInt(1000) > 750) {
                tmp.append("36")
                tmp.append(rnd.nextInt(10))
                tmp.append(rnd.nextInt(10))
            } else {
                tmp.append("0")
                for (i in 0..8) {
                    tmp.append(rnd.nextInt(10))
                }
            }
            return tmp.toString()
        }

        private fun getRandomBetween(low: Int, high: Int): Int {
            return rnd.nextInt(high - low) + low
        }

        private fun getRandomBetween(low: Double, high: Double): Double {
            return rnd.nextDouble() * (high - low) + low
        }

        private fun getRandomCard(type: Type): Card {
            return Card(
                0,
                getRandomFirstName(type),
                getRandomLastName(),
                getRandomPhone(),
                type,
                rnd.nextBoolean(),
                null,
                getRandomBetween(0, 100).toString(),
                getRandomBetween(0, 100).toString(),
            )
        }

        private fun getRandomDeckName(): String {
            return geRandomName(deckNames)
        }

        private fun getRandomDeck(): Deck {
            return Deck(
                0,
                getRandomDeckName(),
                Date()
            )
        }
    }
}