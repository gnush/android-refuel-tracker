package gnush.refueltracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month

interface FuelStopsRepository {
    suspend fun insert(fuelStop: FuelStop): Long
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
    fun averageFuelStats(): Flow<FuelStopAverageValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops between [from] and [to] (both inclusive).
     */
    fun averageFuelStats(from: LocalDate, to: LocalDate): Flow<FuelStopAverageValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops of [year].
     * E.g. for year=2000, all days with pattern 2000-MM-dd.
     */
    fun averageFuelStats(year: Int): Flow<FuelStopAverageValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops of month [month] of year [year].
     * E.g. for year=2000 and month=FEBRUARY, all days with pattern 2000-02-dd.
     */
    fun averageFuelStats(year: Int, month: Month): Flow<FuelStopAverageValues>

    /**
     * Retrieve the sum of the volume and price of all fuel stops.
     */
    fun sumFuelStats(): Flow<FuelStopSumValues>

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops between [from] and [to] (both inclusive).
     */
    fun sumFuelStats(from: LocalDate, to: LocalDate): Flow<FuelStopSumValues>

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops of [year].
     * E.g. for year=2000, all days with pattern 2000-MM-dd.
     */
    fun sumFuelStats(year: Int): Flow<FuelStopSumValues>

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops of month [month] of year [year].
     * E.g. for year=2000 and month=FEBRUARY, all days with pattern 2000-02-dd.
     */
    fun sumFuelStats(year: Int, month: Month): Flow<FuelStopSumValues>

    /**
     * Retrieves the most often fueled fuel sort.
     */
    fun mostUsedFuelSort(): Flow<String?>

    /**
     * Retrieves the [n] most often fueled fuel sorts.
     */
    fun mostUsedFuelSorts(n: Int): Flow<List<String>>

    /**
     * Retrieves the [n] most recently fueled fuel sorts.
     */
    fun mostRecentFuelSorts(n: Int): Flow<List<String>>

    /**
     * Retrieves the [n] most often fueled fuel sorts.
     */
    fun mostUsedFuelStations(n: Int): Flow<List<String>>

    /**
     * Retrieves the [n] most recently fueled fuel sorts.
     */
    fun mostRecentFuelStations(n: Int): Flow<List<String>>
}