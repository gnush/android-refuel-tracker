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

    // TODO: how to tell Room to convert this: three fields selected to triple (or some other box object)
//    // dont need to cast: select  avg(pricePerVolume) as ppv, avg(totalPrice) as vol, avg(totalVolume) as price from fuel_stops;
//    /**
//     * Retrieve the average price per volume, volume and price of all fuel stops.
//     */
//    @Query("select " +
//            "avg(cast(pricePerVolume as real)), " +
//            "avg(cast(totalPrice as real)), " +
//            "avg(cast(totalVolume as real)) from fuel_stops")
//    fun allTimeAverageFuelStats(): Flow<List<Triple<Double, Double, Double>>>  // doesnt work with or without list in return type
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops between [from] and [to] (both inclusive).
//     */
//    @Query("select " +
//            "avg(cast(pricePerVolume as real)), " +
//            "avg(cast(totalPrice as real)), " +
//            "avg(cast(totalVolume as real)) from fuel_stops " +
//            "where day between :from and :to")
//    fun averageFuelStats(from: LocalDate, to: LocalDate): Flow<List<Triple<Double, Double, Double>>>
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops of [year].
//     */
//    @Query("select " +
//            "avg(cast(pricePerVolume as real)), " +
//            "avg(cast(totalPrice as real)), " +
//            "avg(cast(totalVolume as real)) from fuel_stops " +
//            "where day between :year and :year+9999")
//    fun averageFuelStatsByYear(year: Int): Flow<List<Triple<Double, Double, Double>>>
//
//    /**
//     * Retrieve the average price per volume, volume and price
//     * of all fuel stops of [monthOfYear].
//     */
//    @Query("select " +
//            "avg(pricePerVolume), " +
//            "avg(totalPrice), " +
//            "avg(totalVolume) from fuel_stops " +
//            "where day between :monthOfYear and :monthOfYear+99")
//    fun averageFuelStatsByMonthOfYear(monthOfYear: Int): Flow<List<Triple<Double, Double, Double>>>
}