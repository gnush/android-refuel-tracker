package io.github.gnush.refueltracker.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.math.BigDecimal

@Entity(
    tableName = "fuel_stops",
    foreignKeys = [
        ForeignKey(
            entity = FuelStation::class,
            parentColumns = ["id"],
            childColumns = ["station_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = FuelSort::class,
            parentColumns = ["id"],
            childColumns = ["fuel_sort_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Currency::class,
            parentColumns = ["id"],
            childColumns = ["currency_id"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = Volume::class,
            parentColumns = ["id"],
            childColumns = ["volume_id"],
            onDelete = ForeignKey.RESTRICT
        ),
    ]
)
data class FuelStopEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(index = true, name = "station_id") val stationId: Long = 0L,
    @ColumnInfo(index = true, name = "fuel_sort_id") val fuelSortId: Long = 0L,
    @ColumnInfo(index = true, name = "currency_id") val currencyId: Long = 0L,
    @ColumnInfo(index = true, name = "volume_id") val volumeId: Long = 0L,
    val pricePerVolume: BigDecimal,
    val totalVolume: BigDecimal,
    val totalPrice: BigDecimal,
    val day: LocalDate,
    val time: LocalTime? = null,
)

data class FuelStop(
    val id: Long = 0L,
    val station: String,
    val fuelSort: String,
    val currency: String,
    val volume: String,
    val pricePerVolume: BigDecimal,
    val totalVolume: BigDecimal,
    val totalPrice: BigDecimal,
    val day: LocalDate,
    val time: LocalTime? = null
)

data class FuelStopAverageValues(
    val pricePerVolume: BigDecimal = BigDecimal.ZERO,
    val volume: BigDecimal = BigDecimal.ZERO,
    val price: BigDecimal = BigDecimal.ZERO
) {
    operator fun minus(other: FuelStopAverageValues) = FuelStopAverageValues(
        pricePerVolume = this.pricePerVolume - other.pricePerVolume,
        volume = this.volume - other.volume,
        price = this.price - other.price
    )
}

data class FuelStopSumValues(
    val volume: BigDecimal = BigDecimal.ZERO,
    val price: BigDecimal = BigDecimal.ZERO
)