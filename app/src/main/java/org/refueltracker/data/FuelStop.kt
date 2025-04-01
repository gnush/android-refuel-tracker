package org.refueltracker.data

data class FuelStop(
    val station: String,
    val pricePerVolume: Double,
    val totalVolume: Double,
    val totalPrice: Double
)