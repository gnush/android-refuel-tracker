package io.github.gnush.refueltracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import java.time.Month

fun FuelStopEntity.toFuelStop(station: String, sort: String, currency: String, volume: String): FuelStop = FuelStop(
    id = id,
    station = station,
    fuelSort = sort,
    currency = currency,
    volume = volume,
    pricePerVolume = pricePerVolume,
    totalVolume = totalVolume,
    totalPrice = totalPrice,
    day = day,
    time = time
)

fun FuelStop.toFuelStopEntity(stationId: Long, sortId: Long, currencyId: Long, volumeId: Long): FuelStopEntity = FuelStopEntity(
    id = id,
    stationId = stationId,
    fuelSortId = sortId,
    currencyId = currencyId,
    volumeId = volumeId,
    pricePerVolume = pricePerVolume,
    totalVolume = totalVolume,
    totalPrice = totalPrice,
    day = day,
    time = time
)

class RoomFuelStopsRepository(
    private val fuelStopDao: FuelStopDao,
    private val fuelStationDao: FuelStationDao,
    private val fuelSortDao: FuelSortDao,
    private val currencyDao: CurrencyDao,
    private val volumeDao: VolumeDao
): FuelStopsRepository {
    private suspend fun getSortIdOrInsert(fuelSort: String): Long =
        fuelSortDao.getIdFrom(fuelSort).first()
            ?: fuelSortDao.insert(FuelSort(label = fuelSort))

    private suspend fun getStationIdOrInsert(name: String): Long =
        fuelStationDao.getIdFrom(name).first()
            ?: fuelStationDao.insert(FuelStation(name = name))

    private suspend fun getCurrencyIdOrInsert(symbol: String): Long =
        currencyDao.getIdFrom(symbol).first()
            ?: currencyDao.insert(Currency(symbol = symbol))

    private suspend fun getVolumeIdOrInsert(symbol: String): Long =
        volumeDao.getIdFrom(symbol).first()
            ?: volumeDao.insert(Volume(symbol = symbol))

    override suspend fun insert(fuelStop: FuelStop): Long = fuelStopDao.insert(
        fuelStop.toFuelStopEntity(
            stationId = getStationIdOrInsert(fuelStop.station),
            sortId    = getSortIdOrInsert(fuelStop.fuelSort),
            currencyId = getCurrencyIdOrInsert(symbol = fuelStop.currency),
            volumeId = getVolumeIdOrInsert(symbol = fuelStop.volume)
        )
    )

    override suspend fun update(fuelStop: FuelStop) = fuelStopDao.update(
        fuelStop.toFuelStopEntity(
            stationId = getStationIdOrInsert(fuelStop.station),
            sortId = getSortIdOrInsert(fuelStop.fuelSort),
            currencyId = getCurrencyIdOrInsert(symbol = fuelStop.currency),
            volumeId = getVolumeIdOrInsert(symbol = fuelStop.volume)
        )
    )

    override suspend fun delete(fuelStop: FuelStop) = fuelStopDao.delete(
        fuelStop.toFuelStopEntity(
            stationId = getStationIdOrInsert(fuelStop.station),
            sortId = getSortIdOrInsert(fuelStop.fuelSort),
            currencyId = getCurrencyIdOrInsert(symbol = fuelStop.currency),
            volumeId = getVolumeIdOrInsert(symbol = fuelStop.volume)
        )
    )

    /**
     * Retrieves all fuel stops ordered by time  in descending order by day/time
     */
    override fun fuelStopsOrderedNewestFirst(): Flow<List<FuelStop>> =
        fuelStopDao.allFuelStopsOrderedNewestFirst().map { it.map { stop ->
            stop.toFuelStop(
                station = fuelStationDao.getName(stop.stationId).first() ?: "",
                sort = fuelSortDao.getLabel(stop.fuelSortId).first() ?: "",
                currency = currencyDao.getSymbol(stop.currencyId).first() ?: "",
                volume = volumeDao.getSymbol(stop.volumeId).first() ?: ""
            )
        } }

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    override fun fuelStop(id: Int): Flow<FuelStop?> = fuelStopDao.fuelStop(id).map {
        it.toFuelStop(
            station = fuelStationDao.getName(it.stationId).first() ?: "",
            sort = fuelSortDao.getLabel(it.fuelSortId).first() ?: "",
            currency = currencyDao.getSymbol(it.currencyId).first() ?: "",
            volume = volumeDao.getSymbol(it.volumeId).first() ?: ""
        )
    }

    /**
     * Retrieves all fuel stops between [from] and [to] (inclusive)  in descending order by day/time
     */
    override fun fuelStopsBetween(from: LocalDate, to: LocalDate): Flow<List<FuelStop>> =
        fuelStopDao.fuelStopsBetween(from, to).map { it.map { stop ->
            stop.toFuelStop(
                station = fuelStationDao.getName(stop.stationId).first() ?: "",
                sort = fuelSortDao.getLabel(stop.fuelSortId).first() ?: "",
                currency = currencyDao.getSymbol(stop.currencyId).first() ?: "",
                volume = volumeDao.getSymbol(stop.volumeId).first() ?: ""
            )
        } }

    /**
     * Retrieves all fuel stops with year [year] and month [month] in descending order by day/time
     */
    override fun fuelStopsOn(year: Int, month: Month): Flow<List<FuelStop>> =
        fuelStopDao.fuelStopsOnMonthOfYear(year*10000 + month.number*100).map { it.map { stop ->
            stop.toFuelStop(
                station = fuelStationDao.getName(stop.stationId).first() ?: "",
                sort = fuelSortDao.getLabel(stop.fuelSortId).first() ?: "",
                currency = currencyDao.getSymbol(stop.currencyId).first() ?: "",
                volume = volumeDao.getSymbol(stop.volumeId).first() ?: ""
            )
        } }

    /**
     * Retrieves all fuel stops from [year] in descending order by day/time
     */
    override fun fuelStopsOn(year: Int): Flow<List<FuelStop>> =
        fuelStopDao.fuelStopsOnYear(year*10000).map { it.map { stop ->
            stop.toFuelStop(
                station = fuelStationDao.getName(stop.stationId).first() ?: "",
                sort = fuelSortDao.getLabel(stop.fuelSortId).first() ?: "",
                currency = currencyDao.getSymbol(stop.currencyId).first() ?: "",
                volume = volumeDao.getSymbol(stop.volumeId).first() ?: ""
            )
        } }

    /**
     * Retrieve the average price per volume, volume and price of all fuel stops.
     */
    override fun averageFuelStats(): Flow<FuelStopAverageValues> =
        fuelStopDao.averageFuelStats()

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops between [from] and [to] (both inclusive).
     */
    override fun averageFuelStats(from: LocalDate,to: LocalDate): Flow<FuelStopAverageValues> =
        fuelStopDao.averageFuelStats(from, to)

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops of [year].
     * E.g. for year=2000, all days with pattern 2000-MM-dd.
     */
    override fun averageFuelStats(year: Int): Flow<FuelStopAverageValues> =
        fuelStopDao.averageFuelStats(year*10000, year*10000+9999)

    /**
     * Retrieve the average price per volume, volume and price
     * of all fuel stops of month [month] of year [year].
     * E.g. for year=2000 and month=FEBRUARY, all days with pattern 2000-02-dd.
     */
    override fun averageFuelStats(year: Int, month: Month): Flow<FuelStopAverageValues> = fuelStopDao.averageFuelStats(
        from = year*10000 + month.number*100,
        to = year*10000 + month.number*100 + 99
    )

    /**
     * Retrieve the sum of the volume and price of all fuel stops.
     */
    override fun sumFuelStats(): Flow<FuelStopSumValues> = fuelStopDao.sumFuelStats()

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops between [from] and [to] (both inclusive).
     */
    override fun sumFuelStats(from: LocalDate,to: LocalDate): Flow<FuelStopSumValues> =
        fuelStopDao.sumFuelStats(from, to)

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops of [year].
     * E.g. for year=2000, all days with pattern 2000-MM-dd.
     */
    override fun sumFuelStats(year: Int): Flow<FuelStopSumValues> =
        fuelStopDao.sumFuelStats(year*10000, year*10000 + 9999)

    /**
     * Retrieve the sum of the volume and price
     * of all fuel stops of month [month] of year [year].
     * E.g. for year=2000 and month=FEBRUARY, all days with pattern 2000-02-dd.
     */
    override fun sumFuelStats(year: Int, month: Month): Flow<FuelStopSumValues> = fuelStopDao.sumFuelStats(
        from = year*10000 + month.number*100,
        to = year*10000 + month.number*100 + 99
    )

    /**
     * Retrieves the most often fueled fuel sort.
     */
    override fun mostUsedFuelSort(): Flow<String?> = fuelStopDao.mostUsedFuelSort()

    /**
     * Retrieves the [n] most often fueled fuel sorts.
     */
    override fun mostUsedFuelSorts(n: Int): Flow<List<String>> = fuelStopDao.mostUsedFuelSorts(n)

    /**
     * Retrieves the [n] most recently fueled fuel sorts.
     */
    override fun mostRecentFuelSorts(n: Int): Flow<List<String>> = fuelStopDao.mostRecentFuelSorts(n)

    /**
     * Retrieves the [n] most often fueled fuel sorts.
     */
    override fun mostUsedFuelStations(n: Int): Flow<List<String>> = fuelStopDao.mostUsedFuelStations(n)

    /**
     * Retrieves the [n] most recently fueled fuel sorts.
     */
    override fun mostRecentFuelStations(n: Int): Flow<List<String>> = fuelStopDao.mostRecentFuelStations(n)

    // unclean interface
//    fun foobar(n: Int, stations: Boolean = true, mostRecent: Boolean = true): Flow<List<String>> = when {
//        stations && mostRecent -> fuelStopDao.mostRecentFuelStations(n)
//        stations && !mostRecent -> fuelStopDao.mostUsedFuelStations(n)
//        !stations && mostRecent -> fuelStopDao.mostRecentFuelSorts(n)
//        else -> fuelStopDao.mostUsedFuelSorts(n)
//    }
}