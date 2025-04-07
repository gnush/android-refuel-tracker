package org.refueltracker.ui.fuelstop

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import org.refueltracker.data.FuelStop
import org.refueltracker.data.FuelStopsRepository
import org.refueltracker.ui.Config

class FuelStopEntryViewModel(
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(FuelStopUiState())
        private set

    /**
     * Updates the [uiState] with the provided values and validates the input values.
     */
    fun updateUiState(fuelStopDetails: FuelStopDetails) {
        uiState = FuelStopUiState(
            details = fuelStopDetails,
            isValid = validateInput(fuelStopDetails)
        )
    }

    private fun validateInput(fuelStopDetails: FuelStopDetails = uiState.details): Boolean = with(fuelStopDetails) {
        Log.d("ME", "validateInput for $fuelStopDetails")
        val conversionsSucceed: Boolean = try {
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

        station.isNotBlank() && fuelSort.isNotBlank() && conversionsSucceed
    }

    /**
     * Stores the current [FuelStopUiState] in the Database if the inputs are validated
     */
    suspend fun saveFuelStop() {
        if (validateInput())
            fuelStopsRepository.insert(uiState.details.toFuelStop())
    }
}

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

fun FuelStop.toFuelStopDetails(): FuelStopDetails = FuelStopDetails(
    id = id,
    station = station,
    fuelSort = fuelSort,
    pricePerVolume = pricePerVolume.toString(),
    totalVolume = totalVolume.toString(),
    day = day.format(Config.DATE_FORMAT),
    time = time?.format(Config.TIME_FORMAT)
)