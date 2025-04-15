package gnush.refueltracker.ui

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

    ///////////////
    // TODO: remove from config and migrate to preference store
    private const val VOLUME_DECIMAL_PLACES = 2
    private const val CURRENCY_DECIMAL_PLACES = 2
    private const val CURRENCY_VOLUME_RATIO_DECIMAL_PLACES = 3
    const val DISPLAY_CURRENCY_SIGN: String = "€"
    const val DISPLAY_VOLUME_SIGN: String = "L"

    const val DROP_DOWN_LENGTH = 5
    val DROP_DOWN_SELECTION = DropDownSelection.MostUsed
    //////////////

    val VOLUME_FORMAT: NumberFormat = DecimalFormat("#,##0.${(1..VOLUME_DECIMAL_PLACES).toList().map { "0" }.fold(""){ x, xs -> x+xs }}")
    val CURRENCY_FORMAT: NumberFormat = DecimalFormat("#,##0.${(1..CURRENCY_DECIMAL_PLACES).toList().map { "0" }.fold(""){ x, xs -> x+xs }}")
    val CURRENCY_VOLUME_RATIO_FORMAT: NumberFormat = DecimalFormat("#,##0.${(1..CURRENCY_VOLUME_RATIO_DECIMAL_PLACES).toList().map { "0" }.fold(""){ x, xs -> x+xs }}")

    // TODO: use this or provide a predefined set of date formats to pick from?
    var foo = "dd.MM.uuuu"
    var bar = "HH:mm"
    fun baz1(): DateTimeFormat<LocalDate> = LocalDate.Format {
        byUnicodePattern(foo)
    }
    fun baz2(): DateTimeFormat<LocalTime> = LocalTime.Format {
        byUnicodePattern(bar)
    }
}

enum class DropDownSelection {
    MostRecent,
    MostUsed
}