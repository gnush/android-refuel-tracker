package io.github.gnush.refueltracker.ui.settings

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import io.github.gnush.refueltracker.data.UserPreferencesRepository
import io.github.gnush.refueltracker.ui.DropDownSelection
import io.github.gnush.refueltracker.ui.data.ANSI
import io.github.gnush.refueltracker.ui.data.CustomDateFormat
import io.github.gnush.refueltracker.ui.data.DIN
import io.github.gnush.refueltracker.ui.data.DateFormat
import io.github.gnush.refueltracker.ui.data.ISO
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format.FormatStringsInDatetimeFormats
import kotlinx.datetime.format.byUnicodePattern
import kotlin.reflect.KSuspendFunction1

class SettingsViewModel(
    private val userPreferencesRepository: UserPreferencesRepository
): ViewModel() {
    private var _uiState: MutableStateFlow<SettingsUiState> =
        MutableStateFlow(SettingsUiState())
    val uiState: StateFlow<SettingsUiState> = _uiState

    val entryScreenDropDownFilterItems = listOf(DropDownSelection.MostUsed, DropDownSelection.MostRecent)
    val dateFormats = listOf(ISO, DIN, ANSI, CustomDateFormat(""))

    init {
        viewModelScope.launch {
            _uiState.value = SettingsUiState(
                separateThousands = userPreferencesRepository.separateThousands.first(),
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
                defaultDropDownFilter = userPreferencesRepository.defaultEntryScreenDropDownSelection.first(),
                dateFormat = userPreferencesRepository.dateFormat.first(),
                dateFormatPattern = Preference(
                    value = userPreferencesRepository.dateFormatPattern.first(),
                    isValid = true
                )
            )
        }
    }

    fun saveSeparateLargeNumbers(value: Boolean) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(separateThousands = value)
        userPreferencesRepository.saveSeparateThousands(value)
    }

    fun saveLargeNumbersSeparatorPlaces(n: String) {
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
        _uiState.value = _uiState.value.copy(
            defaultDropDownFilter = filter
        )
        userPreferencesRepository.saveDefaultEntryScreenDropDownSelection(filter)
    }

    fun saveDateFormat(format: DateFormat) = viewModelScope.launch {
        _uiState.value = _uiState.value.copy(
            dateFormat = if (format is CustomDateFormat) CustomDateFormat(_uiState.value.dateFormatPattern.value) else format
        )
        userPreferencesRepository.saveDateFormat(format)
    }

    @OptIn(FormatStringsInDatetimeFormats::class)
    fun saveDateFormatPattern(pattern: String) = viewModelScope.launch {
        val dateFormatPattern = Preference(
            value = pattern,
            isValid = try {
                LocalDate.Format { byUnicodePattern(pattern) }
                true
            } catch (_: Exception) {
                Log.d("CONFIG_SAVE", "'$pattern' is no valid date format pattern")
                false
            }
        )

        if (dateFormatPattern.isValid) {
            _uiState.value = _uiState.value.copy(
                dateFormatPattern = dateFormatPattern,
                dateFormat = CustomDateFormat(pattern)
            )
            userPreferencesRepository.saveDateFormatPattern(pattern)
        } else {
            _uiState.value = _uiState.value.copy(
                dateFormatPattern = dateFormatPattern
            )
        }
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
    val separateThousands: Boolean = false,
    val thousandsSeparator: Preference = Preference(),
    val volumeDecimalPlaces: Preference = Preference(),
    val currencyDecimalPlaces: Preference = Preference(),
    val ratioDecimalPlaces: Preference = Preference(),
    val currencySign: String = "",
    val volumeSign: String = "",
    val numDropDownElements: Preference = Preference(),
    val defaultDropDownFilter: DropDownSelection = DropDownSelection.MostUsed,
    val dateFormat: DateFormat = ISO,
    val dateFormatPattern: Preference = Preference()
)

data class Preference(
    val value: String = "",
    val isValid: Boolean = false
)