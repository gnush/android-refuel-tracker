package org.refueltracker.ui.data

import org.refueltracker.data.FuelStop
import org.refueltracker.ui.calendar.CalendarUiState

data class FuelStopCalendarUiState(
    val fuelStops: List<FuelStop> = emptyList(),
    val calendar: CalendarUiState = CalendarUiState()
)