package io.github.gnush.refueltracker.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.gnush.refueltracker.data.FuelStation
import io.github.gnush.refueltracker.data.FuelStationDao
import io.github.gnush.refueltracker.data.RefuelTrackerDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import kotlin.jvm.Throws

@RunWith(AndroidJUnit4::class)
class FuelStationDaoTest {
    private lateinit var dao: FuelStationDao
    private lateinit var db: RefuelTrackerDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, RefuelTrackerDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.fuelStationDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private val station1 = FuelStation(1, "Station 1", "City")
    private val station2 = FuelStation(2, "Station 2", null)

    private val fuelStations = listOf(station1, station2)

    private suspend fun addFuelStationsToDb(fuelStations: List<FuelStation>) {
        fuelStations.forEach {
            dao.insert(it)
        }
    }

    @Test
    fun daoInsert_insertsIntoDb() = runBlocking {
        addFuelStationsToDb(fuelStations)

        val res = db.query("select * from fuel_station", null)

        assertEquals(2, res.count)
        assertEquals(3, res.columnCount)
        assertEquals(0, res.getColumnIndex("id"))
        assertEquals(1, res.getColumnIndex("name"))
        assertEquals(2, res.getColumnIndex("city"))

        fuelStations.forEach {
            if (res.moveToNext()) {
                assertEquals(it.id, res.getLong(0))
                assertEquals(it.name, res.getString(1))
                assertEquals(it.city, res.getString(2))
            }
        }
    }

    @Test
    fun daoUpdate_updatesInDb() = runBlocking {
        addFuelStationsToDb(listOf(station1))

        val name = "NAME"
        dao.update(FuelStation(1, name))

        val res = db.query("select * from fuel_station", null)

        assertEquals(1, res.count)
        assertEquals(3, res.columnCount)
        assertEquals(0, res.getColumnIndex("id"))
        assertEquals(1, res.getColumnIndex("name"))
        assertEquals(2, res.getColumnIndex("city"))

        if (res.moveToNext()) {
            assertEquals(station1.id, res.getLong(0))
            assertEquals(name, res.getString(1))
        } else {
            fail("no entry in db")
        }
    }

    @Test
    fun daoDelete_deletesFromDb() = runBlocking {
        addFuelStationsToDb(listOf(station1))
        dao.delete(station1)

        val res = db.query("select * from fuel_station", null)

        assertEquals(0, res.count)
    }

    @Test
    fun daoGetIdFromName_returnsIdFromDb() = runBlocking {
        addFuelStationsToDb(fuelStations)

        assertEquals(2, fuelStations.size)
        assertEquals(
            fuelStations[0].id,
            dao.getIdFrom(fuelStations[0].name, fuelStations[0].city!!).first()
        )

        assertEquals(
            fuelStations[1].id,
            dao.getIdFrom(fuelStations[1].name).first()
        )
    }

    @Test
    fun daoGetNameFromId_returnsNameFromDb() = runBlocking {
        addFuelStationsToDb(fuelStations)

        assertEquals(2, fuelStations.size)
        assertEquals(
            fuelStations[0].name,
            dao.getName(fuelStations[0].id).first()
        )
    }
}