package org.refueltracker.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.createSavedStateHandle
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.refueltracker.RefuelTrackerApplication
import org.refueltracker.ui.calendar.CalendarViewModel
import org.refueltracker.ui.fuelstop.FuelStopCalendarViewModel
import org.refueltracker.ui.fuelstop.FuelStopEditViewModel
import org.refueltracker.ui.fuelstop.FuelStopEntryViewModel
import org.refueltracker.ui.fuelstop.FuelStopListViewModel

object RefuelTrackerViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FuelStopListViewModel(
                refuelTrackerApplication().container.fuelStopsRepository
            )
        }
        initializer {
            FuelStopCalendarViewModel(
                refuelTrackerApplication().container.fuelStopsRepository
            )
        }
        initializer {
            FuelStopEntryViewModel(
                refuelTrackerApplication().container.fuelStopsRepository
            )
        }
        initializer {
            FuelStopEditViewModel(
                createSavedStateHandle(),
                refuelTrackerApplication().container.fuelStopsRepository
            )
        }
        initializer {
            CalendarViewModel()
        }
    }
}

fun CreationExtras.refuelTrackerApplication(): RefuelTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as RefuelTrackerApplication)