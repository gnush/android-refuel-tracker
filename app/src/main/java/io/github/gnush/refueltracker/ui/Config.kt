package io.github.gnush.refueltracker.ui

import androidx.annotation.StringRes
import io.github.gnush.refueltracker.R
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional
import java.text.DecimalFormat
import java.text.NumberFormat

// TODO: Make config... configurable!
@OptIn(FormatStringsInDatetimeFormats::class)
object Config {
    val DATE_FORMAT: DateTimeFormat<LocalDate> = LocalDate.Format {
        dayOfMonth(); char('.'); monthNumber(); char('.'); year()
    }
    val TIME_FORMAT: DateTimeFormat<LocalTime> = LocalTime.Format {
        hour(); char(':'); minute(); optional { char(':'); second() }
    }

    // TODO: use this or provide a predefined set of date formats to pick from?
    // predefined set and custom pattern
    var foo = "dd.MM.uuuu"
    var bar = "HH:mm"
    fun baz1(): DateTimeFormat<LocalDate> = LocalDate.Format {
        byUnicodePattern(foo)
    }
    fun baz2(): DateTimeFormat<LocalTime> = LocalTime.Format {
        byUnicodePattern(bar)
    }
}

fun createNumberFormat(separateLargeNumbers: Boolean, thousandsSeparatorPlaces: Int, decimalPlaces: Int): NumberFormat = when {
    separateLargeNumbers && thousandsSeparatorPlaces > 0 && decimalPlaces > 0 ->
        DecimalFormat("#,${"#".repeat(thousandsSeparatorPlaces-1)}0.${"0".repeat(decimalPlaces)}")
    !separateLargeNumbers && decimalPlaces > 0 ->
        DecimalFormat("0.${"0".repeat(decimalPlaces)}")
    else -> NumberFormat.getInstance()
}

enum class DropDownSelection(@StringRes val displayText: Int) {
    MostRecent(R.string.drop_down_selection_most_recent_name),
    MostUsed(R.string.drop_down_selection_most_used_name)
}