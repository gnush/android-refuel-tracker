package gnush.refueltracker.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import gnush.refueltracker.data.UserPreferencesRepository
import gnush.refueltracker.ui.DropDownSelection
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlin.reflect.KSuspendFunction1

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private var _uiState: MutableStateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = SettingsUiState(
                thousandsSeparator = Preference(
                    value = userPreferencesRepository.thousandsSeparatorPlaces.first().toString(),
                    isValid = true
                ),
                currencyDecimalPlaces = Preference(
                    value = userPreferencesRepository.currencyDecimalPlaces.first().toString(),
                    isValid = true
                ),
                volumeDecimalPlaces = Preference(
                    value = userPreferencesRepository.volumeDecimalPlaces.first().toString(),
                    isValid = true
                ),
                ratioDecimalPlaces = Preference(
                    value = userPreferencesRepository.currencyVolumeRatioDecimalPlaces.first().toString(),
                    isValid = true
                ),
                currencySign = userPreferencesRepository.defaultCurrencySign.first(),
                volumeSign = userPreferencesRepository.defaultVolumeSign.first(),
                numDropDownElements = Preference(
                    value = userPreferencesRepository.numberOfEntryScreenDropDownElements.first().toString(),
                    isValid = true
                ),
                defaultDropDownFilter = userPreferencesRepository.defaultEntryScreenDropDownSelection.first()
            )
        }
    }

    fun saveThousandsSeparator(n: String) {
        _uiState.value = _uiState.value.copy(
            thousandsSeparator = Preference(
                value = n,
                isValid = saveStringAsIntPreference(
                    value = n,
                    savePreference = userPreferencesRepository::saveThousandsSeparatorPlaces
                )
            )
        )
    }

    fun saveVolumeDecimalPlaces(n: String) {
        _uiState.value = _uiState.value.copy(
            volumeDecimalPlaces = Preference(
                value = n,
                isValid = saveStringAsIntPreference(
                    value = n,
                    savePreference = userPreferencesRepository::saveVolumeDecimalPreference
                )
            )
        )
    }

    fun saveCurrencyDecimalPlaces(n: String) {
        _uiState.value = _uiState.value.copy(
            currencyDecimalPlaces = Preference(
                value = n,
                isValid = saveStringAsIntPreference(
                    value = n,
                    savePreference = userPreferencesRepository::saveCurrencyDecimalPreference
                )
            )
        )
    }

    fun saveRatioDecimalPlaces(n: String) {
        _uiState.value = _uiState.value.copy(
            ratioDecimalPlaces = Preference(
                value = n,
                isValid = saveStringAsIntPreference(
                    value = n,
                    savePreference = userPreferencesRepository::saveCurrencyVolumeRatioDecimalPreferences
                )
            )
        )
    }

    fun saveCurrencySign(sign: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(currencySign = sign)
        userPreferencesRepository.saveDefaultCurrencyPreference(sign)
    }

    fun saveVolumeSign(sign: String) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(volumeSign = sign)
        userPreferencesRepository.saveDefaultVolumePreference(sign)
    }

    fun saveNumberOfDropDownElements(n: String) {
        _uiState.value = _uiState.value.copy(
            numDropDownElements = Preference(
                value = n,
                isValid = saveStringAsIntPreference(
                    value = n,
                    savePreference = userPreferencesRepository::saveDefaultNumberOfEntryScreenDropDownElements
                )
            )
        )
    }

    fun saveInitialDropDownFilter(filter: DropDownSelection) = viewModelScope.launch {
        userPreferencesRepository.saveDefaultEntryScreenDropDownSelection(filter)
    }

    private fun saveStringAsIntPreference(value: String, savePreference: KSuspendFunction1<Int, Unit>): Boolean = try {
        val i = value.toInt()

        viewModelScope.launch {
            savePreference(i)
        }
        true
    } catch (_: NumberFormatException) {
        Log.d("CONFIG_SAVE", "could not convert $value to Int")
        false
    }
}

data class SettingsUiState(
    val thousandsSeparator: Preference = Preference(),
    val volumeDecimalPlaces: Preference = Preference(),
    val currencyDecimalPlaces: Preference = Preference(),
    val ratioDecimalPlaces: Preference = Preference(),
    val currencySign: String = "",
    val volumeSign: String = "",
    val numDropDownElements: Preference = Preference(),
    val defaultDropDownFilter: DropDownSelection = DropDownSelection.MostUsed
)

data class Preference(
    val value: String = "",
    val isValid: Boolean = true
)