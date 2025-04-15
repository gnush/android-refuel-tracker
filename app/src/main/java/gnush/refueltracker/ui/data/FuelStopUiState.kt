package gnush.refueltracker.ui.data

data class FuelStopUiState(
    val details: FuelStopDetails = FuelStopDetails(),
    val isValid: Boolean = false,
    val dropDownItems: DropDownItemsUiState = DropDownItemsUiState()
)

data class DropDownItemsUiState(
    val fuelSortRecentDropDownItems: List<String> = emptyList(),
    val fuelSortUsedDropDownItems: List<String> = emptyList(),
    val stationRecentDropDownItems: List<String> = emptyList(),
    val stationUsedDropDownItems: List<String> = emptyList(),
)