package gnush.refueltracker.ui.fuelstop

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.format
import gnush.refueltracker.data.FuelStopsRepository
import gnush.refueltracker.data.UserPreferencesRepository
import gnush.refueltracker.ui.Config
import gnush.refueltracker.ui.createNumberFormat
import gnush.refueltracker.ui.data.DefaultSigns
import gnush.refueltracker.ui.data.DropDownItemsUiState
import gnush.refueltracker.ui.data.EntryUserPreferences
import gnush.refueltracker.ui.data.FuelStopDetails
import gnush.refueltracker.ui.data.FuelStopUiState
import gnush.refueltracker.ui.data.NumberFormats
import gnush.refueltracker.ui.extensions.format
import gnush.refueltracker.ui.extensions.toFuelStop
import gnush.refueltracker.ui.extensions.toFuelStopDetails
import kotlinx.coroutines.flow.filterNotNull
import java.math.RoundingMode
import java.util.Calendar

class FuelStopEntryViewModel(
    savedStateHandle: SavedStateHandle,
    userPreferences: UserPreferencesRepository,
    private val fuelStopsRepository: FuelStopsRepository
): ViewModel() {
    var uiState by mutableStateOf(FuelStopUiState(
        details = FuelStopDetails(
            day = LocalDate(
                year = Calendar.getInstance().get(Calendar.YEAR),
                monthNumber = Calendar.getInstance().get(Calendar.MONTH)+1,
                dayOfMonth = Calendar.getInstance().get(Calendar.DAY_OF_MONTH),
            ).format(Config.DATE_FORMAT)
        )
    ))
        private set

    private val fuelStopId: Int? = savedStateHandle[FuelStopEditDestination.FUEL_STOP_ID]

    init {
        viewModelScope.launch {
            val numberOfDropDownItems = userPreferences.numberOfEntryScreenDropDownElements.first()
            val thousandsSeparatorPlaces = userPreferences.thousandsSeparatorPlaces.first()

            uiState = uiState.copy(
                details =
                    if (fuelStopId == null)
                        uiState.details.copy(
                            fuelSort = fuelStopsRepository.mostUsedFuelSort().first() ?: ""
                        )
                    else
                        fuelStopsRepository.fuelStop(fuelStopId)
                            .filterNotNull()
                            .first()
                            .toFuelStopDetails(),
                dropDownItems = DropDownItemsUiState(
                    fuelSortRecentDropDownItems = fuelStopsRepository
                        .mostRecentFuelSorts(numberOfDropDownItems).first(),
                    fuelSortUsedDropDownItems = fuelStopsRepository
                        .mostUsedFuelSorts(numberOfDropDownItems).first(),
                    stationRecentDropDownItems = fuelStopsRepository
                        .mostRecentFuelStations(numberOfDropDownItems).first(),
                    stationUsedDropDownItems = fuelStopsRepository
                        .mostUsedFuelStations(numberOfDropDownItems).first()
                ),
                userPreferences = EntryUserPreferences(
                    signs = DefaultSigns(
                        currency = userPreferences.defaultCurrencySign.first(),
                        volume = userPreferences.defaultVolumeSign.first()
                    ),
                    formats = NumberFormats(
                        currency = createNumberFormat(
                            thousandsSeparatorPlaces = thousandsSeparatorPlaces,
                            decimalPlaces = userPreferences.currencyDecimalPlaces.first()
                        ),
                        volume = createNumberFormat(
                            thousandsSeparatorPlaces = thousandsSeparatorPlaces,
                            decimalPlaces = userPreferences.volumeDecimalPlaces.first()
                        ),
                        ratio = createNumberFormat(
                            thousandsSeparatorPlaces = thousandsSeparatorPlaces,
                            decimalPlaces = userPreferences.currencyVolumeRatioDecimalPlaces.first()
                        )
                    ),
                    dropDownFilter = userPreferences.defaultEntryScreenDropDownSelection.first()
                )
            )
        }
    }

    /**
     * Updates the [uiState] with the provided values and validates the input values.
     */
    fun updateUiState(fuelStopDetails: FuelStopDetails) {
        uiState = uiState.copy(
            details = fuelStopDetails,
            isValid = validate(fuelStopDetails)
        )
    }

    /**
     * Stores the current [FuelStopDetails] in the Database if the inputs can be validated
     */
    suspend fun saveFuelStop() {
        if (validate(uiState.details))
            fuelStopsRepository.insert(uiState.details.toFuelStop())
    }

    /**
     * Updates the Database entry of the current [FuelStopDetails] if the inputs can be validated
     */
    suspend fun updateFuelStop() {
        if (validate(uiState.details))
            fuelStopsRepository.update(uiState.details.toFuelStop())
    }

    private fun validate(details: FuelStopDetails): Boolean =
        details.station.isNotBlank()
        && details.fuelSort.isNotBlank()
        && Config.DATE_FORMAT.parseOrNull(details.day) != null
        && (if (details.time == null) true
        else Config.TIME_FORMAT.parseOrNull(details.time) != null)
        && try {
            uiState.userPreferences.formats.ratio.parse(details.pricePerVolume) != null
            && uiState.userPreferences.formats.volume.parse(details.totalVolume) != null
            && uiState.userPreferences.formats.currency.parse(details.totalPrice) != null
        } catch (_: Exception) {
            false
        }

    fun updateBasedOnPricePerVolume(pricePerVolume: String) = updateUiState(
        uiState.details.copy(
            pricePerVolume = pricePerVolume,
            totalPrice = try {
                val ppv = uiState.userPreferences.formats.ratio.parse(pricePerVolume)?.toString() ?: ""
                val vol = uiState.userPreferences.formats.volume.parse(uiState.details.totalVolume)?.toString() ?: ""

                (ppv.toBigDecimal() * vol.toBigDecimal())
                    .format(uiState.userPreferences.formats.currency)
            } catch (_: Exception) {
                Log.d("ENTRY_UPDATE_ON_PPV", "could not convert '$pricePerVolume' or '${uiState.details.totalVolume}'")
                uiState.details.totalPrice
            }
        )
    )

    fun updateBasedOnTotalVolume(totalVolume: String) = updateUiState(
        uiState.details.copy(
            totalVolume = totalVolume,
            totalPrice = try {
                val ppv = uiState.userPreferences.formats.ratio.parse(uiState.details.pricePerVolume)?.toString() ?: ""
                val vol = uiState.userPreferences.formats.volume.parse(totalVolume)?.toString() ?: ""

                (ppv.toBigDecimal() * vol.toBigDecimal())
                    .format(uiState.userPreferences.formats.currency)
            } catch (_: Exception) {
                Log.d("ENTRY_UPDATE_ON_VOL", "could not convert '$totalVolume' or '${uiState.details.totalPrice}'")
                uiState.details.totalPrice
            }
        )
    )

    fun updateBasedOnTotalPrice(totalPrice: String) = updateUiState(
        uiState.details.copy(
            totalPrice = totalPrice,
            totalVolume = try {
                val ppvString = uiState.userPreferences.formats.ratio.parse(uiState.details.pricePerVolume)?.toString() ?: ""
                val ppv = ppvString.toBigDecimal()
                val price = uiState.userPreferences.formats.currency.parse(totalPrice)?.toString() ?: ""

                (price.toBigDecimal().divide(ppv, ppv.scale(), RoundingMode.HALF_UP))
                    .format(uiState.userPreferences.formats.volume)
            } catch (e: ArithmeticException) {
                Log.d("ENTRY_UPDATE_ON_PRICE", "${e.message}")
                uiState.details.totalVolume
            } catch (_: Exception) {
                Log.d("ENTRY_UPDATE_ON_PRICE", "could not convert '$totalPrice' or '${uiState.details.totalVolume}'")
                uiState.details.totalVolume
            }
        )
    )
}
