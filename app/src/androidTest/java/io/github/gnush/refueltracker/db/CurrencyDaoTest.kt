package io.github.gnush.refueltracker.db

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.github.gnush.refueltracker.data.Currency
import io.github.gnush.refueltracker.data.CurrencyDao
import io.github.gnush.refueltracker.data.RefuelTrackerDatabase
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.io.IOException
import kotlin.jvm.Throws

class CurrencyDaoTest {
    private lateinit var dao: CurrencyDao
    private lateinit var db: RefuelTrackerDatabase

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, RefuelTrackerDatabase::class.java)
            .allowMainThreadQueries()
            .build()
        dao = db.currencyDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private val row1 = Currency(1, "Euro")
    private val row2 = Currency(2, "Yen")
    private val rows = listOf(row1, row2)

    private suspend fun addRowsToDb(rows: List<Currency>) = rows.forEach {
        dao.insert(it)
    }

    @Test
    fun daoInsert_insertsIntoDb() = runBlocking {
        addRowsToDb(rows)

        val res = db.query("select * from currency", null)

        assertEquals(rows.size, res.count)
        assertEquals(2, res.columnCount)
        assertEquals(0, res.getColumnIndex("id"))
        assertEquals(1, res.getColumnIndex("symbol"))

        rows.forEach {
            if (res.moveToNext()) {
                assertEquals(it.id, res.getLong(0))
                assertEquals(it.symbol, res.getString(1))
            }
        }
    }

    @Test
    fun daoUpdate_updatesInDb() = runBlocking {
        addRowsToDb(listOf(row1))

        val symbol = "SYMBOL"
        dao.update(Currency(row1.id, symbol))

        val res = db.query("select * from currency", null)

        assertEquals(1, res.count)
        assertEquals(2, res.columnCount)
        assertEquals(0, res.getColumnIndex("id"))
        assertEquals(1, res.getColumnIndex("symbol"))

        if (res.moveToNext()) {
            assertEquals(row1.id, res.getLong(0))
            assertEquals(symbol, res.getString(1))
        } else {
            fail("no entry in db")
        }
    }

    @Test
    fun daoDelete_deletesFromDb() = runBlocking {
        addRowsToDb(listOf(row1))
        dao.delete(row1)

        val res = db.query("select * from currency", null)
        assertEquals(0, res.count)
    }

    @Test
    fun daoGetIdFromSymbol_returnsIdFromDb() = runBlocking {
        addRowsToDb(rows)

        rows.forEach {
            assertEquals(
                it.id,
                dao.getIdFrom(it.symbol).first()
            )
        }
    }

    @Test
    fun daoGetSymbolFromId_returnsSymbolFromDb() = runBlocking {
        addRowsToDb(rows)

        rows.forEach {
            assertEquals(
                it.symbol,
                dao.getSymbol(it.id).first()
            )
        }
    }
}