package gnush.refueltracker.ui.config

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

class ConfigViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private var _uiState: MutableStateFlow<ConfigUiState> =
        MutableStateFlow(ConfigUiState())
    val uiState: StateFlow<ConfigUiState> = _uiState

    init {
        viewModelScope.launch {
            _uiState.value = ConfigUiState(
                thousandsSeparator = userPreferencesRepository.thousandsSeparatorPlaces.first(),
                currencyDecimalPlaces = userPreferencesRepository.currencyDecimalPlaces.first(),
                volumeDecimalPlaces = userPreferencesRepository.volumeDecimalPlaces.first(),
                ratioDecimalPlaces = userPreferencesRepository.currencyVolumeRatioDecimalPlaces.first(),
                currencySign = userPreferencesRepository.defaultCurrencySign.first(),
                volumeSign = userPreferencesRepository.defaultVolumeSign.first(),
                numDropDownElements = userPreferencesRepository.numberOfEntryScreenDropDownElements.first(),
                defaultDropDownFilter = userPreferencesRepository.defaultEntryScreenDropDownSelection.first()
            )
        }
    }

    fun saveThousandsSeparator(n: String) = saveStringAsIntPreference(
        value = n,
        savePreference = userPreferencesRepository::saveThousandsSeparatorPlaces
    )

    fun saveVolumeDecimalPlaces(n: String) = saveStringAsIntPreference(
        value = n,
        savePreference = userPreferencesRepository::saveVolumeDecimalPreference
    )

    fun saveCurrencyDecimalPlaces(n: String) = saveStringAsIntPreference(
        value = n,
        savePreference = userPreferencesRepository::saveCurrencyDecimalPreference
    )

    fun saveRatioDecimalPlaces(n: String) = saveStringAsIntPreference(
        value = n,
        savePreference = userPreferencesRepository::saveCurrencyVolumeRatioDecimalPreferences
    )

    fun saveCurrencySign(sign: String) = viewModelScope.launch {
        userPreferencesRepository.saveDefaultCurrencyPreference(sign)
    }

    fun saveVolumeSign(sign: String) = viewModelScope.launch {
        userPreferencesRepository.saveDefaultVolumePreference(sign)
    }

    fun saveNumberOfDropDownElements(n: String) = saveStringAsIntPreference(
        value = n,
        savePreference = userPreferencesRepository::saveDefaultNumberOfEntryScreenDropDownElements
    )

    fun saveInitialDropDownFilter(filter: DropDownSelection) = viewModelScope.launch {
        userPreferencesRepository.saveDefaultEntryScreenDropDownSelection(filter)
    }

    private fun saveStringAsIntPreference(value: String, savePreference: KSuspendFunction1<Int, Unit>) = try {
        val i = value.toInt()

        viewModelScope.launch {
            savePreference(i)
        }
    } catch (_: NumberFormatException) {
        Log.d("CONFIG_SAVE", "could not convert $value to Int")
    }
}

data class ConfigUiState(
    val thousandsSeparator: Int = 0,
    val volumeDecimalPlaces: Int = 0,
    val currencyDecimalPlaces: Int = 0,
    val ratioDecimalPlaces: Int = 0,
    val currencySign: String = "",
    val volumeSign: String = "",
    val numDropDownElements: Int = 0,
    val defaultDropDownFilter: DropDownSelection = DropDownSelection.MostUsed
)