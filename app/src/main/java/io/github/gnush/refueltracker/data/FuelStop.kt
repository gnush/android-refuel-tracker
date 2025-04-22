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
            childColumns = ["stationId"],
            onDelete = ForeignKey.RESTRICT
        ),
        ForeignKey(
            entity = FuelSort::class,
            parentColumns = ["id"],
            childColumns = ["fuelSortId"],
            onDelete = ForeignKey.RESTRICT
        )
    ]
)
data class FuelStopEntity(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    @ColumnInfo(index = true) val stationId: Long = 0,
    @ColumnInfo(index = true) val fuelSortId: Long = 0,
    val pricePerVolume: BigDecimal,
    val totalVolume: BigDecimal,
    val totalPrice: BigDecimal,
    val day: LocalDate,
    val time: LocalTime? = null
)

data class FuelStop(
    val id: Long = 0L,
    val station: String,
    val fuelSort: String,
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