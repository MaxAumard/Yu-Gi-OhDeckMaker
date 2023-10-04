package fr.uha.hassenforder.android.ui

import androidx.annotation.StringRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import fr.uha.hassenforder.android.R
import java.time.Instant
import java.time.temporal.ChronoUnit
import java.util.*

private fun instantToMidnight(date: Date): Instant {
    val calendar = GregorianCalendar()
    calendar.time = date
    calendar.set(Calendar.MILLISECOND, 0)
    calendar.set(Calendar.SECOND, 0)
    calendar.set(Calendar.MINUTE, 0)
    calendar.set(Calendar.HOUR, 0)
    return calendar.toInstant()
}

private fun buildDateResource (date : Date?) : Int? {
    if (date == null) return R.string.date_now
    val day = instantToMidnight(date)
    val today = instantToMidnight(Date())
    val between: Long = ChronoUnit.DAYS.between(today, day)
    return when (between) {
        -1L -> R.string.date_yesterday
        0L -> R.string.date_today
        1L -> R.string.date_tomorrow
        else -> null
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OutlinedDateField(
    onValueChange : (Date) -> Unit,
    modifier : Modifier = Modifier,
    value : Date? = null,
    @StringRes errorId : Int? = null,
    @StringRes label: Int? = null,
) {
    val showDialog =  remember { mutableStateOf(false) }

    if (showDialog.value) {
        CustomDatePicker (
            titleId = R.string.day_title,
            value ?: Date(),
            7, 7
        ) { showDialog.value = false; if (it != null) onValueChange(it) }
    }

    Column (
        modifier = modifier
            .padding(top = 4.dp, bottom = 4.dp)
            .fillMaxWidth()
            .border(
                BorderStroke(1.dp, MaterialTheme.colorScheme.onBackground),
                MaterialTheme.shapes.extraSmall
            )
            .padding(start = 16.dp),
    ) {
        val color = if (errorId == null) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.error
        if (label != null) {
            Text(text = stringResource(id = label), color = color)
        }
        Row(modifier = Modifier
            .clickable(onClick = { showDialog.value = true })
            .padding(top = 4.dp)
        ) {
            @StringRes val dayAsResource : Int? = buildDateResource (value)
            val dayAsText: String = if (dayAsResource != null) stringResource(id = dayAsResource) else Converter.convert(
                value
            )
            Text(dayAsText, color = color, modifier = Modifier.weight(1.0f))
            Icon (
                imageVector = Icons.Outlined.DateRange,
                contentDescription = "date picker"
            )
        }
        if (errorId != null){
            Text(
                text = stringResource(id = errorId),
                color = color,
            )
        }
    }
}

private fun dateValidator (chose : Long, daysBefore : Long?, daysAfter : Long? ) : Boolean {
    val today = Date()
    if (daysBefore != null) {
        val start = today.toInstant().minus(daysBefore, ChronoUnit.DAYS).toEpochMilli()
        if (chose < start) return false
    }
    if (daysAfter != null) {
        val end = today.toInstant().plus(daysAfter, ChronoUnit.DAYS).toEpochMilli()
        if (chose > end) return false
    }
    return true
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomDatePicker (
    titleId : Int?,
    value : Date,
    daysBefore : Long? = null,
    daysAfter : Long? = null,
    onSelect : (Date?) -> Unit
) {
    val datePickerState = rememberDatePickerState(
        initialSelectedDateMillis = value.time
    )
    DatePickerDialog(
        onDismissRequest = { onSelect(null) },
        confirmButton = {
            TextButton(
                onClick = { onSelect(Date(datePickerState.selectedDateMillis ?: 0)) }
            ) {
                Text(stringResource (id = R.string.valid))
            }
        },
        dismissButton = {
            TextButton(
                onClick = { onSelect(null) }
            ) {
                Text(stringResource (id = R.string.cancel))
            }
        }
    ) {
        DatePicker(
            state = datePickerState,
            dateValidator = { timestamp -> dateValidator(timestamp, daysBefore, daysAfter) },
            title = { if (titleId != null) Text(text = stringResource (id = titleId) ) }
        )
    }
}
