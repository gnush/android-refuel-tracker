package gnush.refueltracker.ui.fuelstop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.StateFlow
import gnush.refueltracker.data.FuelStopsRepository
import gnush.refueltracker.data.UserPreferencesRepository
import gnush.refueltracker.ui.createNumberFormat
import gnush.refueltracker.ui.data.DefaultSigns
import gnush.refueltracker.ui.data.FuelStopListUiState
import gnush.refueltracker.ui.data.NumberFormats
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
                    val thousandsSeparatorPlaces =
                        userPreferencesRepository.thousandsSeparatorPlaces.first()

                    _uiState.value = FuelStopListUiState(
                        fuelStops = it,
                        signs = DefaultSigns(
                            currency = userPreferencesRepository.defaultCurrencySign.first(),
                            volume = userPreferencesRepository.defaultVolumeSign.first()
                        ),
                        formats = NumberFormats(
                            currency = createNumberFormat(
                                thousandsSeparatorPlaces = thousandsSeparatorPlaces,
                                decimalPlaces = userPreferencesRepository.currencyDecimalPlaces.first()
                            ),
                            volume = createNumberFormat(
                                thousandsSeparatorPlaces = thousandsSeparatorPlaces,
                                decimalPlaces = userPreferencesRepository.volumeDecimalPlaces.first()
                            ),
                            ratio = createNumberFormat(
                                thousandsSeparatorPlaces = thousandsSeparatorPlaces,
                                decimalPlaces = userPreferencesRepository.currencyVolumeRatioDecimalPlaces.first()
                            ),
                        )
                    )
                }
        }
    }
}