package io.github.gnush.refueltracker.ui.data

import io.github.gnush.refueltracker.data.FuelStop
import io.github.gnush.refueltracker.ui.calendar.CalendarUiState

data class FuelStopCalendarUiState(
    val fuelStops: List<FuelStop> = emptyList(),
    val calendar: CalendarUiState = CalendarUiState(),
    val signs: DefaultSigns = DefaultSigns(),
    val formats: UserFormats = UserFormats()
)