package org.refueltracker.ui.data

import kotlinx.datetime.format
import org.refueltracker.data.FuelStop
import org.refueltracker.ui.Config

data class FuelStopUiState(
    val details: FuelStopDetails = FuelStopDetails(),
    val isValid: Boolean = false
)

data class FuelStopDetails(
    val id: Int = 0,
    val station: String = "",
    val fuelSort: String = "",
    val pricePerVolume: String = "",
    val totalVolume: String = "",
    val totalPrice: String = "",
    val day: String = "",
    val time: String? = null
)

fun FuelStopDetails.toFuelStop(): FuelStop = FuelStop(
    id = id,
    station = station,
    fuelSort = fuelSort,
    pricePerVolume = pricePerVolume.toBigDecimal(),
    totalVolume = totalVolume.toBigDecimal(),
    totalPrice = totalPrice.toBigDecimal(),
    day = Config.DATE_FORMAT.parse(day),
    time = if (time != null) Config.TIME_FORMAT.parse(time) else null
)

fun FuelStopDetails.validate(): Boolean = station.isNotBlank()
                                         && fuelSort.isNotBlank()
                                         && try {
                                             pricePerVolume.toBigDecimal()
                                             totalVolume.toBigDecimal()
                                             totalPrice.toBigDecimal()

                                             Config.DATE_FORMAT.parse(day)
                                             if (time != null)
                                                 Config.TIME_FORMAT.parse(time)

                                             true
                                         } catch (_: Exception) {
                                             false
                                         }

fun FuelStop.toFuelStopUiState(isValid: Boolean = false): FuelStopUiState = FuelStopUiState(
    details = toFuelStopDetails(),
    isValid = isValid
)

fun FuelStop.toFuelStopDetails(): FuelStopDetails = FuelStopDetails(
    id = id,
    station = station,
    fuelSort = fuelSort,
    pricePerVolume = pricePerVolume.toString(),
    totalVolume = totalVolume.toString(),
    totalPrice = totalPrice.toString(),
    day = day.format(Config.DATE_FORMAT),
    time = time?.format(Config.TIME_FORMAT)
)