package org.refueltracker.ui.data

data class FuelStopDetails(
    val id: Long = 0L,
    val station: String = "",
    val fuelSort: String = "",
    val pricePerVolume: String = "",
    val totalVolume: String = "",
    val totalPrice: String = "",
    val day: String = "",
    val time: String? = null
)