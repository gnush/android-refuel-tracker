package io.github.gnush.refueltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate

@Dao
interface FuelStopDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(fuelStop: FuelStopEntity): Long

    @Update
    suspend fun update(fuelStop: FuelStopEntity)

    @Delete
    suspend fun delete(fuelStop: FuelStopEntity)

    // TODO: change queries returning FuelStopEntity to return FuelStop (joins in the query, instead of later resolving later in der repository)
    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    @Query("select * from fuel_stops where id = :id")
    fun fuelStop(id: Int): Flow<FuelStopEntity>

    /**
     * Retrieves all fuel stops in descending order by day/time
     */
    @Query("select * from fuel_stops order by day desc, time desc")
    fun allFuelStopsOrderedNewestFirst(): Flow<List<FuelStopEntity>>

    /**
     * Retrieves all fuel stops between [from] and [to] in descending order by day/time
     */
    @Query("select * from fuel_stops where day between :from and :to order by day desc, time desc")
    fun fuelStopsBetween(from: LocalDate, to: LocalDate): Flow<List<FuelStopEntity>>

    /**
     * Retrieves all fuel stops from [monthOfYear] in descending order by day/time
     */
    @Query("select * from fuel_stops where day between :monthOfYear and :monthOfYear+99 order by day desc, time desc")
    fun fuelStopsOnMonthOfYear(monthOfYear: Int): Flow<List<FuelStopEntity>>

    /**
     * Retrieves all fuel stops from [year] in descending order by day/time
     */
    @Query("select * from fuel_stops where day between :year and :year+9999 order by day desc, time desc")
    fun fuelStopsOnYear(year: Int): Flow<List<FuelStopEntity>>

    /**
     * Retrieve the average price per volume, volume and price of all fuel stops.
     */
    @Query("""select avg(pricePerVolume) as pricePerVolume,
                     avg(totalPrice) as price,
                     avg(totalVolume) as volume
              from fuel_stops""")
    fun averageFuelStats(): Flow<FuelStopAverageValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops between [from] and [to].
     * @param from First day of the range (inclusive)
     * @param to Last day of the range (inclusive)
     */
    @Query("""select avg(pricePerVolume) as pricePerVolume,
                     avg(totalPrice) as price,
                     avg(totalVolume) as volume
              from fuel_stops
              where day between :from and :to""")
    fun averageFuelStats(from: LocalDate, to: LocalDate): Flow<FuelStopAverageValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops between [from] and [to].
     *
     * Assumes parameters are in yyyyMMdd format.
     * See [Converters.localDateToInt] for more information on the format.
     * @param from First day of the range (inclusive)
     * @param to Last day of the range (inclusive)
     */
    @Query("""select avg(pricePerVolume) as pricePerVolume, 
                     avg(totalPrice) as price, 
                     avg(totalVolume) as volume
              from fuel_stops 
              where day between :from and :to""")
    fun averageFuelStats(from: Int, to: Int): Flow<FuelStopAverageValues>

    /**
     * Retrieve the sum of the volume and price of all fuel stops.
     */
    @Query("""select sum(totalPrice) as price,
                     sum(totalVolume) as volume
              from fuel_stops""")
    fun sumFuelStats(): Flow<FuelStopSumValues>

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops between [from] and [to].
     * @param from First day of the range (inclusive)
     * @param to Last day of the range (inclusive)
     */
    @Query("""select sum(totalPrice) as price,
                     sum(totalVolume) as volume
              from fuel_stops
              where day between :from and :to""")
    fun sumFuelStats(from: LocalDate, to: LocalDate): Flow<FuelStopSumValues>

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops between [from] and [to].
     *
     * Assumes parameters are in yyyyMMdd format.
     * See [Converters.localDateToInt] for more information on the format.
     * @param from First day of the range (inclusive)
     * @param to Last day of the range (inclusive)
     */
    @Query("""select sum(totalPrice) as price,
                     sum(totalVolume) as volume
              from fuel_stops
              where day between :from and :to""")
    fun sumFuelStats(from: Int, to: Int): Flow<FuelStopSumValues>

    /**
     * Retrieves the most often fueled fuel sort.
     */
    @Query("""select fuel_sort.label
              from fuel_sort
              join fuel_stops
              on fuel_sort_id = fuel_sort.id
              group by fuel_sort_id
              order by count(*) desc
              limit 1""")
    fun mostUsedFuelSort(): Flow<String?>

    /**
     * Retrieves the [n] most often fueled fuel sorts.
     */
    @Query("""select fuel_sort.label
              from fuel_sort
              join fuel_stops
              on fuel_sort_id = fuel_sort.id
              group by fuel_sort_id
              order by count(*) desc
              limit :n""")
    fun mostUsedFuelSorts(n: Int): Flow<List<String>>

    /**
     * Retrieves the [n] most recently fueled fuel sorts.
     */
    @Query("""select fuel_sort.label
              from fuel_sort
              join fuel_stops
              on fuel_sort_id = fuel_sort.id
              group by fuel_sort_id
              order by day desc, time desc
              limit :n""")
    fun mostRecentFuelSorts(n: Int): Flow<List<String>>

    /**
     * Retrieves the [n] most often fueled fuel sorts.
     */
    @Query("""select fuel_station.name
              from fuel_station
              join fuel_stops
              on station_id = fuel_station.id
              group by station_id
              order by count(*) desc
              limit :n""")
    fun mostUsedFuelStations(n: Int): Flow<List<String>>

    /**
     * Retrieves the [n] most recently fueled fuel sorts.
     */
    @Query("""select fuel_station.name
              from fuel_station
              join fuel_stops
              on station_id = fuel_station.id
              group by station_id
              order by day desc, time desc
              limit :n""")
    fun mostRecentFuelStations(n: Int): Flow<List<String>>
}