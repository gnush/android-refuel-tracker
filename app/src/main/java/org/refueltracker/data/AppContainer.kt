package org.refueltracker.data

import android.content.Context

/**
 * App container for dependency injection.
 */
interface AppContainer {
    val fuelStopsRepository: FuelStopsRepository
}

/**
 * [AppContainer] implementation providing an instance of [RoomFuelStopsRepository]
 */
class AppDataContainer(private val context: Context): AppContainer {
    override val fuelStopsRepository: FuelStopsRepository by lazy {
        RoomFuelStopsRepository(
            fuelStopDao = RefuelTrackerDatabase.getDatabase(context).fuelStopDao(),
            fuelStationDao = RefuelTrackerDatabase.getDatabase(context).fuelStationDao(),
            fuelSortDao = RefuelTrackerDatabase.getDatabase(context).fuelSortDao()
        )
    }
}