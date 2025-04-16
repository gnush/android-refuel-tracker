package gnush.refueltracker.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import gnush.refueltracker.RefuelTrackerApplication
import gnush.refueltracker.ui.calendar.CalendarViewModel
import gnush.refueltracker.ui.fuelstop.FuelStopCalendarViewModel
import gnush.refueltracker.ui.fuelstop.FuelStopEntryViewModel
import gnush.refueltracker.ui.fuelstop.FuelStopListViewModel
import gnush.refueltracker.ui.statistic.StatisticsHomeViewModel

object RefuelTrackerViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FuelStopListViewModel(
                userPreferencesRepository = refuelTrackerApplication().container.userPreferencesRepository,
                fuelStopsRepository = refuelTrackerApplication().container.fuelStopsRepository
            )
        }
        initializer {
            FuelStopCalendarViewModel(
                userPreferencesRepository = refuelTrackerApplication().container.userPreferencesRepository,
                fuelStopsRepository = refuelTrackerApplication().container.fuelStopsRepository
            )
        }
        initializer {
            FuelStopEntryViewModel(
                userPreferences = refuelTrackerApplication().container.userPreferencesRepository,
                fuelStopsRepository = refuelTrackerApplication().container.fuelStopsRepository,
                savedStateHandle = createSavedStateHandle()
            )
        }
        initializer {
            StatisticsHomeViewModel(
                userPreferencesRepository = refuelTrackerApplication().container.userPreferencesRepository,
                fuelStopsRepository = refuelTrackerApplication().container.fuelStopsRepository
            )
        }
        initializer {
            CalendarViewModel()
        }
    }
}

fun CreationExtras.refuelTrackerApplication(): RefuelTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as RefuelTrackerApplication)