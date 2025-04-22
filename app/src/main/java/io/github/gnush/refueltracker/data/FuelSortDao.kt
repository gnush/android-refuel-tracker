package io.github.gnush.refueltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface FuelSortDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(x: FuelSort): Long

    @Update
    suspend fun update(x: FuelSort)

    @Delete
    suspend fun delete(x: FuelSort)

    @Query("select id from fuel_sort where label=:label")
    fun getIdFrom(label: String): Flow<Long?>

    @Query("select label from fuel_sort where id=:id")
    fun getLabel(id: Long): Flow<String?>
}