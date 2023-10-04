package fr.uha.hassenforder.team.database

import fr.uha.hassenforder.team.R
import fr.uha.hassenforder.team.model.*
import java.util.*

class FeedDatabase {

    private suspend fun feedPersons(): LongArray {
        val dao: PersonDao = TeamDatabase.get().getPersonDao()
        val ids = LongArray(4)
        ids[0] = dao.create(getRandomPerson(Gender.BOY))
        ids[1] = dao.create(getRandomPerson(Gender.GIRL))
        ids[2] = dao.create(getRandomPerson(Gender.NO))
        ids[3] = dao.create(getRandomPerson(Gender.NO))
        return ids
    }
/*
    private suspend fun feedTeams(pids: LongArray) {
        val dao: TeamDao = TeamDatabase.get().getTeamDao()
        val team = getRandomTeam(pids.get(0))
        val tid = dao.create(team)
        dao.addTeamPerson(TeamPersonAssociation(tid, pids.get(0)))
        dao.addTeamPerson(TeamPersonAssociation(tid, pids.get(3)))
    }
*/
    suspend fun populate() {
        val pids = feedPersons()
//        feedTeams(pids)
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

        private fun getRandomFirstName(gender: Gender): String {
            var g = gender
            if (gender == Gender.NO) {
                g = if (rnd.nextInt(1000) > 500) {
                    Gender.BOY
                } else {
                    Gender.GIRL
                }
            }
            when (g) {
                Gender.BOY -> return geRandomName(maleFirstNames)
                Gender.GIRL -> return geRandomName(femaleFirstNames)
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

        private fun getRandomPerson(gender: Gender): Person {
            return Person(0,
                    getRandomFirstName(gender),
                    getRandomLastName(),
                    getRandomPhone(),
                    gender,
            )
        }

        private fun getRandomTeamName(): String {
            return geRandomName(teamNames)
        }
/*
        private fun getRandomTeam(leader: Long): Team {
            return Team(0,
                    getRandomTeamName(),
                    Objective.values()[rnd.nextInt(Objective.values().size)].name,
                    Date(),
                    getRandomBetween(3, 9),
                    getRandomBetween(2, 4),
                    getRandomBetween(4, 6),
                    leader
            )
        }
*/
    }
}