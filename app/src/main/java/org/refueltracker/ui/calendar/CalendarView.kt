package org.refueltracker.ui.calendar

import androidx.annotation.StringRes
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
import kotlinx.datetime.LocalDate
import kotlinx.datetime.isoDayNumber
import kotlinx.datetime.number
import org.refueltracker.R
import org.refueltracker.ui.theme.RefuelTrackerTheme

@Preview(showBackground = true)
@Composable
private fun CalendarPreview() {
    RefuelTrackerTheme {
        CalendarView(
            startDate = LocalDate(2001, 12, 17),
            date = listOf(
                Pair(LocalDate(2001, 10, 1), true),
                Pair(LocalDate(2001, 10, 2), true),
                Pair(LocalDate(2001, 10, 3), true),
                Pair(LocalDate(2001, 10, 4), true),
                Pair(LocalDate(2001, 10, 5), true),
                Pair(LocalDate(2001, 10, 6), true),
                Pair(LocalDate(2001, 10, 7), true),
                Pair(LocalDate(2001, 10, 8), true),
                Pair(LocalDate(2001, 10, 9), true),
                Pair(LocalDate(2001, 10, 10), true),
                Pair(LocalDate(2001, 10, 11), true),
                Pair(LocalDate(2001, 10, 12), true),
                Pair(LocalDate(2001, 10, 13), true),
                Pair(LocalDate(2001, 10, 14), true),
                Pair(LocalDate(2001, 10, 15), true),
                Pair(LocalDate(2001, 10, 16), true),
                Pair(LocalDate(2001, 10, 17), true),
                Pair(LocalDate(2001, 10, 18), true),
                Pair(LocalDate(2001, 10, 19), true),
                Pair(LocalDate(2001, 10, 20), true),
                Pair(LocalDate(2001, 10, 21), true),
                Pair(LocalDate(2001, 10, 22), true),
                Pair(LocalDate(2001, 10, 23), true),
                Pair(LocalDate(2001, 10, 24), true),
                Pair(LocalDate(2001, 10, 25), true),
                Pair(LocalDate(2001, 10, 26), true),
                Pair(LocalDate(2001, 10, 27), true),
                Pair(LocalDate(2001, 10, 28), true),
                Pair(LocalDate(2001, 10, 29), true),
                Pair(LocalDate(2001, 10, 30), true),
                Pair(LocalDate(2001, 10, 31), true)
            ),
            displayNext = true,
            displayPrev = true,
            onClickNext = {},
            onClickPrev = {},
            onClick = {}
        )
    }
}

@Composable
fun CalendarView(
    startDate: LocalDate,
    date: List<Pair<LocalDate, Boolean>>?,
    displayNext: Boolean,
    displayPrev: Boolean,
    onClickNext: () -> Unit,
    onClickPrev: () -> Unit,
    onClick: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    startFromSunday: Boolean = false
) {
    Column(modifier = modifier) {
        Box(modifier = Modifier.fillMaxWidth()) {
            if (displayPrev)
                IconButton(
                    onClick = onClickPrev,
                    modifier = Modifier.align(Alignment.CenterStart),
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "navigate to previous month" // TODO: string res
                    )
                }
            if (displayNext)
                IconButton(
                    onClick = onClickNext,
                    modifier = Modifier.align(Alignment.CenterEnd)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                        contentDescription = "navigate to next month" // TODO: string res
                    )
                }
            Row(modifier = Modifier.align(Alignment.Center)) {
                Text(
                    text = "${stringResource(startDate.month.number.monthOfYearId())} ${startDate.year}",
                    style = typography.headlineMedium,
                    color = colorScheme.onPrimaryContainer,
                )
            }
        }
        Spacer(modifier = Modifier.size(16.dp))
        if (!date.isNullOrEmpty()) {
            CalendarGrid(
                date = date,
                onClick = onClick,
                startFromSunday = startFromSunday,
                modifier = Modifier
                    .wrapContentHeight()
                    .padding(horizontal = 16.dp)
                    .align(Alignment.CenterHorizontally)
            )
        }
    }
}

@Composable
private fun CalendarGrid(
    date: List<Pair<LocalDate, Boolean>>,
    onClick: (LocalDate) -> Unit,
    startFromSunday: Boolean,
    modifier: Modifier = Modifier
) {
    val weekdayFirstDay = date.first().first.dayOfWeek
    val weekdays = getWeekDays(startFromSunday)
    CalendarCustomLayout(modifier = modifier) {
        weekdays.forEach {
            WeekdayCell(weekday = it)
        }
        // TODO: doesnt seem to work, debug
        // Adds Spacers to align the first day of the month to the correct weekday
        repeat(if (!startFromSunday) weekdayFirstDay.isoDayNumber - 2 else weekdayFirstDay.isoDayNumber - 1) {
            Spacer(modifier = Modifier)
        }
        date.forEach {
            CalendarCell(date = it.first, signal = it.second, onClick = { onClick(it.first) })
        }
    }
}

@Composable
private fun CalendarCustomLayout(
    modifier: Modifier = Modifier,
    horizontalGapDp: Dp = 2.dp,
    verticalGapDp: Dp = 2.dp,
    content: @Composable () -> Unit,
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
private fun WeekdayCell(weekday: Int, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
    ) {
        Text(
            text = stringResource(weekday.dayOfWeekAbbreviationId()),
            color = colorScheme.onPrimaryContainer,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}


@Composable
private fun CalendarCell(
    date: LocalDate,
    signal: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    val text = date.dayOfMonth.toString()
    date.dayOfWeek
    Box(
        modifier = modifier
            .aspectRatio(1f)
            .fillMaxSize()
            .padding(2.dp)
            .background(
                shape = RoundedCornerShape(CornerSize(8.dp)),
                color = colorScheme.secondaryContainer,
            )
            .clip(RoundedCornerShape(CornerSize(8.dp)))
            .clickable(onClick = onClick)
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

private fun getWeekDays(startFromSunday: Boolean): List<Int> =
    if (startFromSunday)
        listOf(7, 1, 2, 3, 4, 5, 6)
    else
        listOf(1, 2, 3, 4, 5, 6, 7)

@StringRes
private fun Int.dayOfWeekAbbreviationId(): Int = when(this) {
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
private fun Int.monthOfYearId(): Int = when(this) {
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