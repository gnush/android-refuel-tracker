package io.github.gnush.refueltracker.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import io.github.gnush.refueltracker.data.FuelSort
import io.github.gnush.refueltracker.data.FuelSortDao
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
class FuelSortDaoTest {
    private lateinit var fuelSortDao: FuelSortDao
    private lateinit var db: RefuelTrackerDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, RefuelTrackerDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        fuelSortDao = db.fuelSortDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private val sort1 = FuelSort(1, "E10")
    private val sort2 = FuelSort(2, "Diesel")

    private val fuelSorts = listOf(sort1, sort2)

    private suspend fun addFuelSortsToDb(fuelSorts: List<FuelSort>) {
        fuelSorts.forEach {
            fuelSortDao.insert(it)
        }
    }

    @Test
    fun daoInsert_insertsIntoDb() = runBlocking {
        addFuelSortsToDb(fuelSorts)

        val res = db.query("select * from fuel_sort", null)

        assertEquals(2, res.count)
        assertEquals(2, res.columnCount)
        assertEquals(0, res.getColumnIndex("id"))
        assertEquals(1, res.getColumnIndex("label"))

        fuelSorts.forEach {
            if (res.moveToNext()) {
                assertEquals(it.id, res.getLong(0))
                assertEquals(it.label, res.getString(1))
            } else {
                fail("no entry in db")
            }
        }
    }

    @Test
    fun daoUpdate_updatesInDb() = runBlocking {
        addFuelSortsToDb(listOf(sort1))

        val label = "LABEL"
        fuelSortDao.update(FuelSort(1, label))

        val res = db.query("select * from fuel_sort", null)

        assertEquals(1, res.count)
        assertEquals(2, res.columnCount)
        assertEquals(0, res.getColumnIndex("id"))
        assertEquals(1, res.getColumnIndex("label"))

        if (res.moveToNext() && res.isFirst && res.isLast) {
            assertEquals(sort1.id, res.getLong(0))
            assertEquals(label, res.getString(1))
        }
    }

    @Test
    fun daoDelete_deletesFromDb() = runBlocking {
        addFuelSortsToDb(listOf(sort1))
        fuelSortDao.delete(sort1)

        val res = db.query("select * from fuel_sort", null)

        assertEquals(0, res.count)
    }

    @Test
    fun daoGetIdFromLabel_returnsIdFromDb() = runBlocking {
        addFuelSortsToDb(fuelSorts)

        assertEquals(2, fuelSorts.size)
        val id = fuelSortDao.getIdFrom(fuelSorts[1].label).first()
        assertEquals(fuelSorts[1].id, id)
    }

    @Test
    fun daoGetLabelFromId_returnsLabelFromDb() = runBlocking {
        addFuelSortsToDb(fuelSorts)

        assertEquals(2, fuelSorts.size)
        val label = fuelSortDao.getLabel(fuelSorts[0].id).first()
        assertEquals(fuelSorts[0].label, label)
    }
}