package gnush.refueltracker.ui.fuelstop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import gnush.refueltracker.data.FuelStopsRepository
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.data.DropDownItemsUiState
import gnush.refueltracker.ui.data.FuelStopDetails
import gnush.refueltracker.ui.data.FuelStopUiState
import gnush.refueltracker.ui.extensions.toFuelStop
import gnush.refueltracker.ui.extensions.toFuelStopUiState
import gnush.refueltracker.ui.extensions.validate

class FuelStopEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(FuelStopUiState())
        private set

    private val fuelStopId: Int = checkNotNull(savedStateHandle[FuelStopEditDestination.FUEL_STOP_ID])

    init {
        viewModelScope.launch {
            uiState = fuelStopsRepository.fuelStop(fuelStopId)
                .filterNotNull()
                .first()
                .toFuelStopUiState().copy(
                    dropDownItems = DropDownItemsUiState(
                        fuelSortRecentDropDownItems = fuelStopsRepository.mostRecentFuelSorts(Config.DROP_DOWN_LENGTH).first(),
                        fuelSortUsedDropDownItems = fuelStopsRepository.mostUsedFuelSorts(Config.DROP_DOWN_LENGTH).first(),
                        stationRecentDropDownItems = fuelStopsRepository.mostRecentFuelStations(Config.DROP_DOWN_LENGTH).first(),
                        stationUsedDropDownItems = fuelStopsRepository.mostUsedFuelStations(Config.DROP_DOWN_LENGTH).first()
                    )
                )
        }
    }

    fun updateUiState(fuelStopDetails: FuelStopDetails) {
        uiState = uiState.copy(
            details = fuelStopDetails,
            isValid = fuelStopDetails.validate()
        )
    }

    suspend fun updateFuelStop() {
        if (uiState.details.validate())
            fuelStopsRepository.update(uiState.details.toFuelStop())
    }
}