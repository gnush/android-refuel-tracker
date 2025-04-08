package org.refueltracker.ui.fuelstop

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.refueltracker.data.FuelStopsRepository
import org.refueltracker.ui.Config
import org.refueltracker.ui.data.FuelStopDetails
import org.refueltracker.ui.data.FuelStopUiState
import org.refueltracker.ui.data.toFuelStop

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

