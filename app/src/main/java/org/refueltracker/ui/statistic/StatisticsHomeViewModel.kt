package org.refueltracker.ui.statistic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.refueltracker.data.FuelStopDecimalValues
import org.refueltracker.data.FuelStopsRepository
import org.refueltracker.ui.calendar.CalendarUiState
import org.refueltracker.ui.calendar.previousMonth

class StatisticsHomeViewModel(
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(StatisticsHomeUiState())
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                allStops = fuelStopsRepository
                    .averageFuelStats()
                    .first(),
                currentYearStops = fuelStopsRepository
                    .averageFuelStats(uiState.currentMonthCalendar.year)
                    .first(),
                previousYearStops = fuelStopsRepository
                    .averageFuelStats(uiState.currentMonthCalendar.year-1)
                    .first(),
                currentMonthStops = fuelStopsRepository
                    .averageFuelStats(uiState.currentMonthCalendar.year, uiState.currentMonthCalendar.month)
                    .first(),
                previousMonthStops = fuelStopsRepository
                    .averageFuelStats(uiState.currentMonthCalendar.previousMonth().year, uiState.currentMonthCalendar.previousMonth().month)
                    .first()
            )
        }
    }
}

data class StatisticsHomeUiState(
    val allStops: FuelStopDecimalValues = FuelStopDecimalValues(),
    val currentMonthStops: FuelStopDecimalValues = FuelStopDecimalValues(),
    val previousMonthStops: FuelStopDecimalValues = FuelStopDecimalValues(),
    val currentYearStops: FuelStopDecimalValues = FuelStopDecimalValues(),
    val previousYearStops: FuelStopDecimalValues = FuelStopDecimalValues(),
    val currentMonthCalendar: CalendarUiState = CalendarUiState()
)