package io.github.gnush.refueltracker.ui

import androidx.annotation.StringRes
import io.github.gnush.refueltracker.R
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional
import java.text.DecimalFormat
import java.text.NumberFormat

object Config {
    val TIME_FORMAT: DateTimeFormat<LocalTime> = LocalTime.Format {
        hour(); char(':'); minute(); optional { char(':'); second() }
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