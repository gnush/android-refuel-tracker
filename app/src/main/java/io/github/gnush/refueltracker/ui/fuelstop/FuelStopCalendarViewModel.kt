package io.github.gnush.refueltracker.ui.fuelstop

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import kotlinx.datetime.number
import io.github.gnush.refueltracker.data.FuelStopsRepository
import io.github.gnush.refueltracker.data.UserPreferencesRepository
import io.github.gnush.refueltracker.ui.createNumberFormat
import io.github.gnush.refueltracker.ui.data.DefaultSigns
import io.github.gnush.refueltracker.ui.data.FuelStopCalendarUiState
import io.github.gnush.refueltracker.ui.data.UserFormats
import kotlinx.coroutines.flow.first

class FuelStopCalendarViewModel(
    userPreferencesRepository: UserPreferencesRepository,
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    private val _fuelStopsState: MutableStateFlow<FuelStopCalendarUiState> =
        MutableStateFlow(FuelStopCalendarUiState())
    val fuelStopsState: StateFlow<FuelStopCalendarUiState> = _fuelStopsState

    init {
        viewModelScope.launch {
            fuelStopsRepository.fuelStopsOn(_fuelStopsState.value.calendar.year, _fuelStopsState.value.calendar.month)
                .collect {
                    val separateLargeNumbers = userPreferencesRepository.groupLargeNumbers.first()

                    _fuelStopsState.value = _fuelStopsState.value.copy(
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

    fun displayPreviousMonth() =
        if (_fuelStopsState.value.calendar.month.number == 1)
            updateCalendarView(_fuelStopsState.value.calendar.year-1, Month(12))
        else
            updateCalendarView(Month(_fuelStopsState.value.calendar.month.number-1))

    fun displayNextMonth() =
        if (_fuelStopsState.value.calendar.month.number == 12)
            updateCalendarView(_fuelStopsState.value.calendar.year+1, Month(1))
        else
            updateCalendarView(Month(_fuelStopsState.value.calendar.month.number+1))

    fun displayPreviousYear() = updateCalendarView(_fuelStopsState.value.calendar.year-1)
    fun displayNextYear() = updateCalendarView(_fuelStopsState.value.calendar.year+1)

    private fun updateCalendarView(month: Month) {
        _fuelStopsState.value = _fuelStopsState.value.copy(
            calendar = _fuelStopsState.value.calendar.copy(month = month)
        )
        updateFuelStops()
    }

    private fun updateCalendarView(year: Int) {
        _fuelStopsState.value = _fuelStopsState.value.copy(
            calendar = _fuelStopsState.value.calendar.copy(year = year)
        )
        updateFuelStops()
    }

    private fun updateCalendarView(year: Int, month: Month) {
        _fuelStopsState.value = _fuelStopsState.value.copy(
            calendar = _fuelStopsState.value.calendar.copy(year = year, month = month)
        )
        updateFuelStops()
    }

    private fun updateFuelStops() = viewModelScope.launch {
        fuelStopsRepository.fuelStopsOn(
            _fuelStopsState.value.calendar.year,
            _fuelStopsState.value.calendar.month
        ).collect {
            _fuelStopsState.value = _fuelStopsState.value.copy(fuelStops = it)
        }
    }
}