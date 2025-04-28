package io.github.gnush.refueltracker.ui.fuelstop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import io.github.gnush.refueltracker.data.FuelStopsRepository
import io.github.gnush.refueltracker.data.UserPreferencesRepository
import io.github.gnush.refueltracker.ui.createNumberFormat
import io.github.gnush.refueltracker.ui.data.DefaultSigns
import io.github.gnush.refueltracker.ui.data.FuelStopListItem
import io.github.gnush.refueltracker.ui.data.FuelStopListUiState
import io.github.gnush.refueltracker.ui.data.UserFormats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FuelStopListViewModel(
    userPreferencesRepository: UserPreferencesRepository,
    val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    // TODO: - fuelStops in ui state from val to var?
    //       - fuelStops in ui state from List to Set?
    private var _uiState: MutableStateFlow<FuelStopListUiState> =
        MutableStateFlow(FuelStopListUiState())
    val uiState: StateFlow<FuelStopListUiState> = _uiState

    init {
        viewModelScope.launch {
            fuelStopsRepository.fuelStopsOrderedNewestFirst()
                .collect {
                    val separateLargeNumbers = userPreferencesRepository.groupLargeNumbers.first()

                    _uiState.value = FuelStopListUiState(
                        fuelStops = it.map { stop -> FuelStopListItem(stop) },
                        signs = DefaultSigns(
                            currency = userPreferencesRepository.defaultCurrencySign.first(),
                            volume = userPreferencesRepository.defaultVolumeSign.first()
                        ),
                        formats = UserFormats(
                            currency = createNumberFormat(
                                groupLargeNumbers = separateLargeNumbers,
                                fractionDigits = userPreferencesRepository.currencyDecimalPlaces.first()
                            ),
                            volume = createNumberFormat(
                                groupLargeNumbers = separateLargeNumbers,
                                fractionDigits = userPreferencesRepository.volumeDecimalPlaces.first()
                            ),
                            ratio = createNumberFormat(
                                groupLargeNumbers = separateLargeNumbers,
                                fractionDigits = userPreferencesRepository.currencyVolumeRatioDecimalPlaces.first()
                            ),
                            date = userPreferencesRepository.dateFormat.first()
                        )
                    )
                }
        }
    }

    fun toggleSelection(id: Long) {
        // TODO: stateful: isSelected from val to var?
        //_uiState.value.fuelStops.find { it.stop.id == id }?.isSelected = true
        _uiState.value = _uiState.value.copy(
            fuelStops = _uiState.value.fuelStops.map {
                if (id == it.stop.id)
                    it.copy(isSelected = !it.isSelected)
                else
                    it
            }
        )
    }

    fun deleteSelection() = viewModelScope.launch {
        val selected = _uiState.value.fuelStops.filter { it.isSelected }.toSet()

        selected.forEach {
            fuelStopsRepository.deleteFuelStop(it.stop.id)
        }

        _uiState.value = _uiState.value.copy(
            fuelStops = _uiState.value.fuelStops.minus(selected)
        )
    }
}