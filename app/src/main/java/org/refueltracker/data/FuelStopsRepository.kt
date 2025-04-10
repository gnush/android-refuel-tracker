package org.refueltracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

interface FuelStopsRepository {
    suspend fun insert(fuelStop: FuelStop)
    suspend fun update(fuelStop: FuelStop)
    suspend fun delete(fuelStop: FuelStop)

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    fun fuelStop(id: Int): Flow<FuelStop?>

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
     * Retrieves all fuel stops from [year] in descending order by day/time
     */
    fun fuelStopsOn(year: Int): Flow<List<FuelStop>>

    /**
     * Retrieve the average price per volume, volume and price of all fuel stops.
     */
    fun averageFuelStats(): Flow<FuelStopDecimalValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops between [from] and [to] (both inclusive).
     */
    fun averageFuelStats(from: LocalDate, to: LocalDate): Flow<FuelStopDecimalValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops of [year].
     * E.g. for year=2000, all days with pattern 2000-MM-dd.
     */
    fun averageFuelStats(year: Int): Flow<FuelStopDecimalValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops of month [month] of year [year].
     * E.g. for year=2000 and month=FEBRUARY, all days with pattern 2000-02-dd.
     */
    fun averageFuelStats(year: Int, month: Month): Flow<FuelStopDecimalValues>
}