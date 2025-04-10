package org.refueltracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.math.BigDecimal

@Entity(tableName = "fuel_stops")
data class FuelStop(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
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