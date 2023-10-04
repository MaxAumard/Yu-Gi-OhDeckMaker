package fr.uha.hassenforder.team.model

class Comparators {

    companion object {

        fun shallowEqualsPerson(oldPerson: Person?, newPerson: Person?): Boolean {
            if (oldPerson == null && newPerson == null) return true
            if (oldPerson != null && newPerson == null) return false
            if (oldPerson == null && newPerson != null) return false
            oldPerson as Person
            newPerson as Person
            if (oldPerson.pid != newPerson.pid) return false
            return true
        }

        fun shallowEqualsListPersons(oldPersons: List<Person>?, newPersons: List<Person>?): Boolean {
            if (oldPersons == null && newPersons == null) return true
            if (oldPersons != null && newPersons == null) return false
            if (oldPersons == null && newPersons != null) return false
            oldPersons as List<Person>
            newPersons as List<Person>
            if (oldPersons.size != newPersons.size) return false
            val oldMap = mutableSetOf<Long>()
            oldPersons.forEach { p -> oldMap.add(p.pid) }
            newPersons.forEach { p -> if (! oldMap.contains(p.pid)) return false }
            val newMap = mutableSetOf<Long>()
            newPersons.forEach { p -> newMap.add(p.pid) }
            oldPersons.forEach { p -> if (! newMap.contains(p.pid)) return false }
            return true
        }

        fun shallowEqualsTeam(oldTeam: Team?, newTeam: Team?): Boolean {
            if (newTeam == null && oldTeam == null) return true
            if (newTeam != null && oldTeam == null) return false
            if (newTeam == null && oldTeam != null) return false
            val safeNew : Team = newTeam as Team
            val safeOld : Team = oldTeam as Team
            if (safeNew.tid != safeOld.tid) return false
            if (safeNew.name != safeOld.name) return false
            if (safeNew.startDay != safeOld.startDay) return false
            if (safeNew.duration != safeOld.duration) return false
            if (safeNew.leaderId != safeOld.leaderId) return false
            return true
        }

    }

}