package gnush.refueltracker.ui.data

import gnush.refueltracker.data.FuelStop
import gnush.refueltracker.ui.calendar.CalendarUiState

data class FuelStopCalendarUiState(
    val fuelStops: List<FuelStop> = emptyList(),
    val calendar: CalendarUiState = CalendarUiState()
)