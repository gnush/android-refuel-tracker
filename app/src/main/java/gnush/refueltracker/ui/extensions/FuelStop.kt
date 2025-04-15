package gnush.refueltracker.ui.extensions

import kotlinx.datetime.format
import gnush.refueltracker.data.FuelStop
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.data.FuelStopDetails
import gnush.refueltracker.ui.data.FuelStopUiState

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