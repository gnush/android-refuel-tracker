package gnush.refueltracker.ui.extensions

import android.util.Log
import gnush.refueltracker.data.FuelStop
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.data.FuelStopDetails
import java.math.RoundingMode

fun FuelStopDetails.updateBasedOnPricePerVolume(pricePerVolume: String) = copy(
    pricePerVolume = pricePerVolume,
    totalPrice = try {
        val ppv = Config.CURRENCY_VOLUME_RATIO_FORMAT.parse(pricePerVolume)?.toString() ?: ""
        val vol = Config.VOLUME_FORMAT.parse(totalVolume)?.toString() ?: ""

        (ppv.toBigDecimal() * vol.toBigDecimal())
            .currencyText
    } catch (_: Exception) {
        Log.d("ENTRY_UPDATE_ON_PPV", "could not convert '$pricePerVolume' or '$totalVolume'")
        totalPrice
    }
)

fun FuelStopDetails.updateBasedOnTotalVolume(totalVolume: String) = copy(
    totalVolume = totalVolume,
    totalPrice = try {
        val ppv = Config.CURRENCY_VOLUME_RATIO_FORMAT.parse(pricePerVolume)?.toString() ?: ""
        val vol = Config.VOLUME_FORMAT.parse(totalVolume)?.toString() ?: ""

        (ppv.toBigDecimal() * vol.toBigDecimal())
            .currencyText
    } catch (_: Exception) {
        Log.d("ENTRY_UPDATE_ON_VOL", "could not convert '$totalVolume' or '$totalPrice'")
        totalPrice
    }
)

fun FuelStopDetails.updateBasedOnTotalPrice(totalPrice: String) = copy(
    totalPrice = totalPrice,
    totalVolume = try {
        val ppvString = Config.CURRENCY_VOLUME_RATIO_FORMAT.parse(pricePerVolume)?.toString() ?: ""
        val ppv = ppvString.toBigDecimal()
        val price = Config.CURRENCY_FORMAT.parse(totalPrice)?.toString() ?: ""

        (price.toBigDecimal().divide(ppv, ppv.scale(), RoundingMode.HALF_UP))
            .volumeText
    } catch (e: ArithmeticException) {
        Log.d("ENTRY_UPDATE_ON_PRICE", "${e.message}")
        totalVolume
    } catch (_: Exception) {
        Log.d("ENTRY_UPDATE_ON_PRICE", "could not convert '$totalPrice' or '$totalVolume'")
        totalVolume
    }
)

fun FuelStopDetails.validate(): Boolean =
    station.isNotBlank()
    && fuelSort.isNotBlank()
    && Config.DATE_FORMAT.parseOrNull(day) != null
    && (if (time == null) true
        else Config.TIME_FORMAT.parseOrNull(time) != null)
    && try {
        Config.CURRENCY_VOLUME_RATIO_FORMAT.parse(pricePerVolume) != null
        && Config.VOLUME_FORMAT.parse(totalVolume) != null
        && Config.CURRENCY_FORMAT.parse(totalPrice) != null
    } catch (_: Exception) {
        false
    }

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