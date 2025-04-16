package gnush.refueltracker.ui.data

import gnush.refueltracker.data.FuelStop

data class FuelStopListUiState(
    val fuelStops: List<FuelStop> = listOf(),
    val userPreferences: DefaultSigns = DefaultSigns()
)