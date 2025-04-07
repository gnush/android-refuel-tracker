package org.refueltracker.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import org.refueltracker.RefuelTrackerApplication
import org.refueltracker.ui.fuelstop.FuelStopEntryViewModel
import org.refueltracker.ui.fuelstop.FuelStopHomeViewModel

object RefuelTrackerViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            FuelStopHomeViewModel(
                refuelTrackerApplication().container.fuelStopsRepository
            )
        }
        initializer {
            FuelStopEntryViewModel(
                refuelTrackerApplication().container.fuelStopsRepository
            )
        }
    }
}

fun CreationExtras.refuelTrackerApplication(): RefuelTrackerApplication =
    (this[AndroidViewModelFactory.APPLICATION_KEY] as RefuelTrackerApplication)