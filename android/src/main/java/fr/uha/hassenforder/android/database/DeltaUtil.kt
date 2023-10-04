package fr.uha.hassenforder.android.database

import java.util.*

abstract class DeltaUtil<X, Y> {

    val toAdd: MutableList<Y> = mutableListOf<Y>()
    val toRemove: MutableList<Y> = mutableListOf<Y>()
    val toUpdate: MutableList<Y> = mutableListOf<Y>()

    private fun convert(list: List<X>?): Map<Long, X> {
        val ids: MutableMap<Long, X> = TreeMap()
        if (list != null) {
            for (x in list) {
                ids[getId(x)] = x
            }
        }
        return ids
    }

    fun calculate(left: List<X>?, right: List<X>?) {
        val initial = convert(left)
        val next = convert(right)
        for (id in next.keys) {
            if (!initial.containsKey(id)) toAdd.add(createFor(next.get(id)!!))
        }
        for (id in initial.keys) {
            if (!next.containsKey(id)) toRemove.add(createFor(initial.get(id)!!))
        }
        for (id in next.keys) {
            if (initial.containsKey(id)) {
                if (!same(initial.get(id)!!, next.get(id)!!)) {
                    toUpdate.add(createFor(next.get(id)!!))
                }
            }
        }
    }

    protected abstract fun getId(input: X): Long
    protected abstract fun same(initial: X, now: X): Boolean
    protected abstract fun createFor(input: X): Y

}