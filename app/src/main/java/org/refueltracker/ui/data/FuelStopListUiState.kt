package org.refueltracker.ui.data

import org.refueltracker.data.FuelStop

data class FuelStopListUiState(
    val fuelStops: List<FuelStop> = listOf()
)