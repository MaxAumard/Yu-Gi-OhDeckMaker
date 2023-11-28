package fr.uha.hassenforder.team.database

import android.net.Uri
import fr.uha.hassenforder.team.model.*
import java.util.*

class FeedDatabase {

    private suspend fun feedPersons(): LongArray {
        val dao: PersonDao = TeamDatabase.get().personDao
        val ids = LongArray(4)
        ids[0] = dao.create(getRandomPerson(Type.TRAP))
        ids[1] = dao.create(getRandomPerson(Type.MAGIC))
        ids[2] = dao.create(getRandomPerson(Type.MONSTER))
        ids[3] = dao.create(getRandomPerson(Type.MONSTER))
        return ids
    }

    private suspend fun feedTeams(pids: LongArray) {
        val dao: TeamDao = TeamDatabase.get().teamDao
        val team = getRandomTeam()
        val tid = dao.create(team)
//        dao.addTeamPerson(TeamPersonAssociation(did, pids.get(0)))
//        dao.addTeamPerson(TeamPersonAssociation(did, pids.get(3)))
    }

    suspend fun populate() {
        val pids = feedPersons()
        feedTeams(pids)
    }

    suspend fun clear() {
        TeamDatabase.get().clearAllTables()
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
        private val teamNames: Array<String> = arrayOf(
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

        private fun getRandomPerson(type: Type): Card {
            return Card(
                0,
                getRandomFirstName(type),
                getRandomLastName(),
                getRandomPhone(),
                type,
                null
            )
        }

        private fun getRandomTeamName(): String {
            return geRandomName(teamNames)
        }

        private fun getRandomTeam(): Deck {
            return Deck(
                0,
                getRandomTeamName(),
                Date()
            )
        }
    }
}