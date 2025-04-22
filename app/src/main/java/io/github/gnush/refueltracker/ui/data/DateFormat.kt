package io.github.gnush.refueltracker.ui.data

import androidx.annotation.StringRes
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.ui.Displayable
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char

sealed interface DateFormat: Displayable {
    val get: DateTimeFormat<LocalDate>
}

data object ISO: DateFormat {
    @StringRes override val displayText: Int = R.string.date_format_iso
    override val get: DateTimeFormat<LocalDate> = LocalDate.Formats.ISO
}

data object DIN: DateFormat {
    @StringRes override val displayText: Int = R.string.date_format_din

    override val get: DateTimeFormat<LocalDate> = LocalDate.Format {
        dayOfMonth(); char('.'); monthNumber(); char('.'); year()
    }
}

data object ANSI: DateFormat {
    @StringRes override val displayText: Int = R.string.date_format_ansi

    override val get: DateTimeFormat<LocalDate> = LocalDate.Format {
        monthNumber(); char('/'); dayOfMonth(); char('/'); year()
    }
}

class CustomDateFormat(private val pattern: String): DateFormat {
    @StringRes override val displayText: Int = R.string.date_format_custom

    @OptIn(FormatStringsInDatetimeFormats::class)
    override val get: DateTimeFormat<LocalDate> = LocalDate.Format {
        byUnicodePattern(pattern)
    }
}