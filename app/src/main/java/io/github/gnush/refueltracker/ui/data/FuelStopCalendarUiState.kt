package io.github.gnush.refueltracker.ui.data

import io.github.gnush.refueltracker.ui.calendar.CalendarUiState

data class FuelStopCalendarUiState(
    val fuelStops: List<FuelStopListItem> = emptyList(),
    val calendar: CalendarUiState = CalendarUiState(),
    val signs: DefaultSigns = DefaultSigns(),
    val formats: UserFormats = UserFormats()
)