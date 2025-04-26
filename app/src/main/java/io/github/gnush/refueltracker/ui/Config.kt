package io.github.gnush.refueltracker.ui

import androidx.annotation.IntRange
import androidx.annotation.StringRes
import io.github.gnush.refueltracker.R
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional
import java.text.NumberFormat

object Config {
    val TIME_FORMAT: DateTimeFormat<LocalTime> = LocalTime.Format {
        hour(); char(':'); minute(); optional { char(':'); second() }
    }
}

fun createNumberFormat(groupLargeNumbers: Boolean, @IntRange(from = 0) fractionDigits: Int): NumberFormat {
    val format = NumberFormat.getInstance()

    format.isGroupingUsed = groupLargeNumbers
    format.minimumFractionDigits = fractionDigits

    return format
}

enum class DropDownSelection: Displayable {
    MostRecent {
        @StringRes override val displayText: Int = R.string.drop_down_selection_most_recent_name
    },
    MostUsed {
        @StringRes override val displayText: Int = R.string.drop_down_selection_most_used_name
    }
}

interface Displayable {
    @get:StringRes val displayText: Int
}