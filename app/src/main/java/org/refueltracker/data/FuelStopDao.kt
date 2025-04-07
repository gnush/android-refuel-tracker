package org.refueltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

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
}