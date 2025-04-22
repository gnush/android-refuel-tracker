package io.github.gnush.refueltracker.ui.extensions

import androidx.annotation.StringRes
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import io.github.gnush.refueltracker.R

/**
 * The [StringRes] associated with this [DayOfWeek] instance.
 */
val DayOfWeek.abbreviationId: Int
    @StringRes get() = when(isoDayNumber) {
        1 -> R.string.day_of_week_abbreviation_1
        2 -> R.string.day_of_week_abbreviation_2
        3 -> R.string.day_of_week_abbreviation_3
        4 -> R.string.day_of_week_abbreviation_4
        5 -> R.string.day_of_week_abbreviation_5
        6 -> R.string.day_of_week_abbreviation_6
        7 -> R.string.day_of_week_abbreviation_7
        else -> R.string.day_of_week_abbreviation_1
    }

/**
 * The [StringRes] associated with this [Month] instance.
 */
val Month.monthOfYearId: Int
    @StringRes get() = when(number) {
        1 -> R.string.month_1
        2 -> R.string.month_2
        3 -> R.string.month_3
        4 -> R.string.month_4
        5 -> R.string.month_5
        6 -> R.string.month_6
        7 -> R.string.month_7
        8 -> R.string.month_8
        9 -> R.string.month_9
        10 -> R.string.month_10
        11 -> R.string.month_11
        12 -> R.string.month_12
        else -> R.string.month_1
    }

/**
 * Returns the number of days in a month (Assumes Gregorian calendar).
 * If on Api Level >= 26, use Month.length instead
 * @param isLeapYear Indicates if the year is a leap year
 */
fun Month.numberOfDays(isLeapYear: Boolean): Int = when(number) {
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