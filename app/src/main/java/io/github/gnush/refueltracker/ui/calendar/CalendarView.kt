package io.github.gnush.refueltracker.ui.calendar

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.layout.Placeable
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.datetime.DayOfWeek
import kotlinx.datetime.LocalDate
import kotlinx.datetime.Month
import kotlinx.datetime.isoDayNumber
import io.github.gnush.refueltracker.R
import io.github.gnush.refueltracker.ui.RefuelTrackerViewModelProvider
import io.github.gnush.refueltracker.ui.extensions.abbreviationId
import io.github.gnush.refueltracker.ui.extensions.monthOfYearId
import io.github.gnush.refueltracker.ui.theme.RefuelTrackerTheme
import kotlin.math.roundToInt

// TODO:
//  - add year navigation
//  - make Month and Year in calendar header clickable with a drop down list (or else)
//    month select: navigate directly to the selected month of the same year
//    year select: navigate directly to the selected year selecting the same month
//    alternatively: just one click to open a date picker dialog
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
    modifier: Modifier = Modifier
) {
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
        val monthNameId = uiState.month.monthOfYearId
        Text(
            text = "${stringResource(monthNameId)} ${uiState.year}",
            style = typography.headlineMedium,
            color = colorScheme.onPrimaryContainer,
            modifier = Modifier
                .align(Alignment.Center)
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