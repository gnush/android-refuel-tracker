package gnush.refueltracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "fuel_station")
data class FuelStation(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val name: String,
    val city: String? = null
)