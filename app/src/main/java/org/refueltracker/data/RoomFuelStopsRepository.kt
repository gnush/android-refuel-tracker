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

    /**
     * Retrieve the average price per volume, volume and price of all fuel stops.
     */
    override fun averageFuelStats(): Flow<FuelStopAverageValues> =
        fuelStopDao.averageFuelStats()

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops between [from] and [to] (both inclusive).
     */
    override fun averageFuelStats(from: LocalDate,to: LocalDate): Flow<FuelStopAverageValues> =
        fuelStopDao.averageFuelStats(from, to)

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops of [year].
     * E.g. for year=2000, all days with pattern 2000-MM-dd.
     */
    override fun averageFuelStats(year: Int): Flow<FuelStopAverageValues> =
        fuelStopDao.averageFuelStats(year*10000, year*10000+9999)

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops of month [month] of year [year].
     * E.g. for year=2000 and month=FEBRUARY, all days with pattern 2000-02-dd.
     */
    override fun averageFuelStats(year: Int, month: Month): Flow<FuelStopAverageValues> = fuelStopDao.averageFuelStats(
        from = year*10000 + month.number*100,
        to = year*10000 + month.number*100 + 99
    )

    /**
     * Retrieve the sum of the volume and price of all fuel stops.
     */
    override fun sumFuelStats(): Flow<FuelStopSumValues> = fuelStopDao.sumFuelStats()

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops between [from] and [to] (both inclusive).
     */
    override fun sumFuelStats(from: LocalDate,to: LocalDate): Flow<FuelStopSumValues> =
        fuelStopDao.sumFuelStats(from, to)

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops of [year].
     * E.g. for year=2000, all days with pattern 2000-MM-dd.
     */
    override fun sumFuelStats(year: Int): Flow<FuelStopSumValues> =
        fuelStopDao.sumFuelStats(year*10000, year*10000 + 9999)

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops of month [month] of year [year].
     * E.g. for year=2000 and month=FEBRUARY, all days with pattern 2000-02-dd.
     */
    override fun sumFuelStats(year: Int, month: Month): Flow<FuelStopSumValues> = fuelStopDao.sumFuelStats(
        from = year*10000 + month.number*100,
        to = year*10000 + month.number*100 + 99
    )

    /**
     * Retrieves the most often fueled fuel sort.
     */
    override fun mostUsedFuelSort(): Flow<String> = fuelStopDao.mostUsedFuelSort()

    /**
     * Retrieves the [n] most often fueled fuel sorts.
     */
    override fun mostUsedFuelSorts(n: Int): Flow<List<String>> = fuelStopDao.mostUsedFuelSorts(n)

    /**
     * Retrieves the [n] most recently fueled fuel sorts.
     */
    override fun mostRecentFuelSorts(n: Int): Flow<List<String>> = fuelStopDao.mostRecentFuelSorts(n)

    /**
     * Retrieves the [n] most often fueled fuel sorts.
     */
    override fun mostUsedFuelStations(n: Int): Flow<List<String>> = fuelStopDao.mostUsedFuelStations(n)

    /**
     * Retrieves the [n] most recently fueled fuel sorts.
     */
    override fun mostRecentFuelStations(n: Int): Flow<List<String>> = fuelStopDao.mostRecentFuelStations(n)

    // unclean interface
//    fun foobar(n: Int, stations: Boolean = true, mostRecent: Boolean = true): Flow<List<String>> = when {
//        stations && mostRecent -> fuelStopDao.mostRecentFuelStations(n)
//        stations && !mostRecent -> fuelStopDao.mostUsedFuelStations(n)
//        !stations && mostRecent -> fuelStopDao.mostRecentFuelSorts(n)
//        else -> fuelStopDao.mostUsedFuelSorts(n)
//    }
}