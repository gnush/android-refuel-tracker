package org.refueltracker.ui.fuelstop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import org.refueltracker.data.FuelStop
import org.refueltracker.data.FuelStopsRepository
import org.refueltracker.ui.data.FuelStopListUiState

class FuelStopListViewModel(
    val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    val uiState: StateFlow<FuelStopListUiState> = fuelStopsRepository
        .fuelStopsOrderedNewestFirst()
        .map { FuelStopListUiState(it) }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
            initialValue = FuelStopListUiState()
        )

    companion object {
        private const val TIMEOUT_MILLIS = 5000L
    }
}