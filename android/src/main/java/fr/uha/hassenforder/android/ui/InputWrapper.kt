package fr.uha.hassenforder.android.ui

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class StringInputWrapper(
    val value: String = "",
    val errorId: Int? = null
) : Parcelable

@Parcelize
data class EnumInputWrapper<T : Enum<T>>(
    val value: T,
    val errorId: Int? = null
) : Parcelable