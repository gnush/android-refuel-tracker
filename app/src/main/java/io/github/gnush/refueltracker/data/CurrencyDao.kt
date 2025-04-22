package io.github.gnush.refueltracker.data

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface CurrencyDao {
    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(x: Currency): Long

    @Update
    suspend fun update(x: Currency)

    @Delete
    suspend fun delete(x: Currency)

    @Query("select id from currency where symbol=:symbol")
    fun getIdFrom(symbol: String): Flow<Long?>

    @Query("select symbol from currency where id=:id")
    fun getSymbol(id: Long): Flow<String?>
}