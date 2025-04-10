package org.refueltracker.ui.extensions

import android.util.Log
import org.refueltracker.data.FuelStop
import org.refueltracker.ui.Config
import org.refueltracker.ui.data.FuelStopDetails
import java.math.RoundingMode

fun FuelStopDetails.updateBasedOnPricePerVolume(pricePerVolume: String) = copy(
    pricePerVolume = pricePerVolume,
    totalPrice = try {
        (pricePerVolume.toBigDecimal() * totalVolume.toBigDecimal())
            .defaultText
    } catch (_: NumberFormatException) {
        Log.d("ENTRY_UPDATE_ON_PPV", "could not convert '$pricePerVolume' or '$totalVolume'")
        totalPrice
    }
)

fun FuelStopDetails.updateBasedOnTotalVolume(totalVolume: String) = copy(
    totalVolume = totalVolume,
    totalPrice = try {
        (pricePerVolume.toBigDecimal() * totalVolume.toBigDecimal())
            .defaultText
    } catch (_: NumberFormatException) {
        Log.d("ENTRY_UPDATE_ON_VOL", "could not convert '$totalVolume' or '$totalPrice'")
        totalPrice
    }
)

fun FuelStopDetails.updateBasedOnTotalPrice(totalPrice: String) = copy(
    totalPrice = totalPrice,
    totalVolume = try {
        val ppv = pricePerVolume.toBigDecimal()
        (totalPrice.toBigDecimal().divide(ppv, ppv.scale(), RoundingMode.HALF_UP))
            .defaultText
    } catch (_: NumberFormatException) {
        Log.d("ENTRY_UPDATE_ON_PRICE", "could not convert '$totalPrice' or '$totalVolume'")
        totalVolume
    } catch (e: ArithmeticException) {
        Log.d("ENTRY_UPDATE_ON_PRICE", "${e.message}")
        totalVolume
    }
)

fun FuelStopDetails.validate(): Boolean =
    station.isNotBlank()
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