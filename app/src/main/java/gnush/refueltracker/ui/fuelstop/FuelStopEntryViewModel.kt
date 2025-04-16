package gnush.refueltracker.ui.fuelstop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import gnush.refueltracker.data.FuelStopsRepository
import gnush.refueltracker.data.UserPreferencesRepository
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.data.DefaultSigns
import gnush.refueltracker.ui.data.DropDownItemsUiState
import gnush.refueltracker.ui.data.FuelStopDetails
import gnush.refueltracker.ui.data.FuelStopUiState
import gnush.refueltracker.ui.extensions.toFuelStop
import gnush.refueltracker.ui.extensions.validate
import java.util.Calendar

class FuelStopEntryViewModel(
    userPreferences: UserPreferencesRepository,
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(FuelStopUiState(
        details = FuelStopDetails(
            day = LocalDate(
                year = Calendar.getInstance().get(Calendar.YEAR),
                monthNumber = Calendar.getInstance().get(Calendar.MONTH)+1,
                dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            ).format(Config.DATE_FORMAT)
        )
    ))
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                details = uiState.details.copy(
                    fuelSort = fuelStopsRepository.mostUsedFuelSort().first() ?: ""
                ),
                dropDownItems = DropDownItemsUiState(
                    fuelSortRecentDropDownItems = fuelStopsRepository.mostRecentFuelSorts(Config.DROP_DOWN_LENGTH).first(),
                    fuelSortUsedDropDownItems = fuelStopsRepository.mostUsedFuelSorts(Config.DROP_DOWN_LENGTH).first(),
                    stationRecentDropDownItems = fuelStopsRepository.mostRecentFuelStations(Config.DROP_DOWN_LENGTH).first(),
                    stationUsedDropDownItems = fuelStopsRepository.mostUsedFuelStations(Config.DROP_DOWN_LENGTH).first()
                ),
                userPreferences = DefaultSigns(
                    currencySign = userPreferences.defaultCurrencySign.first(),
                    volumeSign = userPreferences.defaultVolumeSign.first()
                )
            )
        }
    }

    /**
     * Updates the [uiState] with the provided values and validates the input values.
     */
    fun updateUiState(fuelStopDetails: FuelStopDetails) {
        uiState = uiState.copy(
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

