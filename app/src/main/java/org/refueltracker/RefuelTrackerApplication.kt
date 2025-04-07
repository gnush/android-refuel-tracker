package org.refueltracker

import android.app.Application
import org.refueltracker.data.AppContainer
import org.refueltracker.data.AppDataContainer

class RefuelTrackerApplication: Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
    }
}