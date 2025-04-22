package io.github.gnush.refueltracker.data

import androidx.room.TypeConverter
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.math.BigDecimal

class Converters {
    @TypeConverter
    fun bigDecimalFromString(value: String?): BigDecimal = if (value != null) BigDecimal(value) else BigDecimal.ZERO

    @TypeConverter
    fun bigDecimalToString(value: BigDecimal) = value.toString()

    /**
     * Converts a [LocalDate] to an [Int] with yyyyMMdd format
     * @param day The [LocalDate] to convert
     */
    @TypeConverter
    fun localDateToInt(day: LocalDate): Int = day.year * 10000 +
                                              day.monthNumber * 100 +
                                              day.dayOfMonth

    /**
     * Converts an [Int] to a [LocalDate]
     * @param day The [Int] to convert in yyyyMMdd format
     */
    @TypeConverter
    fun localDateFromInt(day: Int): LocalDate = LocalDate(
        year = day/10000,
        monthNumber = day%10000/100,
        dayOfMonth = day%100
    )

    /**
     * Converts [LocalTime] to [Int] with HHmmSS format
     * @param time The [LocalTime] to convert
     */
    @TypeConverter
    fun localTimeToInt(time: LocalTime): Int = time.hour * 10000 +
                                               time.minute * 100 +
                                               time.second

    /**
     * Converts [Int] to [LocalTime]
     * @param time The [Int] to convert in HHmmSS format
     */
    @TypeConverter
    fun localTimeFromInt(time: Int): LocalTime = LocalTime(
        hour = time/10000,
        minute = time%10000/100,
        second = time%100
    )
}