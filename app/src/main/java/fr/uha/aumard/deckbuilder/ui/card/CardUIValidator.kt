package fr.uha.aumard.deckbuilder.ui.card

import android.net.Uri
import fr.uha.aumard.deckbuilder.R
import fr.uha.aumard.deckbuilder.model.Type

object CardUIValidator {

    fun validateNameChange(newValue: String): Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 3 -> R.string.value_too_short
            newValue.length > 12 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateDescriptionChange(newValue: String): Int? {
        return when {
            newValue.isEmpty() -> R.string.value_empty
            newValue.isBlank() -> R.string.value_blank
            newValue.length < 3 -> R.string.value_too_short
            newValue.length > 12 -> R.string.value_too_long
            else -> null
        }
    }

    fun validateLevelChange(newValue: String): Int? {
        return if (newValue.startsWith("+")) {
            when {
                newValue.length < 12 -> R.string.value_too_short
                newValue.length > 12 -> R.string.value_too_long
                newValue.length == 12 -> null
                else -> R.string.phone_illegal
            }
        } else {
            when {
                newValue.length < 4 -> R.string.value_too_short
                newValue.length > 10 -> R.string.value_too_long
                newValue.length == 4 -> null
                newValue.length == 10 -> null
                else -> R.string.phone_illegal
            }
        }
    }

    fun validateTypeChange(newValue: Type?): Int? {
        return when {
            newValue == null -> R.string.gender_must_set
            else -> null
        }
    }

    fun validatePictureChange(newValue: Uri?): Int? {
        return null
    }

}