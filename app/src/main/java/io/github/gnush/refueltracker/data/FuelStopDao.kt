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

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    @Query("""select fuel_stops.id, fuel_station.name as station, fuel_sort.label as fuelSort, currency.symbol as currency, volume.symbol as volume, price_per_volume as pricePerVolume, total_volume as totalVolume, total_price as totalPrice, day, time
              from fuel_stops
              inner join fuel_station on station_id = fuel_station.id
              inner join fuel_sort on fuel_sort_id = fuel_sort.id
              inner join currency on currency_id = currency.id
              inner join volume on volume_id = volume.id
              where fuel_stops.id = :id""")
    fun fuelStop(id: Long): Flow<FuelStop?>

    /**
     * Retrieves all fuel stops in descending order by day/time
     */
    @Query("""select fuel_stops.id, fuel_station.name as station, fuel_sort.label as fuelSort, currency.symbol as currency, volume.symbol as volume, price_per_volume as pricePerVolume, total_volume as totalVolume, total_price as totalPrice, day, time
              from fuel_stops
              inner join fuel_station on station_id = fuel_station.id
              inner join fuel_sort on fuel_sort_id = fuel_sort.id
              inner join currency on currency_id = currency.id
              inner join volume on volume_id = volume.id
              order by day desc, time desc""")
    fun allFuelStopsOrderedNewestFirst(): Flow<List<FuelStop>>

    /**
     * Retrieves all fuel stops between [from] and [to] in descending order by day/time
     */
    @Query("""select fuel_stops.id, fuel_station.name as station, fuel_sort.label as fuelSort, currency.symbol as currency, volume.symbol as volume, price_per_volume as pricePerVolume, total_volume as totalVolume, total_price as totalPrice, day, time
              from fuel_stops
              inner join fuel_station on station_id = fuel_station.id
              inner join fuel_sort on fuel_sort_id = fuel_sort.id
              inner join currency on currency_id = currency.id
              inner join volume on volume_id = volume.id
              where day between :from and :to order by day desc, time desc""")
    fun fuelStopsBetween(from: LocalDate, to: LocalDate): Flow<List<FuelStop>>

    /**
     * Retrieves all fuel stops from [monthOfYear] in descending order by day/time
     */
    @Query("""select fuel_stops.id, fuel_station.name as station, fuel_sort.label as fuelSort, currency.symbol as currency, volume.symbol as volume, price_per_volume as pricePerVolume, total_volume as totalVolume, total_price as totalPrice, day, time
              from fuel_stops
              inner join fuel_station on station_id = fuel_station.id
              inner join fuel_sort on fuel_sort_id = fuel_sort.id
              inner join currency on currency_id = currency.id
              inner join volume on volume_id = volume.id
              where day between :monthOfYear and :monthOfYear+99 order by day desc, time desc""")
    fun fuelStopsOnMonthOfYear(monthOfYear: Int): Flow<List<FuelStop>>

    /**
     * Retrieves all fuel stops from [year] in descending order by day/time
     */
    @Query("""select fuel_stops.id, fuel_station.name as station, fuel_sort.label as fuelSort, currency.symbol as currency, volume.symbol as volume, price_per_volume as pricePerVolume, total_volume as totalVolume, total_price as totalPrice, day, time
              from fuel_stops
              inner join fuel_station on station_id = fuel_station.id
              inner join fuel_sort on fuel_sort_id = fuel_sort.id
              inner join currency on currency_id = currency.id
              inner join volume on volume_id = volume.id
              where day between :year and :year+9999 order by day desc, time desc""")
    fun fuelStopsOnYear(year: Int): Flow<List<FuelStop>>

    /**
     * Retrieve the average price per volume, volume and price of all fuel stops.
     */
    @Query("""select avg(price_per_volume) as pricePerVolume,
                     avg(total_price) as price,
                     avg(total_volume) as volume
              from fuel_stops""")
    fun averageFuelStats(): Flow<FuelStopAverageValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops between [from] and [to].
     * @param from First day of the range (inclusive)
     * @param to Last day of the range (inclusive)
     */
    @Query("""select avg(price_per_volume) as pricePerVolume,
                     avg(total_price) as price,
                     avg(total_volume) as volume
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
    @Query("""select avg(price_per_volume) as pricePerVolume, 
                     avg(total_price) as price, 
                     avg(total_volume) as volume
              from fuel_stops 
              where day between :from and :to""")
    fun averageFuelStats(from: Int, to: Int): Flow<FuelStopAverageValues>

    /**
     * Retrieve the sum of the volume and price of all fuel stops.
     */
    @Query("""select sum(total_price) as price,
                     sum(total_volume) as volume
              from fuel_stops""")
    fun sumFuelStats(): Flow<FuelStopSumValues>

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops between [from] and [to].
     * @param from First day of the range (inclusive)
     * @param to Last day of the range (inclusive)
     */
    @Query("""select sum(total_price) as price,
                     sum(total_volume) as volume
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
    @Query("""select sum(total_price) as price,
                     sum(total_volume) as volume
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