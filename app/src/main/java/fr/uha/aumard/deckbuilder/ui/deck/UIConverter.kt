package fr.uha.aumard.deckbuilder.ui.deck

import java.text.SimpleDateFormat
import java.util.Date

class UIConverter {

    companion object {
        fun convert(date: Date): String {
            val sdf = SimpleDateFormat("dd/MMM/yyyy")
            return sdf.format(date)
        }
    }

}