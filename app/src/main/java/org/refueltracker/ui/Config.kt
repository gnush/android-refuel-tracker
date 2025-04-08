package org.refueltracker.ui

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional

// TODO: Make config... configurable!
object Config {
    val DATE_FORMAT: DateTimeFormat<LocalDate> =
        LocalDate.Format { dayOfMonth(); char('.'); monthNumber(); char('.'); year() }
    val TIME_FORMAT: DateTimeFormat<LocalTime> =
        LocalTime.Format {
            hour(); char(':'); minute(); optional { char(':'); second() }
        }

    // requires at least api 26: bump min sdk?
    //   and introduce DATE_FORMAT_PATTERN: String config (same for time)
    // val foo = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")

    const val DISPLAY_CURRENCY_SIGN: String = "€"
    const val DISPLAY_VOLUME_SIGN: String = "L"

    const val CURRENCY_DECIMAL_PLACES_DEFAULT = 2
    const val VOLUME_DECIMAL_PLACES_DEFAULT = 2
}