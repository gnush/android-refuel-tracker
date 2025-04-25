package io.github.gnush.refueltracker.db

import android.content.Context
import androidx.core.database.getIntOrNull
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import io.github.gnush.refueltracker.data.Converters
import io.github.gnush.refueltracker.data.Currency
import io.github.gnush.refueltracker.data.FuelSort
import io.github.gnush.refueltracker.data.FuelStation
import io.github.gnush.refueltracker.data.FuelStop
import io.github.gnush.refueltracker.data.FuelStopDao
import io.github.gnush.refueltracker.data.FuelStopEntity
import io.github.gnush.refueltracker.data.RefuelTrackerDatabase
import io.github.gnush.refueltracker.data.Volume
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.datetime.DateTimeUnit
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalTime
import kotlinx.datetime.plus
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.fail
import org.junit.Before
import org.junit.Test
import java.io.IOException
import java.math.BigDecimal
import java.math.RoundingMode
import kotlin.jvm.Throws

class FuelStopDaoTest {
    private lateinit var dao: FuelStopDao
    private lateinit var db: RefuelTrackerDatabase

    private val station = FuelStation(1L, "Foo Station")
    private val sort = FuelSort(1L, "E10")
    private val currency = Currency(1L, "Euro")
    private val volume = Volume(1L, "L")

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()

        db = Room.inMemoryDatabaseBuilder(context, RefuelTrackerDatabase::class.java)
            .allowMainThreadQueries()
            .build()

        dao = db.fuelStopDao()

        // Init station, sort, currency and volume tables
        runBlocking {
            db.fuelStationDao().insert(station)
            db.fuelSortDao().insert(sort)
            db.currencyDao().insert(currency)
            db.volumeDao().insert(volume)
        }
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    private val row1 = FuelStopEntity(
        id = 1,
        stationId = station.id,
        fuelSortId = sort.id,
        currencyId = currency.id,
        volumeId = volume.id,
        pricePerVolume = BigDecimal("1.25"),
        totalVolume = BigDecimal("5"),
        totalPrice = BigDecimal("6.25"),
        day = LocalDate(2000, 1, 1),
        time = LocalTime(12, 30)
    )
    private val row2 = FuelStopEntity(
        id = 2,
        stationId = station.id,
        fuelSortId = sort.id,
        currencyId = currency.id,
        volumeId = volume.id,
        pricePerVolume = BigDecimal("2"),
        totalVolume = BigDecimal("2"),
        totalPrice = BigDecimal("4"),
        day = LocalDate(2222, 6, 12),
        time = null
    )
    private val row3 = FuelStopEntity(
        id = 3,
        stationId = station.id,
        fuelSortId = sort.id,
        currencyId = currency.id,
        volumeId = volume.id,
        pricePerVolume = BigDecimal("5"),
        totalVolume = BigDecimal("10"),
        totalPrice = BigDecimal("50"),
        day = LocalDate(1602, 1, 1),
        time = null
    )

    private val rows = listOf(row1, row2, row3)

    private val converters = Converters()

    private suspend fun addRowsToDb(rows: List<FuelStopEntity>) = rows.forEach {
        dao.insert(it)
    }

