package gnush.refueltracker.ui.extensions

import gnush.refueltracker.data.FuelStop
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.data.FuelStopDetails

fun FuelStopDetails.toFuelStop(): FuelStop = FuelStop(
    id = id,
    station = station,
    fuelSort = fuelSort,
    pricePerVolume = Config.CURRENCY_VOLUME_RATIO_FORMAT.parse(pricePerVolume)!!.toString().toBigDecimal(),
    totalVolume = Config.VOLUME_FORMAT.parse(totalVolume)!!.toString().toBigDecimal(),
    totalPrice = Config.CURRENCY_FORMAT.parse(totalPrice)!!.toString().toBigDecimal(),
    day = Config.DATE_FORMAT.parse(day),
    time = if (time != null) Config.TIME_FORMAT.parse(time) else null
)