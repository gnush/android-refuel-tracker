package io.github.gnush.refueltracker.data

import android.content.Context
import android.util.Log
import androidx.compose.runtime.key
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.emptyPreferences
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import io.github.gnush.refueltracker.ui.DropDownSelection
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.map

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class UserPreferencesRepository(
    private val datastore: DataStore<Preferences>
) {
    companion object {
        private const val TAG = "UserPreferencesRepo"

        private val SEPARATE_THOUSANDS =
            booleanPreferencesKey("separate_thousands")
        private val THOUSANDS_SEPARATOR_PLACES =
            intPreferencesKey("thousands_separator_places")
        private val VOLUME_DECIMAL_PLACES =
            intPreferencesKey("volume_decimal_places")
        private val CURRENCY_DECIMAL_PLACES =
            intPreferencesKey("currency_decimal_places")
        private val CURRENCY_VOLUME_RATIO_DECIMAL_PLACES =
            intPreferencesKey("currency_volume_ratio_decimal_places")

        private val CURRENCY_SIGN = stringPreferencesKey("currency_sign")
        private val VOLUME_SIGN = stringPreferencesKey("volume_sign")

        private val ENTRY_SCREEN_DROP_DOWN_ELEMENTS =
            intPreferencesKey("entry_screen_drop_down_elements")
        private val ENTRY_SCREEN_DROP_DOWN_SELECTION =
            booleanPreferencesKey("entry_screen_drop_down_selection")

        @Volatile
        private var Instance: UserPreferencesRepository? = null

        fun getInstance(context: Context): UserPreferencesRepository =
            Instance ?: synchronized(this) {
                UserPreferencesRepository(context.datastore).also {
                    Instance = it
                }
            }
    }

    val separateThousands: Flow<Boolean> = readDatastore(SEPARATE_THOUSANDS, false)

    val thousandsSeparatorPlaces: Flow<Int> = readDatastore(THOUSANDS_SEPARATOR_PLACES, 3)

    val volumeDecimalPlaces: Flow<Int> = readDatastore(VOLUME_DECIMAL_PLACES, 2)

    val currencyDecimalPlaces: Flow<Int> = readDatastore(CURRENCY_DECIMAL_PLACES, 2)

    val currencyVolumeRatioDecimalPlaces: Flow<Int> = readDatastore(CURRENCY_VOLUME_RATIO_DECIMAL_PLACES, 3)

    val defaultCurrencySign: Flow<String> = readDatastore(CURRENCY_SIGN, "€")

    val defaultVolumeSign: Flow<String> = readDatastore(VOLUME_SIGN, "L")

    val numberOfEntryScreenDropDownElements: Flow<Int> = readDatastore(ENTRY_SCREEN_DROP_DOWN_ELEMENTS, 5)

    val defaultEntryScreenDropDownSelection: Flow<DropDownSelection> = datastore.data
        .catch {
            Log.e(TAG, "error reading $ENTRY_SCREEN_DROP_DOWN_SELECTION", it)
            emit(emptyPreferences())
        }.map {
            when (it[ENTRY_SCREEN_DROP_DOWN_SELECTION]) {
                false -> DropDownSelection.MostRecent
                true -> DropDownSelection.MostUsed
                null -> DropDownSelection.MostUsed
            }
        }

    suspend fun saveSeparateThousands(value: Boolean) = datastore.edit {
        it[SEPARATE_THOUSANDS] = value
    }

    suspend fun saveThousandsSeparatorPlaces(places: Int) = saveIntIfPositive(
        key = THOUSANDS_SEPARATOR_PLACES,
        value = places
    )

    suspend fun saveVolumeDecimalPreference(places: Int) = saveIntIfPositive(
        key = VOLUME_DECIMAL_PLACES,
        value = places
    )

    suspend fun saveCurrencyDecimalPreference(places: Int) = saveIntIfPositive(
        key = CURRENCY_DECIMAL_PLACES,
        value = places
    )

    suspend fun saveCurrencyVolumeRatioDecimalPreferences(places: Int) = saveIntIfPositive(
        key = CURRENCY_VOLUME_RATIO_DECIMAL_PLACES,
        value = places
    )

    suspend fun saveDefaultCurrencyPreference(sign: String) = datastore.edit {
        it[CURRENCY_SIGN] = sign
    }

    suspend fun saveDefaultVolumePreference(sign: String) = datastore.edit {
        it[VOLUME_SIGN] = sign
    }

    suspend fun saveDefaultNumberOfEntryScreenDropDownElements(numElements: Int) = saveIntIfPositive(
        key = ENTRY_SCREEN_DROP_DOWN_ELEMENTS,
        value = numElements
    )

    suspend fun saveDefaultEntryScreenDropDownSelection(dropDownSelection: DropDownSelection) = datastore.edit {
        it[ENTRY_SCREEN_DROP_DOWN_SELECTION] = when (dropDownSelection) {
            DropDownSelection.MostRecent -> false
            DropDownSelection.MostUsed -> true
        }
    }

    private fun <T> readDatastore(key: Preferences.Key<T>, default: T): Flow<T> = datastore.data
        .catch {
            Log.e(TAG, "error reading $key", it)
            emit(emptyPreferences())
        }
        .map { it[key] ?: default}

    private suspend fun saveIntIfPositive(key: Preferences.Key<Int>, value: Int) {
        if (value > 0)
            datastore.edit {
                it[key] = value
            }
    }
}