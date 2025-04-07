package org.refueltracker.ui

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional

object Config {
    val DATE_FORMAT: DateTimeFormat<LocalDate> =
        LocalDate.Format { dayOfMonth(); char('.'); monthNumber(); char('.'); year() }
    val TIME_FORMAT: DateTimeFormat<LocalTime> =
        LocalTime.Format {
            hour(); char(':'); minute(); optional { char(':'); second() }
        }

    const val DISPLAY_CURRENCY_SIGN: String = "€"
    const val DISPLAY_VOLUME_SIGN: String = "L"
}