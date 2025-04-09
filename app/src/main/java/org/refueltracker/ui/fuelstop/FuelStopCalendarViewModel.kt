package org.refueltracker.ui.fuelstop

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.Month
import kotlinx.datetime.number
import org.refueltracker.data.FuelStop
import org.refueltracker.data.FuelStopsRepository
import org.refueltracker.ui.calendar.CalendarUiState

class FuelStopCalendarViewModel(
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(FuelStopCalendarUiState())
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                fuelStops = fuelStopsRepository.fuelStopsOn(uiState.calendar.year, uiState.calendar.month)
                    .filterNotNull()
                    .first()
            )
        }
    }

    fun displayPreviousMonth() =
        if (uiState.calendar.month.number == 1)
            updateCalendarView(uiState.calendar.year-1, Month(12))
        else
            updateCalendarView(Month(uiState.calendar.month.number-1))

    fun displayNextMonth() =
        if (uiState.calendar.month.number == 12)
            updateCalendarView(uiState.calendar.year+1, Month(1))
        else
            updateCalendarView(Month(uiState.calendar.month.number+1))

    fun displayPreviousYear() = updateCalendarView(uiState.calendar.year-1)
    fun displayNextYear() = updateCalendarView(uiState.calendar.year+1)

    fun refreshFuelStops() = updateFuelStops()

    private fun updateCalendarView(month: Month) {
        uiState = uiState.copy(
            calendar = uiState.calendar.copy(month = month)
        )
        updateFuelStops()
    }

    private fun updateCalendarView(year: Int) {
        uiState = uiState.copy(
            calendar = uiState.calendar.copy(year = year)
        )
        updateFuelStops()
    }

    private fun updateCalendarView(year: Int, month: Month) {
        uiState = uiState.copy(
            calendar = uiState.calendar.copy(
                year = year,
                month = month
            )
        )
        updateFuelStops()
    }

    private fun updateFuelStops() = viewModelScope.launch {
        uiState = uiState.copy(
            fuelStops = fuelStopsRepository
                .fuelStopsOn(uiState.calendar.year, uiState.calendar.month)
                .filterNotNull()
                .first()
        )
    }
}

data class FuelStopCalendarUiState(
    val fuelStops: List<FuelStop> = emptyList(),
    val calendar: CalendarUiState = CalendarUiState()
)