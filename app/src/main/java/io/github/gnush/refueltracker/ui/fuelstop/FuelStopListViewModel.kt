package io.github.gnush.refueltracker.ui.fuelstop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import io.github.gnush.refueltracker.data.FuelStopsRepository
import io.github.gnush.refueltracker.data.UserPreferencesRepository
import io.github.gnush.refueltracker.ui.createNumberFormat
import io.github.gnush.refueltracker.ui.data.DefaultSigns
import io.github.gnush.refueltracker.ui.data.FuelStopListUiState
import io.github.gnush.refueltracker.ui.data.UserFormats
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class FuelStopListViewModel(
    userPreferencesRepository: UserPreferencesRepository,
    fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    private var _uiState: MutableStateFlow<FuelStopListUiState> =
        MutableStateFlow(FuelStopListUiState())
    val uiState: StateFlow<FuelStopListUiState> = _uiState

    init {
        viewModelScope.launch {
            fuelStopsRepository.fuelStopsOrderedNewestFirst()
                .collect {
                    val separateLargeNumbers = userPreferencesRepository.groupLargeNumbers.first()

                    _uiState.value = FuelStopListUiState(
                        fuelStops = it,
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
}