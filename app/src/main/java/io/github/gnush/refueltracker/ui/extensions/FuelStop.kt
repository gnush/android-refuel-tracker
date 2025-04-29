package io.github.gnush.refueltracker.ui.extensions

import kotlinx.datetime.format
import io.github.gnush.refueltracker.data.FuelStop
import io.github.gnush.refueltracker.ui.Config
import io.github.gnush.refueltracker.ui.data.FuelStopDetails
import io.github.gnush.refueltracker.ui.data.UserFormats

fun FuelStop.toFuelStopDetails(formats: UserFormats = UserFormats()): FuelStopDetails = FuelStopDetails(
    id = id,
    station = station,
    fuelSort = fuelSort,
    pricePerVolume = pricePerVolume.format(formats.ratio),
    totalVolume = totalVolume.format(formats.volume),
    totalPrice = totalPrice.format(formats.currency),
    day = day.format(formats.date.get),
    time = time?.format(Config.TIME_FORMAT)
)