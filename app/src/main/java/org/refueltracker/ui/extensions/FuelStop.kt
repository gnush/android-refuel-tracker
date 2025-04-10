package org.refueltracker.ui.extensions

import kotlinx.datetime.format
import org.refueltracker.data.FuelStop
import org.refueltracker.ui.Config
import org.refueltracker.ui.data.FuelStopDetails
import org.refueltracker.ui.data.FuelStopUiState

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