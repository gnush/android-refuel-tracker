package org.refueltracker.data

import kotlinx.coroutines.flow.Flow

interface FuelStopsRepository {
    /**
     * Retrieves all fuel stops ordered by time (descendent)
     */
    fun fuelStopsOrderedNewestFirst(): Flow<List<FuelStop>>

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    fun fuelStop(id: Int): Flow<FuelStop?>

    suspend fun insert(fuelStop: FuelStop)
    suspend fun update(fuelStop: FuelStop)
    suspend fun delete(fuelStop: FuelStop)
}