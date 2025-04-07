package org.refueltracker.data

import kotlinx.coroutines.flow.Flow

class RoomFuelStopsRepository(private val fuelStopDao: FuelStopDao): FuelStopsRepository {
    /**
     * Retrieves all fuel stops ordered by time (descendent)
     */
    override fun fuelStopsOrderedNewestFirst(): Flow<List<FuelStop>> = fuelStopDao.allFuelStopsOrderedNewestFirst()

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    override fun fuelStop(id: Int): Flow<FuelStop?> = fuelStopDao.fuelStop(id)

    override suspend fun insert(fuelStop: FuelStop) = fuelStopDao.insert(fuelStop)

    override suspend fun update(fuelStop: FuelStop) = fuelStopDao.update(fuelStop)

    override suspend fun delete(fuelStop: FuelStop) = fuelStopDao.delete(fuelStop)
}