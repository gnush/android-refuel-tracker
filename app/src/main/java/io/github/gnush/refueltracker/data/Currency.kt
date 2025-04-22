package io.github.gnush.refueltracker.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "currency")
data class Currency(
    @PrimaryKey(autoGenerate = true) val id: Long = 0L,
    val symbol: String
)