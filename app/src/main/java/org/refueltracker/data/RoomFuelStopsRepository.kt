package org.refueltracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import java.time.Month

class RoomFuelStopsRepository(private val fuelStopDao: FuelStopDao): FuelStopsRepository {
    override suspend fun insert(fuelStop: FuelStop) = fuelStopDao.insert(fuelStop)

    override suspend fun update(fuelStop: FuelStop) = fuelStopDao.update(fuelStop)

    override suspend fun delete(fuelStop: FuelStop) = fuelStopDao.delete(fuelStop)

    /**
     * Retrieves all fuel stops ordered by time  in descending order by day/time
     */
    override fun fuelStopsOrderedNewestFirst(): Flow<List<FuelStop>> =
        fuelStopDao.allFuelStopsOrderedNewestFirst()

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    override fun fuelStop(id: Int): Flow<FuelStop?> = fuelStopDao.fuelStop(id)

    /**
     * Retrieves all fuel stops between [from] and [to] (inclusive)  in descending order by day/time
     */
    override fun fuelStopsBetween(from: LocalDate, to: LocalDate): Flow<List<FuelStop>> =
        fuelStopDao.fuelStopsBetween(from, to)

    /**
     * Retrieves all fuel stops with year [year] and month [month] in descending order by day/time
     */
    override fun fuelStopsOn(year: Int, month: Month): Flow<List<FuelStop>> =
        fuelStopDao.fuelStopsOnMonthOfYear(year*10000 + month.number*100)

    /**
     * Retrieves all fuel stops from [year] in descending order by day/time
     */
    override fun fuelStopsOn(year: Int): Flow<List<FuelStop>> =
        fuelStopDao.fuelStopsOnYear(year*10000)

//    /**
//     * Retrieve the average price per volume, volume and price of all fuel stops.
//     */
//    override fun allTimeAverageFuelStats(): Flow<List<Triple<Double, Double, Double>>> =
//        fuelStopDao.allTimeAverageFuelStats()
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops between [from] and [to] (both inclusive).
//     */
//    override fun averageFuelStats(from: LocalDate,to: LocalDate): Flow<List<Triple<Double, Double, Double>>> =
//        fuelStopDao.averageFuelStats(from, to)
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops of [year].
//     */
//    override fun averageFuelStatsByYear(year: Int): Flow<List<Triple<Double, Double, Double>>> =
//        fuelStopDao.averageFuelStatsByYear(year*10000)
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops of month [month] of year [year].
//     */
//    override fun averageFuelStatsByMonthOfYear(
//        year: Int,
//        month: kotlinx.datetime.Month
//    ): Flow<List<Triple<Double, Double, Double>>> =
//        fuelStopDao.averageFuelStatsByMonthOfYear(year*10000 + month.number*100)
}