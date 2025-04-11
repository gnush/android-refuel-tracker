package org.refueltracker.ui.fuelstop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import org.refueltracker.data.FuelStopsRepository
import org.refueltracker.ui.Config
import org.refueltracker.ui.data.FuelStopDetails
import org.refueltracker.ui.data.FuelStopUiState
import org.refueltracker.ui.extensions.toFuelStop
import org.refueltracker.ui.extensions.validate
import java.util.Calendar

class FuelStopEntryViewModel(
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(FuelStopUiState(
        details = FuelStopDetails(
            day = LocalDate(
                year = Calendar.getInstance().get(Calendar.YEAR),
                monthNumber = Calendar.getInstance().get(Calendar.MONTH),
                dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            ).format(Config.DATE_FORMAT)
        )
    ))
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                details = uiState.details.copy(
                    fuelSort = fuelStopsRepository.mostUsedFuelSort().first()
                )
            )
        }
    }

    /**
     * Updates the [uiState] with the provided values and validates the input values.
     */
    fun updateUiState(fuelStopDetails: FuelStopDetails) {
        uiState = FuelStopUiState(
            details = fuelStopDetails,
            isValid = fuelStopDetails.validate()
        )
    }

    suspend fun dropDownFuelSorts(recent: Boolean = false): List<String> = when (recent) {
        true -> fuelStopsRepository.mostRecentFuelSorts(10).first()
        false -> fuelStopsRepository.mostUsedFuelSorts(10).first()
    }

    suspend fun dropDownStations(recent: Boolean = false): List<String> = when (recent) {
        true -> fuelStopsRepository.mostRecentFuelStations(10).first()
        false -> fuelStopsRepository.mostUsedFuelStations(10).first()
    }

    /**
     * Stores the current [FuelStopUiState] in the Database if the inputs are validated
     */
    suspend fun saveFuelStop() {
        if (uiState.details.validate())
            fuelStopsRepository.insert(uiState.details.toFuelStop())
    }
}

