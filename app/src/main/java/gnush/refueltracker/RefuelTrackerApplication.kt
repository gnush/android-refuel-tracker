package gnush.refueltracker

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import gnush.refueltracker.data.AppContainer
import gnush.refueltracker.data.AppDataContainer
import gnush.refueltracker.data.UserPreferencesRepository

private const val USER_PREFERENCES_NAME = "user_preferences"
private val Context.datastore: DataStore<Preferences> by preferencesDataStore(
    name = USER_PREFERENCES_NAME
)

class RefuelTrackerApplication: Application() {
    /**
     * AppContainer instance used by the rest of classes to obtain dependencies
     */
    lateinit var container: AppContainer

    /**
     * User preferences repository
     */
    lateinit var preferences: UserPreferencesRepository

    override fun onCreate() {
        super.onCreate()
        container = AppDataContainer(this)
        preferences = UserPreferencesRepository(datastore)
    }
}