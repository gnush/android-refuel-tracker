package io.github.gnush.refueltracker.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.github.gnush.refueltracker.RefuelTrackerApplication
import io.github.gnush.refueltracker.ui.settings.SettingsViewModel
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopCalendarViewModel
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopEntryViewModel
import io.github.gnush.refueltracker.ui.fuelstop.FuelStopListViewModel
import io.github.gnush.refueltracker.ui.statistic.StatisticsHomeViewModel

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
            SettingsViewModel(
                userPreferencesRepository = refuelTrackerApplication().container.userPreferencesRepository
            )
        }
    }
}

fun CreationExtras.refuelTrackerApplication(): RefuelTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as RefuelTrackerApplication)