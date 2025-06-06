package io.github.gnush.refueltracker.ui.calendar

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.number
import io.github.gnush.refueltracker.ui.extensions.numberOfDays
import java.util.GregorianCalendar

class CalendarViewModel: ViewModel() {
    var uiState by mutableStateOf(CalendarUiState())

    fun updateDisplayMonth(year: Int, month: Month) {
        if (month.number in 1..12)
            uiState = CalendarUiState(
                month = month,
                year = year
            )
    }

    fun updateDisplayMonth(month: Month) {
        uiState = CalendarUiState(
            month = month,
            year = uiState.year
        )
    }

    fun setPreviousMonth() {
        uiState = uiState.previousMonth()
    }

    fun setNextMonth() {
        uiState = uiState.nextMonth()
    }

    fun updateDisplayYear(year: Int) {
        uiState = CalendarUiState(
            month = uiState.month,
            year = year
        )
    }

    fun firstWeekDayOfMonth(): DayOfWeek = LocalDate(
        year = uiState.year,
        monthNumber = uiState.month.number,
        dayOfMonth = 1
    ).dayOfWeek

    private fun isLeapYear(): Boolean =
        (GregorianCalendar.getInstance() as GregorianCalendar)
            .isLeapYear(uiState.year)

    fun daysOfMonth(): Int = uiState.month.numberOfDays(isLeapYear())
}