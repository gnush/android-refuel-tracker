package org.refueltracker.ui.data

data class FuelStopUiState(
    val details: FuelStopDetails = FuelStopDetails(),
    val isValid: Boolean = false,
    val fuelSortDropDownItems: List<String> = emptyList(),
    val stationDropDownItems: List<String> = emptyList()
)