package org.refueltracker.ui.calendar

import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme.colorScheme
import androidx.compose.material3.MaterialTheme.typography
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import org.refueltracker.R
import org.refueltracker.ui.RefuelTrackerViewModelProvider
import org.refueltracker.ui.theme.RefuelTrackerTheme

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
    hasClickableCells: Boolean = false,
    onCellClick: (LocalDate) -> Unit = {},
    viewModel: CalendarViewModel = viewModel( factory = RefuelTrackerViewModelProvider.Factory )
) {
    if (firstDisplayMonth != null)
        viewModel.updateDisplayMonth(firstDisplayMonth)

    if (firstDisplayYear != null)
        viewModel.updateDisplayYear(firstDisplayYear)

    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (canNavigateMonth) {
                IconButton(
                    onClick = {
                        if (viewModel.uiState.month.number == 1) {
                            viewModel.updateUiState(
                                viewModel.uiState.copy(
                                    month = Month(12),
                                    year = viewModel.uiState.year-1
                                )
                            )
                        } else {
                            viewModel.updateDisplayMonth(
                                viewModel.uiState.month.number-1
                            )
                        }
                        onPreviousMonthClick()
                    },
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = stringResource(R.string.calendar_previous_button_description)
                    )
                }
                IconButton(
                    onClick = {
                        if (viewModel.uiState.month.number == 12) {
                            viewModel.updateUiState(
                                viewModel.uiState.copy(
                                    month = Month(1),
                                    year = viewModel.uiState.year+1
                                )
                            )
                        } else {
                            viewModel.updateDisplayMonth(
                                viewModel.uiState.month.number+1
                            )
                        }
                        onNextMonthClick()
                    },
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = stringResource(R.string.calendar_next_button_description)
                    )
                }
            }
            val monthNameId = viewModel.uiState.month.monthOfYearId()
            Text(
                text = "${stringResource(monthNameId)} ${viewModel.uiState.year}",
                style = typography.headlineMedium,
                color = colorScheme.onPrimaryContainer,
                modifier = Modifier.align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.size(16.dp))
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
            text = stringResource(weekday.abbreviationId()),
            color = colorScheme.onPrimaryContainer,
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
                color = colorScheme.secondaryContainer,
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
                        color = colorScheme.tertiaryContainer.copy(alpha = 0.7f)
                    )
            )
        }
        Text(
            text = text,
            color = colorScheme.onSecondaryContainer,
            modifier = Modifier.align(Alignment.Center)
        )
    }
}

private fun getWeekDays(startFromSunday: Boolean): List<DayOfWeek> =
    if (startFromSunday)
        listOf(DayOfWeek(7), DayOfWeek(1), DayOfWeek(2), DayOfWeek(3), DayOfWeek(4), DayOfWeek(5), DayOfWeek(6))
    else
        listOf(DayOfWeek(1), DayOfWeek(2), DayOfWeek(3), DayOfWeek(4), DayOfWeek(5), DayOfWeek(6), DayOfWeek(7))

@StringRes
private fun DayOfWeek.abbreviationId(): Int = when(isoDayNumber) {
    1 -> R.string.day_of_week_abbreviation_1
    2 -> R.string.day_of_week_abbreviation_2
    3 -> R.string.day_of_week_abbreviation_3
    4 -> R.string.day_of_week_abbreviation_4
    5 -> R.string.day_of_week_abbreviation_5
    6 -> R.string.day_of_week_abbreviation_6
    7 -> R.string.day_of_week_abbreviation_7
    else -> R.string.day_of_week_abbreviation_1
}

@StringRes
private fun Month.monthOfYearId(): Int = when(number) {
    1 -> R.string.month_1
    2 -> R.string.month_2
    3 -> R.string.month_3
    4 -> R.string.month_4
    5 -> R.string.month_5
    6 -> R.string.month_6
    7 -> R.string.month_7
    8 -> R.string.month_8
    9 -> R.string.month_9
    10 -> R.string.month_10
    11 -> R.string.month_11
    12 -> R.string.month_12
    else -> R.string.month_1
}

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