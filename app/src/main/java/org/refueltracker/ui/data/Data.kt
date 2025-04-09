package org.refueltracker.ui.data

import android.util.Log
import kotlinx.datetime.format
import org.refueltracker.data.FuelStop
import org.refueltracker.ui.Config
import java.math.RoundingMode

data class FuelStopListUiState(val fuelStops: List<FuelStop> = listOf())

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

fun FuelStopDetails.updateBasedOnPricePerVolume(pricePerVolume: String) = copy(
    pricePerVolume = pricePerVolume,
    totalPrice = try {
        (pricePerVolume.toBigDecimal() * totalVolume.toBigDecimal())
            .setScale(
                Config.CURRENCY_DECIMAL_PLACES_DEFAULT,
                RoundingMode.HALF_UP
            ).toString()
    } catch (_: Exception) {
        totalPrice
    }
)

fun FuelStopDetails.updateBasedOnTotalVolume(totalVolume: String) = copy(
    totalVolume = totalVolume,
    totalPrice = try {
        (pricePerVolume.toBigDecimal() * totalVolume.toBigDecimal())
            .setScale(
                Config.CURRENCY_DECIMAL_PLACES_DEFAULT,
                RoundingMode.HALF_UP
            ).toString()
    } catch (_: Exception) {
        totalPrice
    }
)

fun FuelStopDetails.updateBasedOnTotalPrice(totalPrice: String) = copy(
    totalPrice = totalPrice,
    totalVolume = try {
        val ppv = pricePerVolume.toBigDecimal()
        (totalPrice.toBigDecimal().divide(ppv, ppv.scale(), RoundingMode.HALF_UP))
            .setScale(
                Config.VOLUME_DECIMAL_PLACES_DEFAULT,
                RoundingMode.HALF_UP
            ).toString()
    } catch (_: Exception) {
        Log.d("ME", "oopsies")
        totalVolume
    }
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