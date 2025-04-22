package io.github.gnush.refueltracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "volume")
data class Volume(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val symbol: String
)