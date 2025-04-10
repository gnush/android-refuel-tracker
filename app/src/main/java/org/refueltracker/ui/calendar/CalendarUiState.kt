package org.refueltracker.ui.calendar

import kotlinx.datetime.Month
import kotlinx.datetime.number
import java.util.Calendar

data class CalendarUiState(
    val month: Month = Month(Calendar.getInstance().get(Calendar.MONTH)+1),
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
)

fun CalendarUiState.previousMonth(): CalendarUiState =
    if (month.number == 1)
        CalendarUiState(
            month = Month(12),
            year = year-1
        )
    else
        copy(month = Month(month.number-1))

fun CalendarUiState.nextMonth(): CalendarUiState =
    if (month.number == 12)
        CalendarUiState(
            month = Month(1),
            year = year+1
        )
    else
        copy(month = Month(month.number+1))