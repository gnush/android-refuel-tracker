package io.github.gnush.refueltracker.ui.extensions

import io.github.gnush.refueltracker.data.FuelStop
import io.github.gnush.refueltracker.ui.Config
import io.github.gnush.refueltracker.ui.data.DefaultSigns
import io.github.gnush.refueltracker.ui.data.FuelStopDetails
import io.github.gnush.refueltracker.ui.data.NumberFormats

/**
 * Converts validated [FuelStopDetails] into [FuelStop].
 * @throws [NullPointerException] if called on unvalidated instance.
 */
fun FuelStopDetails.toFuelStop(formats: NumberFormats, signs: DefaultSigns): FuelStop = FuelStop(
    id = id,
    station = station,
    fuelSort = fuelSort,
    pricePerVolume = formats.ratio.parse(pricePerVolume)!!.toString().toBigDecimal(),
    totalVolume = formats.volume.parse(totalVolume)!!.toString().toBigDecimal(),
    totalPrice = formats.currency.parse(totalPrice)!!.toString().toBigDecimal(),
    day = Config.DATE_FORMAT.parse(day),
    time = if (time != null) Config.TIME_FORMAT.parse(time) else null,
    currency = signs.currency,
    volume = signs.volume
)