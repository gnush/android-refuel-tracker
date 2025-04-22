package io.github.gnush.refueltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelStationDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(x: FuelStation): Long

    @Update
    suspend fun update(x: FuelStation)

    @Delete
    suspend fun delete(x: FuelStation)

    @Query("select id from fuel_station where name=:name and city=:city")
    fun getIdFrom(name: String, city: String): Flow<Long?>

    @Query("select id from fuel_station where name=:name and city is null")
    fun getIdFrom(name: String): Flow<Long?>

    @Query("select name from fuel_station where id=:id")
    fun getName(id: Long): Flow<String?>
}