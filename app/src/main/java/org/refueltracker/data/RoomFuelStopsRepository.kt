package org.refueltracker.data

import kotlinx.coroutines.flow.Flow
import kotlinx.datetime.LocalDate
import kotlinx.datetime.number
import java.time.Month

class RoomFuelStopsRepository(private val fuelStopDao: FuelStopDao): FuelStopsRepository {
    /**
     * Retrieves all fuel stops ordered by time  in descending order by day/time
     */
    override fun fuelStopsOrderedNewestFirst(): Flow<List<FuelStop>> =
        fuelStopDao.allFuelStopsOrderedNewestFirst()

    /**
     * Retrieves all fuel stops between [from] and [to] (inclusive)  in descending order by day/time
     */
    override fun fuelStopsBetween(from: LocalDate, to: LocalDate): Flow<List<FuelStop>> =
        fuelStopDao.fuelStopsBetween(from, to)

    /**
     * Retrieves all fuel stops with year [year] and month [month] in descending order by day/time
     */
    override fun fuelStopsOn(year: Int, month: Month): Flow<List<FuelStop>> =
        fuelStopDao.fuelStopsOn(year*10000 + month.number*100)

    /**
     * Retrieves a specific fuel stop
     * @param id The id of the fuel stop to retrieve
     */
    override fun fuelStop(id: Int): Flow<FuelStop?> = fuelStopDao.fuelStop(id)

    override suspend fun insert(fuelStop: FuelStop) = fuelStopDao.insert(fuelStop)

    override suspend fun update(fuelStop: FuelStop) = fuelStopDao.update(fuelStop)

    override suspend fun delete(fuelStop: FuelStop) = fuelStopDao.delete(fuelStop)
}