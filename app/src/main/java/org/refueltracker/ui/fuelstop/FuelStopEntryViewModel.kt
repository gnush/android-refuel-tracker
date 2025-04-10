package org.refueltracker.ui.fuelstop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import org.refueltracker.data.FuelStopsRepository
import org.refueltracker.ui.data.FuelStopDetails
import org.refueltracker.ui.data.FuelStopUiState
import org.refueltracker.ui.extensions.toFuelStop
import org.refueltracker.ui.extensions.validate

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
            isValid = fuelStopDetails.validate()
        )
    }

    /**
     * Stores the current [FuelStopUiState] in the Database if the inputs are validated
     */
    suspend fun saveFuelStop() {
        if (uiState.details.validate())
            fuelStopsRepository.insert(uiState.details.toFuelStop())
    }
}

