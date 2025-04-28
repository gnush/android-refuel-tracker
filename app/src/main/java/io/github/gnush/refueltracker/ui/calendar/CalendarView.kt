package io.github.gnush.refueltracker.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import io.github.gnush.refueltracker.ui.dialog.ConfirmDismissDialog
import io.github.gnush.refueltracker.ui.extensions.abbreviationId
import io.github.gnush.refueltracker.ui.extensions.monthOfYearId
import io.github.gnush.refueltracker.ui.theme.RefuelTrackerTheme

// TODO:
//  - remove calendar view model (double data storage)?
//    at least remove the navigation functions (should be only filled through caller)
@Composable
fun CalendarView(
    modifier: Modifier = Modifier,
    firstDisplayMonth: Month? = null,
    firstDisplayYear: Int? = null,
    selectedDays: List<LocalDate> = listOf(),
    startFromSunday: Boolean = false,
    canNavigateMonth: Boolean = false,
    onNextMonthClick: () -> Unit = {},
    onPreviousMonthClick: () -> Unit = {},
    onMonthSelected: (Int, Month) -> Unit = { _, _ -> },
    hasClickableCells: Boolean = false,
    onCellClick: (LocalDate) -> Unit = {},
    viewModel: CalendarViewModel = viewModel( factory = RefuelTrackerViewModelProvider.Factory )
) {
    if (firstDisplayMonth != null)
        viewModel.updateDisplayMonth(firstDisplayMonth)

    if (firstDisplayYear != null)
        viewModel.updateDisplayYear(firstDisplayYear)

    Column(
        modifier = modifier
    ) {
        CalendarHeader(
            uiState = viewModel.uiState,
            canNavigateMonth = canNavigateMonth,
            onNextMonthClick = {
                viewModel.setNextMonth()
                onNextMonthClick()
            },
            onPreviousMonthClick = {
                viewModel.setPreviousMonth()
                onPreviousMonthClick()
            },
            onMonthSelected = { year, month ->
                viewModel.updateDisplayMonth(year, month)
                onMonthSelected(year, month)
            }
        )
        CalendarGrid(
            firstWeekDayOfMonth = viewModel.firstWeekDayOfMonth(),
            daysOfMonth = viewModel.daysOfMonth(),
            selectedDays = selectedDays,
            hasClickableCells = hasClickableCells,
            onCellClick = onCellClick,
            startFromSunday = startFromSunday,
            uiState = viewModel.uiState,
            modifier = Modifier
                .wrapContentHeight()
                .padding(horizontal = 16.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
private fun CalendarHeader(
    uiState: CalendarUiState,
    canNavigateMonth: Boolean,
    onNextMonthClick: () -> Unit,
    onPreviousMonthClick: () -> Unit,
    onMonthSelected: (Int, Month) -> Unit,
    modifier: Modifier = Modifier
) {
    var showMonthPicker by remember { mutableStateOf(false) }
    var selectedMonth by remember { mutableStateOf(uiState.month) }
    var selectedYear by remember { mutableIntStateOf(uiState.year) }
//    var offset by remember { mutableFloatStateOf(0f) }
//
//    //  - only react when gesture released / not clicked anymore
//    //  - animate (e.g. with spring) smooth back to zero position
//    //  - generally go (smoothly) back to 0 when click released
//    if (offset <= -250) {
//        offset = 0f
//        onPreviousMonthClick()
//    }
//
//    if (250 <= offset) {
//        offset = 0f
//        onNextMonthClick()
//    }

    Box(modifier = modifier.fillMaxWidth()) {
        if (canNavigateMonth) {
            IconButton(
                onClick = onPreviousMonthClick,
                modifier = Modifier.align(Alignment.CenterStart),
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = stringResource(R.string.calendar_previous_button_description)
                )
            }
            IconButton(
                onClick = onNextMonthClick,
                modifier = Modifier.align(Alignment.CenterEnd)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = stringResource(R.string.calendar_next_button_description)
                )
            }
        }
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier
                .align(Alignment.Center)
                .clip(MaterialTheme.shapes.medium)
                .clickable { showMonthPicker = !showMonthPicker }
        ) {
            Text(
                text = "${stringResource(uiState.month.monthOfYearId)} ${uiState.year}",
                style = MaterialTheme.typography.headlineMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .padding(start = 16.dp)
//                .offset {
//                    IntOffset(
//                        x = offset.roundToInt(),
//                        y = 0
//                    )
//                }
//                .draggable(
//                    state = rememberDraggableState {
//                        offset += it
//                    },
//                    orientation = Orientation.Horizontal
//                )
            )
            Icon(
                imageVector = Icons.Default.ArrowDropDown,
                contentDescription = stringResource(R.string.drop_down_button_description),
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            )
        }

        if (showMonthPicker) {
            ConfirmDismissDialog(
                onConfirm = {
                    showMonthPicker = false
                    onMonthSelected(selectedYear, selectedMonth)
                },
                onDismiss = { showMonthPicker = false }
            ) {
                MonthPicker(
                    onMonthChange = { selectedMonth = it },
                    onYearChange = { selectedYear = it }
                )
            }
        }
    }
}

@Composable
private fun MonthPicker(
    onMonthChange: (Month) -> Unit,
    onYearChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    var monthText by remember { mutableStateOf("") }
    var monthTextValid by remember { mutableStateOf(false) }
    var yearText by remember { mutableStateOf("") }
    var yearTextValid by remember { mutableStateOf(false) }

    Column {
        Text(
            text = stringResource(R.string.jump_to_month_dialog_headline),
            style = MaterialTheme.typography.headlineSmall
        )
        Row(
            modifier.padding(8.dp)
        ) {
            OutlinedTextField(
                value = monthText,
                label = { Text(stringResource(R.string.month)) },
                onValueChange = {
                    monthText = it

                    monthTextValid = try {
                        onMonthChange(Month(it.toInt()))
                        true
                    } catch (_: Exception) {
                        false
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next
                ),
                singleLine = true,
                isError = !monthTextValid,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp)
            )
            OutlinedTextField(
                value = yearText,
                label = { Text(stringResource(R.string.year)) },
                onValueChange = {
                    yearText = it

                    yearTextValid = try {
                        onYearChange(it.toInt())
                        true
                    } catch (_: Exception) {
                        false
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.NumberPassword,
                    imeAction = ImeAction.Done
                ),
                singleLine = true,
                readOnly = false,
                isError = !yearTextValid,
                modifier = Modifier
                    .weight(0.5f)
                    .padding(8.dp)
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    firstWeekDayOfMonth: DayOfWeek,
    daysOfMonth: Int,
    selectedDays: List<LocalDate>,
    startFromSunday: Boolean,
    hasClickableCells: Boolean,
    onCellClick: (LocalDate) -> Unit,
    uiState: CalendarUiState,
    modifier: Modifier = Modifier
) {
    val weekdays = getWeekDays(startFromSunday)
    val days: List<Pair<LocalDate, Boolean>> = (1 .. daysOfMonth)
        .map {
            LocalDate(
                uiState.year,
                uiState.month,
                it
            )
        }
        .map { it to selectedDays.contains(it) }

    CalendarCustomLayout(modifier = modifier) {
        weekdays.forEach {
            CalendarWeekHeader(weekday = it)
        }
        // Adds Spacers to align the first day of the month to the correct weekday
        repeat(
            if (!startFromSunday)
                firstWeekDayOfMonth.isoDayNumber - 1
            else if (firstWeekDayOfMonth.isoDayNumber == 7)
                0
            else
                firstWeekDayOfMonth.isoDayNumber
        ) {
            Spacer(modifier = Modifier)
        }
        days.forEach {
            CalendarDayCell(
                date = it.first,
                signal = it.second,
                isClickable = hasClickableCells,
                onClick = { onCellClick(it.first) }
            )
        }
    }
}

@Composable
private fun CalendarCustomLayout(
    modifier: Modifier = Modifier,
    horizontalGapDp: Dp = 2.dp,
    verticalGapDp: Dp = 2.dp,
    content: @Composable () -> Unit
) {
    val horizontalGap = with(LocalDensity.current) {
        horizontalGapDp.roundToPx()
    }
    val verticalGap = with(LocalDensity.current) {
        verticalGapDp.roundToPx()
    }
    Layout(
        content = content,
        modifier = modifier,
    ) { measurables, constraints ->
        val totalWidthWithoutGap = constraints.maxWidth - (horizontalGap * 6)
        val singleWidth = totalWidthWithoutGap / 7

        val xPos: MutableList<Int> = mutableListOf()
        val yPos: MutableList<Int> = mutableListOf()
        var currentX = 0
        var currentY = 0
        measurables.forEach { _ ->
            xPos.add(currentX)
            yPos.add(currentY)
            if (currentX + singleWidth + horizontalGap > totalWidthWithoutGap) {
                currentX = 0
                currentY += singleWidth + verticalGap
            } else {
                currentX += singleWidth + horizontalGap
            }
        }

        val placeables: List<Placeable> = measurables.map { measurable ->
            measurable.measure(constraints.copy(maxHeight = singleWidth, maxWidth = singleWidth))
        }

        layout(
            width = constraints.maxWidth,
            height = currentY + singleWidth + verticalGap,
        ) {
            placeables.forEachIndexed { index, placeable ->
                placeable.placeRelative(
                    x = xPos[index],
                    y = yPos[index],
                )
            }
        }
    }
}

@Composable
private fun CalendarWeekHeader(weekday: DayOfWeek, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(weekday.abbreviationId),
            color = MaterialTheme.colorScheme.onPrimaryContainer,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun CalendarDayCell(
    date: LocalDate,
    signal: Boolean,
    isClickable: Boolean,
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
    val text = date.dayOfMonth.toString()
    val mod =
        if (isClickable) modifier.clickable(onClick = onClick)
        else modifier
    Box(
        modifier = mod
            .aspectRatio(1f)
            .fillMaxSize()
            .padding(2.dp)
            .background(
                shape = RoundedCornerShape(CornerSize(8.dp)),
                color = MaterialTheme.colorScheme.secondaryContainer,
            )
            .clip(RoundedCornerShape(CornerSize(8.dp)))
    ) {
        if (signal) {
            Box(
                modifier = Modifier
                    .aspectRatio(1f)
                    .fillMaxSize()
                    .padding(8.dp)
                    .background(
                        shape = CircleShape,
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                    )
            )
        }
        Text(
            text = text,
            color = MaterialTheme.colorScheme.onSecondaryContainer,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun getWeekDays(startFromSunday: Boolean): List<DayOfWeek> =
    if (startFromSunday)
        listOf(DayOfWeek(7), DayOfWeek(1), DayOfWeek(2), DayOfWeek(3), DayOfWeek(4), DayOfWeek(5), DayOfWeek(6))
    else
        listOf(DayOfWeek(1), DayOfWeek(2), DayOfWeek(3), DayOfWeek(4), DayOfWeek(5), DayOfWeek(6), DayOfWeek(7))

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    RefuelTrackerTheme {
        CalendarView(
            firstDisplayMonth = Month(2),
            firstDisplayYear = 1602,
            selectedDays = listOf(
                LocalDate(1602, 2, 1),
                LocalDate(1602, 2, 7),
                LocalDate(1602, 2, 15),
                LocalDate(1602, 2, 25),
                LocalDate(1602, 2, 12),
                LocalDate(1602, 2, 19),
                LocalDate(1602, 2, 8)
            ),
            canNavigateMonth = true
        )
    }
}