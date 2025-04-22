package io.github.gnush.refueltracker.ui.data

import io.github.gnush.refueltracker.data.FuelStop

data class FuelStopListUiState(
    val fuelStops: List<FuelStop> = listOf(),
    val signs: DefaultSigns = DefaultSigns(),
    val formats: UserFormats = UserFormats()
)