    @Test
    fun daoInsert_insertsIntoDb() = runBlocking {
        addRowsToDb(rows)

        val res = db.query("select * from fuel_stops", null)

        assertEquals(rows.size, res.count)
        assertEquals(10, res.columnCount)
        assertEquals(0, res.getColumnIndex("id"))
        assertEquals(1, res.getColumnIndex("station_id"))
        assertEquals(2, res.getColumnIndex("fuel_sort_id"))
        assertEquals(3, res.getColumnIndex("currency_id"))
        assertEquals(4, res.getColumnIndex("volume_id"))
        assertEquals(5, res.getColumnIndex("price_per_volume"))
        assertEquals(6, res.getColumnIndex("total_volume"))
        assertEquals(7, res.getColumnIndex("total_price"))
        assertEquals(8, res.getColumnIndex("day"))
        assertEquals(9, res.getColumnIndex("time"))

        rows.forEach {
            if (res.moveToNext()) {
                assertEquals(it.id, res.getLong(0))
                assertEquals(it.stationId, res.getLong(1))
                assertEquals(it.fuelSortId, res.getLong(2))
                assertEquals(it.currencyId, res.getLong(3))
                assertEquals(it.volumeId, res.getLong(4))
                assertEquals(it.pricePerVolume, converters.bigDecimalFromString(res.getString(5)))
                assertEquals(it.totalVolume, converters.bigDecimalFromString(res.getString(6)))
                assertEquals(it.totalPrice, converters.bigDecimalFromString(res.getString(7)))
                assertEquals(it.day, converters.localDateFromInt(res.getInt(8)))
                assertEquals(
                    it.time,
                    if (res.getIntOrNull(9) == null)
                        null
                    else
                        converters.localTimeFromInt(res.getInt(9))
                )
            }
        }
    }

    @Test
    fun daoUpdate_updatesInDb() = runBlocking {
        addRowsToDb(listOf(row1))

        val update = FuelStopEntity(
            row1.id,
            stationId = station.id,
            fuelSortId = sort.id,
            currencyId = currency.id,
            volumeId = volume.id,
            pricePerVolume = BigDecimal("3.33"),
            totalVolume = BigDecimal("3"),
            totalPrice = BigDecimal("9.99"),
            day = LocalDate(1602, 8, 27),
            time = LocalTime(11, 45)
        )

        dao.update(update)

        val res = db.query("select * from fuel_stops", null)

        assertEquals(1, res.count)
        assertEquals(10, res.columnCount)
        assertEquals(0, res.getColumnIndex("id"))
        assertEquals(1, res.getColumnIndex("station_id"))
        assertEquals(2, res.getColumnIndex("fuel_sort_id"))
        assertEquals(3, res.getColumnIndex("currency_id"))
        assertEquals(4, res.getColumnIndex("volume_id"))
        assertEquals(5, res.getColumnIndex("price_per_volume"))
        assertEquals(6, res.getColumnIndex("total_volume"))
        assertEquals(7, res.getColumnIndex("total_price"))
        assertEquals(8, res.getColumnIndex("day"))
        assertEquals(9, res.getColumnIndex("time"))

        if (res.moveToNext()) {
            assertEquals(update.id, res.getLong(0))
            assertEquals(update.stationId, res.getLong(1))
            assertEquals(update.fuelSortId, res.getLong(2))
            assertEquals(update.currencyId, res.getLong(3))
            assertEquals(update.volumeId, res.getLong(4))
            assertEquals(update.pricePerVolume, converters.bigDecimalFromString(res.getString(5)))
            assertEquals(update.totalVolume, converters.bigDecimalFromString(res.getString(6)))
            assertEquals(update.totalPrice, converters.bigDecimalFromString(res.getString(7)))
            assertEquals(update.day, converters.localDateFromInt(res.getInt(8)))
            assertEquals(update.time, converters.localTimeFromInt(res.getInt(9)))
        } else {
            fail("no entry in db")
        }
    }

    @Test
    fun daoDelete_deletesFromDb() = runBlocking {
        addRowsToDb(listOf(row1))
        dao.delete(row1)

        val res = db.query("select * from fuel_stops", null)
        assertEquals(0, res.count)
    }

    @Test
    fun daoFuelStop_returnsTheGivenFuelStopFromDb() = runBlocking {
        addRowsToDb(rows)

        rows.forEach {
            assertEquals(
                it.toFuelStop(),
                dao.fuelStop(it.id).first()
            )
        }
    }

    @Test
    fun daoAllFuelStopsOrderedNewestFirst_returnsAllStopsOrderedByNewestFirstFromDb() = runBlocking {
        addRowsToDb(listOf(row1, row2, row3))

        assertEquals(
            listOf(row2, row1, row3).map { it.toFuelStop() },
            dao.allFuelStopsOrderedNewestFirst().first()
        )
    }

