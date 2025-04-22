package io.github.gnush.refueltracker.ui.dialog

import android.text.format.DateFormat
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.DatePicker
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TimePicker
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import kotlinx.datetime.LocalTime
import kotlinx.datetime.format
import io.github.gnush.refueltracker.ui.Config
import java.util.Calendar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickDateDialog(
    onDateSelected: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val datePickerState = rememberDatePickerState()

    ConfirmDismissDialog(
        onConfirm = {
            val dayMillis = datePickerState.selectedDateMillis
            if (dayMillis != null) {
                onDateSelected(
                    DateFormat.format("dd.MM.yyyy", dayMillis).toString()
                )
            }
        },
        onDismiss = onDismiss
    ) {
        DatePicker(state = datePickerState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PickTimeDialDialog(
    onConfirm: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val currentTime = Calendar.getInstance()

    val timePickerState = rememberTimePickerState(
        initialHour = currentTime.get(Calendar.HOUR_OF_DAY),
        initialMinute = currentTime.get(Calendar.MINUTE),
        is24Hour = true
    )

    ConfirmDismissDialog(
        onConfirm = {
            onConfirm(
                LocalTime(
                    hour = timePickerState.hour,
                    minute = timePickerState.minute
                ).format(Config.TIME_FORMAT)
            )
        },
        onDismiss = onDismiss
    ) {
        TimePicker(state = timePickerState)
    }
}

@Composable
private fun ConfirmDismissDialog(
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
    content: @Composable () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Dismiss")
            }
        },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("OK")
            }
        },
        text = { content() }
    )
}