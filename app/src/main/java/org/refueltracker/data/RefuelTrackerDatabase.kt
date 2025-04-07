package org.refueltracker.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [FuelStop::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class RefuelTrackerDatabase: RoomDatabase() {
    abstract fun fuelStopDao(): FuelStopDao

    companion object {
        @Volatile
        private var Instance: RefuelTrackerDatabase? = null

        fun getDatabase(context: Context): RefuelTrackerDatabase =
            Instance ?: synchronized(this) {
                Room.databaseBuilder(context, RefuelTrackerDatabase::class.java, "refuel_tracker_database")
                    .fallbackToDestructiveMigration()
                    .build()
                    .also { Instance = it }
            }
    }
}