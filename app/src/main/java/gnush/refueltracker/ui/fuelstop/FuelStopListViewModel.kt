package gnush.refueltracker.ui.fuelstop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import gnush.refueltracker.data.FuelStopsRepository
import gnush.refueltracker.data.UserPreferencesRepository
import gnush.refueltracker.ui.data.DefaultSigns
import gnush.refueltracker.ui.data.FuelStopListUiState
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
                    _uiState.value = FuelStopListUiState(
                        fuelStops = it,
                        userPreferences = DefaultSigns(
                            currencySign = userPreferencesRepository.defaultCurrencySign.first(),
                            volumeSign = userPreferencesRepository.defaultVolumeSign.first()
                        )
                    )
                }
        }
    }
}