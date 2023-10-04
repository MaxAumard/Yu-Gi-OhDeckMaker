package fr.uha.hassenforder.android.ui

import java.text.SimpleDateFormat
import java.util.Date

class Converter {

    companion object {

        private var output: SimpleDateFormat? = null

        private fun getDateShortFormater () : SimpleDateFormat {
            if (output == null) {
                output = SimpleDateFormat("dd MMMM yyyy")
            }
            return output!!
        }

        fun convert (date : Date?, default : String = "") : String {
            if (date == null) {
                return default
            } else {
                return getDateShortFormater().format(date)
            }
        }

    }

}
