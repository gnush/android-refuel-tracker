package io.github.gnush.refueltracker.ui.data

import io.github.gnush.refueltracker.data.FuelStop

data class FuelStopListUiState(
    val fuelStops: List<FuelStopListItem> = emptyList(),
    val signs: DefaultSigns = DefaultSigns(),
    val formats: UserFormats = UserFormats()
)

data class FuelStopListItem(
    val stop: FuelStop,
    val isSelected: Boolean = false
)