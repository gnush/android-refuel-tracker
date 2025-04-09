package org.refueltracker.ui.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.number
import java.util.Calendar
import java.util.GregorianCalendar

class CalendarViewModel: ViewModel() {
    var uiState by mutableStateOf(CalendarUiState())

    fun updateDisplayMonth(monthNumber: Int) {
        if (monthNumber > 0 && monthNumber <= 12)
            uiState = CalendarUiState(
                month = Month(monthNumber),
                year = uiState.year
            )
    }

    fun updateDisplayMonth(month: Month) {
        uiState = CalendarUiState(
            month = month,
            year = uiState.year
        )
    }

    fun updateDisplayYear(year: Int) {
        uiState = CalendarUiState(
            month = uiState.month,
            year = year
        )
    }

    fun updateUiState(uiState: CalendarUiState) {
        this.uiState = uiState
    }

    fun firstWeekDayOfMonth(): DayOfWeek = LocalDate(
        year = uiState.year,
        monthNumber = uiState.month.number,
        dayOfMonth = 1
    ).dayOfWeek

    fun isLeapYear(): Boolean =
        (GregorianCalendar.getInstance() as GregorianCalendar)
            .isLeapYear(uiState.year)

    fun daysOfMonth(): Int = uiState.month.numberOfDays(isLeapYear())
}

data class CalendarUiState(
    val month: Month = Month(Calendar.getInstance().get(Calendar.MONTH)+1),
    val year: Int = Calendar.getInstance().get(Calendar.YEAR),
)

/**
 * Returns the number of days in a month (Assumes Gregorian calendar).
 * If on Api Level >= 26, use Month.length instead
 * @param isLeapYear Indicates if the year is a leap year
 */
private fun Month.numberOfDays(isLeapYear: Boolean): Int = when(number) {
    1 -> 31
    2 -> if (isLeapYear) 29 else 28
    3 -> 31
    4 -> 30
    5 -> 31
    6 -> 30
    7 -> 31
    8 -> 31
    9 -> 30
    10 -> 31
    11 -> 30
    12 -> 31
    else -> 30
}