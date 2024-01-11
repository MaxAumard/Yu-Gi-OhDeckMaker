package fr.uha.aumard.deckbuilder.ui.deck

import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Card
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.Calendar
import java.util.Date
import java.util.GregorianCalendar

object DeckUIValidator {

    fun validateNameChange(newValue: String) : Int? {
        return when {
            newValue.isEmpty()  ->  R.string.value_empty
            newValue.isBlank()  ->  R.string.value_blank
            newValue.length < 3 ->  R.string.value_too_short
            else -> null
        }
    }

    private fun instantToMidnight(date: Date): Instant {
        val calendar = GregorianCalendar()
        calendar.time = date
        calendar.set(Calendar.MILLISECOND, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.HOUR, 0)
        return calendar.toInstant()
    }

    fun validateStartDayChange(newValue: Date?) : Int? {
        if (newValue == null) return R.string.date_must_set
        val day = instantToMidnight(newValue)
        val today = instantToMidnight(Date())
        val between: Long = ChronoUnit.DAYS.between(today, day)
        if (between < -7) return R.string.date_too_old
        if (between >  7) return R.string.date_too_far
        return null
    }

    fun validateDurationChange(newValue: Int): Int? {
        return when {
            newValue < 2 -> R.string.duration_too_short
            newValue > 9 -> R.string.duration_too_long
            else -> null
        }
    }


    fun validateCardsChange(state: DeckViewModel.DeckUIState, newValue: List<Card>?): Int? {
        if (newValue == null) return R.string.decks_not_empty

        val mainDeckCards = newValue.filter { !it.isExtraDeck }
        val extraDeckCards = newValue.filter { it.isExtraDeck }

        return when {
            mainDeckCards.size < 40 -> R.string.decks_not_enough
            mainDeckCards.size > 60 -> R.string.decks_too_much
            extraDeckCards.size > 15 -> R.string.extra_decks_too_much
            else -> null
        }
    }

}