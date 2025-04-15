package gnush.refueltracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fuel_sort")
data class FuelSort(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val label: String
)