    @Test
    fun daoFuelStopsBetween_returnsAllStopsBetweenTwoDatesOrderedByNewestFirstFromDb() = runBlocking {
        val rows = createOneEntityPerDay(
            LocalDate(2000, 1, 1),
            20
        )

        addRowsToDb(rows)

        assertEquals(
            rows.subList(4, 10).reversed().map { it.toFuelStop() },
            dao.fuelStopsBetween(
                from = LocalDate(2000, 1, 5),
                to = LocalDate(2000, 1, 10)
            ).first()
        )
    }

    @Test
    fun daoFuelStopsOnMonthOfYear_returnsAllStopsOfGivenMonthOrderedByNewestFirstFromDb() = runBlocking {
        val rows = createOneEntityPerDay(
            LocalDate(1999, 12, 21),
            60
        )

        addRowsToDb(rows)

        assertEquals(
            rows.filter { it.day.year == 2000 && it.day.monthNumber == 1 }
                .reversed().map { it.toFuelStop() },
            dao.fuelStopsOnMonthOfYear(20000100).first()
        )
    }

    @Test
    fun daoFuelStopsOnfYear_returnsAllStopsOfGivenYearOrderedByNewestFirstFromDb() = runBlocking {
        val rows = createOneEntityPerDay(
            LocalDate(1999, 12, 21),
            50
        )

        addRowsToDb(rows)

        assertEquals(
            rows.filter { it.day.year == 2000 }
                .reversed().map { it.toFuelStop() },
            dao.fuelStopsOnYear(20000000).first()
        )
    }

    @Test
    fun daoAverageFuelStats_returnsAverageValuesFromDb() = runBlocking {
        addRowsToDb(rows)

        val averages = dao.averageFuelStats().first()

        assertEquals(
            rows.sumOf { it.pricePerVolume }
                .divide(BigDecimal(rows.size), 5, RoundingMode.HALF_UP)
                .toDouble(),
            averages.pricePerVolume.toDouble(),
            0.0001
        )
        assertEquals(
            rows.sumOf { it.totalVolume }
                .divide(BigDecimal(rows.size), 5, RoundingMode.HALF_UP)
                .toDouble(),
            averages.volume.toDouble(),
            0.0001
        )
        assertEquals(
            rows.sumOf { it.totalPrice }
                .divide(BigDecimal(rows.size), 5, RoundingMode.HALF_UP)
                .toDouble(),
            averages.price.toDouble(),
            0.0001
        )
    }

    @Test
    fun daoSumFuelStats_returnsSumsFromDb() = runBlocking {
        addRowsToDb(rows)

        val sums = dao.sumFuelStats().first()

        assertEquals(
            rows.sumOf { it.totalPrice }.toDouble(),
            sums.price.toDouble(),
            0.0001
        )
        assertEquals(
            rows.sumOf { it.totalVolume }.toDouble(),
            sums.volume.toDouble(),
            0.0001
        )
    }

    private fun FuelStopEntity.toFuelStop() = FuelStop(
        id = id,
        station = if (stationId == station.id) station.name else "",
        fuelSort = if (fuelSortId == sort.id) sort.label else "",
        currency = if (currencyId == currency.id) currency.symbol else "",
        volume = volume.symbol,
        pricePerVolume = pricePerVolume,
        totalVolume = totalVolume,
        totalPrice = totalPrice,
        day = day,
        time = time
    )

    private fun createOneEntityPerDay(startDate: LocalDate, numEntities: Long)
    : List<FuelStopEntity> = (0 until numEntities).map {
        FuelStopEntity(
            id = it,
            stationId = station.id,
            fuelSortId = sort.id,
            currencyId = currency.id,
            volumeId = volume.id,
            pricePerVolume = BigDecimal.ONE,
            totalVolume = BigDecimal.ONE,
            totalPrice = BigDecimal.ONE,
            day = startDate.plus(it, DateTimeUnit.DAY),
            time = null
        )
    }
}