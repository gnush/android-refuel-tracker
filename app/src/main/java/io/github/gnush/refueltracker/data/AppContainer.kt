package io.github.gnush.refueltracker.data

import android.content.Context

/**
 * App container for dependency injection.
 */
interface AppContainer {
    val userPreferencesRepository: UserPreferencesRepository
    val fuelStopsRepository: FuelStopsRepository
}

/**
 * [AppContainer] implementation providing an instance of [RoomFuelStopsRepository] and [UserPreferencesRepository]
 */
class AppDataContainer(private val context: Context): AppContainer {
    override val fuelStopsRepository: FuelStopsRepository by lazy {
        RoomFuelStopsRepository(
            fuelStopDao = RefuelTrackerDatabase.getDatabase(context).fuelStopDao(),
            fuelStationDao = RefuelTrackerDatabase.getDatabase(context).fuelStationDao(),
            fuelSortDao = RefuelTrackerDatabase.getDatabase(context).fuelSortDao()
        )
    }

    override val userPreferencesRepository: UserPreferencesRepository by lazy {
        UserPreferencesRepository.getInstance(context)
    }
}