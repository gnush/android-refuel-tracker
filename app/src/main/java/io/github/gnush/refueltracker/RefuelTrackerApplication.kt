package io.github.gnush.refueltracker

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import io.github.gnush.refueltracker.data.AppContainer
import io.github.gnush.refueltracker.data.AppDataContainer
import io.github.gnush.refueltracker.data.UserPreferencesRepository

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