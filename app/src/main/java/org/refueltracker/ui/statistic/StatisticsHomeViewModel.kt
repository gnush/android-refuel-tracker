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

class StatisticsHomeViewModel(
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(StatisticsHomeUiState())
        private set

    init {
        viewModelScope.launch {
            uiState = uiState.copy(
                year = uiState.monthCalendar.year,
                allStopsAvg = fuelStopsRepository
                    .averageFuelStats()
                    .first(),
                currentYearAvg = fuelStopsRepository
                    .averageFuelStats(uiState.monthCalendar.year)
                    .first(),
                previousYearAvg = fuelStopsRepository
                    .averageFuelStats(uiState.monthCalendar.year-1)
                    .first(),
                currentMonthAvg = fuelStopsRepository
                    .averageFuelStats(uiState.monthCalendar.year, uiState.monthCalendar.month)
                    .first(),
                previousMonthAvg = fuelStopsRepository
                    .averageFuelStats(uiState.monthCalendar.previousMonth().year, uiState.monthCalendar.previousMonth().month)
                    .first(),
                allStopsSum = fuelStopsRepository.sumFuelStats().first()
            )
        }
    }

    fun navigatePreviousMonth() {
        uiState = uiState.copy(
            monthCalendar = uiState.monthCalendar.previousMonth()
        )
        updateMonthlyStatsFromDatabase()
    }

    fun navigateNextMonth() {
        uiState = uiState.copy(
            monthCalendar = uiState.monthCalendar.nextMonth()
        )
        updateMonthlyStatsFromDatabase()
    }

    fun navigatePreviousYear() {
        uiState = uiState.copy(
            year = uiState.year-1
        )
        updateYearlyStatsFromDatabase()
    }

    fun navigateNextYear() {
        uiState = uiState.copy(
            year = uiState.year+1
        )
        updateYearlyStatsFromDatabase()
    }

    private fun updateMonthlyStatsFromDatabase() {
        viewModelScope.launch {
            uiState = uiState.copy(
                currentMonthAvg = fuelStopsRepository
                    .averageFuelStats(uiState.monthCalendar.year, uiState.monthCalendar.month)
                    .first(),
                previousMonthAvg = fuelStopsRepository
                    .averageFuelStats(uiState.monthCalendar.previousMonth().year, uiState.monthCalendar.previousMonth().month)
                    .first(),
            )
        }
    }

    private fun updateYearlyStatsFromDatabase() {
        viewModelScope.launch {
            uiState = uiState.copy(
                currentYearAvg = fuelStopsRepository
                    .averageFuelStats(uiState.year)
                    .first(),
                previousYearAvg = fuelStopsRepository
                    .averageFuelStats(uiState.year-1)
                    .first(),
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
    val monthCalendar: CalendarUiState = CalendarUiState(),
    val year: Int = 0
)