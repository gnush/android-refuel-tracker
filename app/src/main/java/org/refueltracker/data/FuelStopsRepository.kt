package org.refueltracker.data

import androidx.room.Query
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

//    /**
//     * Retrieve the average price per volume, volume and price of all fuel stops.
//     */
//    fun allTimeAverageFuelStats(): Flow<List<Triple<Double, Double, Double>>>
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops between [from] and [to] (both inclusive).
//     */
//    fun averageFuelStats(from: LocalDate, to: LocalDate): Flow<List<Triple<Double, Double, Double>>>
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops of [year].
//     */
//    fun averageFuelStatsByYear(year: Int): Flow<List<Triple<Double, Double, Double>>>
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops of month [month] of year [year].
//     */
//    fun averageFuelStatsByMonthOfYear(year: Int, month: kotlinx.datetime.Month): Flow<List<Triple<Double, Double, Double>>>
}