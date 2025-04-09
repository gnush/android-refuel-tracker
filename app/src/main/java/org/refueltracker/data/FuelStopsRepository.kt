package org.refueltracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import java.time.Month

interface FuelStopsRepository {
    /**
     * Retrieves all fuel stops ordered by time in descending order by day/time
     */
    fun fuelStopsOrderedNewestFirst(): Flow<List<FuelStop>>

    /**
     * Retrieves all fuel stops between [from] and [to] (inclusive)  in descending order by day/time
     */
    fun fuelStopsBetween(from: LocalDate, to: LocalDate): Flow<List<FuelStop>>

    /**
     * Retrieves all fuel stops with year [year] and month [month] in descending order by day/time
     */
    fun fuelStopsOn(year: Int, month: Month): Flow<List<FuelStop>>

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    fun fuelStop(id: Int): Flow<FuelStop?>

    suspend fun insert(fuelStop: FuelStop)
    suspend fun update(fuelStop: FuelStop)
    suspend fun delete(fuelStop: FuelStop)
}