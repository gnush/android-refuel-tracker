package org.refueltracker.data

import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import java.math.BigDecimal

data class FuelStop(
    val id: Int = 0,
    val station: String,
    val fuelSort: String,
    val volumeSign: String,
    val currencySign: String,
    val pricePerVolume: BigDecimal,
    val totalVolume: BigDecimal,
    val totalPrice: BigDecimal,
    val day: LocalDate,
    val time: LocalTime? = null
)