package org.refueltracker.ui.statistic

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.refueltracker.data.FuelStopAverageValues
import org.refueltracker.data.FuelStopSumValues
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
                allStopsAvg = fuelStopsRepository
                    .averageFuelStats()
                    .first(),
                currentYearAvg = fuelStopsRepository
                    .averageFuelStats(uiState.currentMonthCalendar.year)
                    .first(),
                previousYearAvg = fuelStopsRepository
                    .averageFuelStats(uiState.currentMonthCalendar.year-1)
                    .first(),
                currentMonthAvg = fuelStopsRepository
                    .averageFuelStats(uiState.currentMonthCalendar.year, uiState.currentMonthCalendar.month)
                    .first(),
                previousMonthAvg = fuelStopsRepository
                    .averageFuelStats(uiState.currentMonthCalendar.previousMonth().year, uiState.currentMonthCalendar.previousMonth().month)
                    .first(),
                allStopsSum = fuelStopsRepository.sumFuelStats().first()
            )
        }
    }
}

data class StatisticsHomeUiState(
    val allStopsAvg: FuelStopAverageValues = FuelStopAverageValues(),
    val allStopsSum: FuelStopSumValues = FuelStopSumValues(),
    val currentMonthAvg: FuelStopAverageValues = FuelStopAverageValues(),
    val previousMonthAvg: FuelStopAverageValues = FuelStopAverageValues(),
    val currentYearAvg: FuelStopAverageValues = FuelStopAverageValues(),
    val previousYearAvg: FuelStopAverageValues = FuelStopAverageValues(),
    val currentMonthCalendar: CalendarUiState = CalendarUiState()
)