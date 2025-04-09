package org.refueltracker.ui.statistic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.refueltracker.data.FuelStop
import org.refueltracker.data.FuelStopsRepository
import org.refueltracker.ui.calendar.CalendarUiState

class StatisticsHomeViewModel(
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(StatisticsHomeUiState())
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                allStops = fuelStopsRepository
                    .fuelStopsOrderedNewestFirst()
                    .first(),
                currentYearStops = fuelStopsRepository
                    .fuelStopsOn(uiState.currentMonthCalendar.year)
                    .first(),
                previousYearStops = fuelStopsRepository
                    .fuelStopsOn(uiState.currentMonthCalendar.year-1)
                    .first(),
                currentMonthStops = fuelStopsRepository
                    .fuelStopsOn(uiState.currentMonthCalendar.year, uiState.currentMonthCalendar.month)
                    .first(),
                previousMonthStops = fuelStopsRepository
                    .fuelStopsOn(uiState.previousMonthCalendar.year, uiState.previousMonthCalendar.month)
                    .first()
            )
        }
    }
}

data class StatisticsHomeUiState(
    val allStops: List<FuelStop> = emptyList(),
    val currentMonthStops: List<FuelStop> = emptyList(),
    val previousMonthStops: List<FuelStop> = emptyList(),
    val currentYearStops: List<FuelStop> = emptyList(),
    val previousYearStops: List<FuelStop> = emptyList(),
    val currentMonthCalendar: CalendarUiState = CalendarUiState(),
    val previousMonthCalendar: CalendarUiState = CalendarUiState()
)