package gnush.refueltracker.ui.data

import gnush.refueltracker.ui.DropDownSelection
import java.text.NumberFormat

data class FuelStopUiState(
    val details: FuelStopDetails = FuelStopDetails(),
    val isValid: Boolean = false,
    val dropDownItems: DropDownItemsUiState = DropDownItemsUiState(),
    val userPreferences: EntryUserPreferences = EntryUserPreferences()
)

data class DropDownItemsUiState(
    val fuelSortRecentDropDownItems: List<String> = emptyList(),
    val fuelSortUsedDropDownItems: List<String> = emptyList(),
    val stationRecentDropDownItems: List<String> = emptyList(),
    val stationUsedDropDownItems: List<String> = emptyList(),
)

data class DefaultSigns(
    val currency: String = "",
    val volume: String = ""
)

data class NumberFormats(
    val currency: NumberFormat = NumberFormat.getInstance(),
    val volume: NumberFormat = NumberFormat.getInstance(),
    val ratio: NumberFormat = NumberFormat.getInstance()
)

data class EntryUserPreferences(
    val signs: DefaultSigns = DefaultSigns(),
    val formats: NumberFormats = NumberFormats(),
    val dropDownFilter: DropDownSelection = DropDownSelection.MostUsed
)