package fr.uha.hassenforder.team.ui.team

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