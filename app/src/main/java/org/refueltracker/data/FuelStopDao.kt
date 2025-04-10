package org.refueltracker.data

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
    suspend fun insert(fuelStop: FuelStop)

    @Update
    suspend fun update(fuelStop: FuelStop)

    @Delete
    suspend fun delete(fuelStop: FuelStop)

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    @Query("select * from fuel_stops where id = :id")
    fun fuelStop(id: Int): Flow<FuelStop>

    /**
     * Retrieves all fuel stops in descending order by day/time
     */
    @Query("select * from fuel_stops order by day desc, time desc")
    fun allFuelStopsOrderedNewestFirst(): Flow<List<FuelStop>>

    /**
     * Retrieves all fuel stops between [from] and [to] in descending order by day/time
     */
    @Query("select * from fuel_stops where day between :from and :to order by day desc, time desc")
    fun fuelStopsBetween(from: LocalDate, to: LocalDate): Flow<List<FuelStop>>

    /**
     * Retrieves all fuel stops from [monthOfYear] in descending order by day/time
     */
    @Query("select * from fuel_stops where day between :monthOfYear and :monthOfYear+99 order by day desc, time desc")
    fun fuelStopsOnMonthOfYear(monthOfYear: Int): Flow<List<FuelStop>>

    /**
     * Retrieves all fuel stops from [year] in descending order by day/time
     */
    @Query("select * from fuel_stops where day between :year and :year+9999 order by day desc, time desc")
    fun fuelStopsOnYear(year: Int): Flow<List<FuelStop>>

    /**
     * Retrieve the average price per volume, volume and price of all fuel stops.
     */
    @Query("""select avg(pricePerVolume) as pricePerVolume,
                     avg(totalPrice) as price,
                     avg(totalVolume) as volume
              from fuel_stops""")
    fun averageFuelStats(): Flow<FuelStopDecimalValues>

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
    fun averageFuelStats(from: LocalDate, to: LocalDate): Flow<FuelStopDecimalValues>

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops between [from] and [to].
     *
     * Assumes parameters are in yyyyMMdd format.
     * See [Converters.localDateToInt] for more information on the format or use [FuelStopDao.averageFuelStats].
     * @param from First day of the range (inclusive)
     * @param to Last day of the range (inclusive)
     */
    @Query("""select avg(pricePerVolume) as pricePerVolume, 
                     avg(totalPrice) as price, 
                     avg(totalVolume) as volume
              from fuel_stops 
              where day between :from and :to""")
    fun averageFuelStats(from: Int, to: Int): Flow<FuelStopDecimalValues>
}