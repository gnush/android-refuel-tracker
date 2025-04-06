package org.refueltracker.ui

import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char

object Config {
    val DATE_FORMAT: DateTimeFormat<LocalDate> =
        LocalDate.Format { dayOfMonth(); char('.'); monthNumber(); char('.'); year() }

    val CURRENCY_SIGN: String = "€"
    val VOLUME_SIGN: String = "L"
}