package gnush.refueltracker.ui

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format.DateTimeFormat
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlinx.datetime.format.char
import kotlinx.datetime.format.optional
import java.math.BigDecimal
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

    val NUMBER_FORMAT: NumberFormat = NumberFormat.getInstance()

//    val saddsa: BigDecimal = NUMBER_FORMAT.parse("1.2223") as BigDecimal

    // MonthNames list must contain 12 entries
//    val CALENDAR_MONTH_AND_YEAR = LocalDate.Format {
//        monthName(names = MonthNames()); char(' '); year()
//    }

    // TODO: use this or provide a predefined set of date formats to pick from?
    var foo = "dd.MM.uuuu"
    var bar = "HH:mm"
    fun baz1(): DateTimeFormat<LocalDate> = LocalDate.Format {
        byUnicodePattern(foo)
    }
    fun baz2(): DateTimeFormat<LocalTime> = LocalTime.Format {
        byUnicodePattern(bar)
    }

    // requires at least api 26: bump min sdk? (done) //maybe revert again and keep using kotlinx.datetime
    //                                                //requires to change calendar getWeekdays, as DayOfWeek.MONDAY enum not accessible
    //   and introduce DATE_FORMAT_PATTERN: String config (same for time)
    // val foo = java.time.format.DateTimeFormatter.ofPattern("dd.MM.yyyy")

    const val DISPLAY_CURRENCY_SIGN: String = "€"
    const val DISPLAY_VOLUME_SIGN: String = "L"

    const val DECIMAL_PLACES_DEFAULT = 2
    const val DECIMAL_PLACES_SPECIAL = 3

    const val DROP_DOWN_LENGTH = 5
    val DROP_DOWN_SELECTION = DropDownSelection.MostUsed
}

enum class DropDownSelection {
    MostRecent,
    MostUsed
}