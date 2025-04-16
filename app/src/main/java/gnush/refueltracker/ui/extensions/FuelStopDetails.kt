package gnush.refueltracker.ui.extensions

import gnush.refueltracker.data.FuelStop
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.data.FuelStopDetails
import gnush.refueltracker.ui.data.NumberFormats

/**
 * Converts validated [FuelStopDetails] into [FuelStop].
 * @throws [NullPointerException] if called on unvalidated instance.
 */
fun FuelStopDetails.toFuelStop(formats: NumberFormats): FuelStop = FuelStop(
    id = id,
    station = station,
    fuelSort = fuelSort,
    pricePerVolume = formats.ratio.parse(pricePerVolume)!!.toString().toBigDecimal(),
    totalVolume = formats.volume.parse(totalVolume)!!.toString().toBigDecimal(),
    totalPrice = formats.currency.parse(totalPrice)!!.toString().toBigDecimal(),
    day = Config.DATE_FORMAT.parse(day),
    time = if (time != null) Config.TIME_FORMAT.parse(time) else null
)