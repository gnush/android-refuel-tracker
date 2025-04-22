package io.github.gnush.refueltracker.ui.extensions

import kotlinx.datetime.format
import io.github.gnush.refueltracker.data.FuelStop
import io.github.gnush.refueltracker.ui.Config
import io.github.gnush.refueltracker.ui.data.FuelStopDetails
import io.github.gnush.refueltracker.ui.data.FuelStopUiState
import io.github.gnush.refueltracker.ui.data.NumberFormats

fun FuelStop.toFuelStopUiState(formats: NumberFormats, isValid: Boolean = false): FuelStopUiState = FuelStopUiState(
    details = toFuelStopDetails(formats),
    isValid = isValid
)

fun FuelStop.toFuelStopDetails(formats: NumberFormats = NumberFormats()): FuelStopDetails = FuelStopDetails(
    id = id,
    station = station,
    fuelSort = fuelSort,
    pricePerVolume = pricePerVolume.format(formats.ratio),
    totalVolume = totalVolume.format(formats.volume),
    totalPrice = totalPrice.format(formats.currency),
    day = day.format(Config.DATE_FORMAT),
    time = time?.format(Config.TIME_FORMAT)
)