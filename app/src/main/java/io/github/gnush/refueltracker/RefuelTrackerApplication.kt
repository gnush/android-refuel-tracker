package io.github.gnush.refueltracker

import android.app.Application
import io.github.gnush.refueltracker.data.AppContainer
import io.github.gnush.refueltracker.data.AppDataContainer

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