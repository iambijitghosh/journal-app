package com.journal.app.ui.calendar

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.journal.app.ui.theme.*
import java.time.LocalDate
import java.time.YearMonth
import java.time.format.TextStyle as JavaTextStyle
import java.util.Locale

@Composable
fun CalendarScreen(
    onDateClick: (String) -> Unit,
    onTodayClick: () -> Unit,
    viewModel: CalendarViewModel = viewModel()
) {
    val currentMonth by viewModel.currentMonth.collectAsState()
    val entryDates by viewModel.entriesForMonth.collectAsState()
    val today = viewModel.today

    // Subtle ambient gradient background - barely-there red glow at top
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(
                        Color(0xFF16090B),
                        Black,
                        Black
                    )
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .statusBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = "Journal",
                style = MaterialTheme.typography.headlineLarge,
                color = PrimaryText,
                fontWeight = FontWeight.Thin,
                letterSpacing = 6.sp
            )

            Spacer(modifier = Modifier.height(28.dp))

            // Glass calendar panel
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(24.dp))
                    .background(GlassSurface)
                    .border(
                        width = 0.5.dp,
                        color = GlassBorder,
                        shape = RoundedCornerShape(24.dp)
                    )
                    .padding(16.dp)
            ) {
                MonthHeader(
                    yearMonth = currentMonth,
                    onPrevious = { viewModel.goToPreviousMonth() },
                    onNext = { viewModel.goToNextMonth() }
                )

                Spacer(modifier = Modifier.height(16.dp))

                DayOfWeekRow()

                Spacer(modifier = Modifier.height(8.dp))

                CalendarGrid(
                    yearMonth = currentMonth,
                    entryDates = entryDates,
                    today = today,
                    onDateClick = onDateClick
                )
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Legend
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                LegendItem(color = AccentRed, label = "Entry logged")
                Spacer(modifier = Modifier.width(20.dp))
                LegendItem(color = SubtleGray, label = "No entry")
            }

            Spacer(modifier = Modifier.weight(1f))

            // Glass CTA button with muted red tint
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(
                                AccentRed.copy(alpha = 0.85f),
                                AccentRedBright.copy(alpha = 0.75f)
                            )
                        )
                    )
                    .border(
                        width = 0.5.dp,
                        color = Color.White.copy(alpha = 0.15f),
                        shape = RoundedCornerShape(18.dp)
                    )
                    .clickable { onTodayClick() },
                contentAlignment = Alignment.Center
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Edit,
                        contentDescription = null,
                        tint = PrimaryText,
                        modifier = Modifier.size(17.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Write Today's Entry",
                        style = MaterialTheme.typography.titleMedium,
                        color = PrimaryText,
                        fontWeight = FontWeight.Medium,
                        letterSpacing = 0.5.sp
                    )
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
        }
    }
}

@Composable
fun MonthHeader(
    yearMonth: YearMonth,
    onPrevious: () -> Unit,
    onNext: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // Glass nav buttons
        GlassIconButton(
            icon = { 
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                    contentDescription = "Previous month",
                    tint = SoftText,
                    modifier = Modifier.size(22.dp)
                )
            },
            onClick = onPrevious
        )

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(
                text = yearMonth.month.getDisplayName(JavaTextStyle.FULL, Locale.getDefault()),
                style = MaterialTheme.typography.titleLarge,
                color = PrimaryText,
                fontWeight = FontWeight.Light,
                letterSpacing = 3.sp
            )
            Text(
                text = yearMonth.year.toString(),
                style = MaterialTheme.typography.labelSmall,
                color = MutedText,
                letterSpacing = 2.sp
            )
        }

        GlassIconButton(
            icon = {
                Icon(
                    Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = "Next month",
                    tint = SoftText,
                    modifier = Modifier.size(22.dp)
                )
            },
            onClick = onNext
        )
    }
}

@Composable
fun GlassIconButton(
    icon: @Composable () -> Unit,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(38.dp)
            .clip(CircleShape)
            .background(GlassSurface)
            .border(0.5.dp, GlassBorder, CircleShape)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        icon()
    }
}

@Composable
fun DayOfWeekRow() {
    val days = listOf("S", "M", "T", "W", "T", "F", "S")
    Row(modifier = Modifier.fillMaxWidth()) {
        days.forEach { day ->
            Text(
                text = day,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.labelSmall,
                color = MutedText,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
fun CalendarGrid(
    yearMonth: YearMonth,
    entryDates: Set<String>,
    today: LocalDate,
    onDateClick: (String) -> Unit
) {
    val firstDay = yearMonth.atDay(1)
    val startOffset = firstDay.dayOfWeek.value % 7
    val daysInMonth = yearMonth.lengthOfMonth()
    val totalCells = startOffset + daysInMonth
    val rows = (totalCells + 6) / 7

    Column(modifier = Modifier.fillMaxWidth()) {
        repeat(rows) { row ->
            Row(modifier = Modifier.fillMaxWidth()) {
                repeat(7) { col ->
                    val cellIndex = row * 7 + col
                    val dayNumber = cellIndex - startOffset + 1

                    if (dayNumber < 1 || dayNumber > daysInMonth) {
                        Spacer(modifier = Modifier.weight(1f).aspectRatio(1f))
                    } else {
                        val date = yearMonth.atDay(dayNumber)
                        val dateString = date.toString()

                        DayCell(
                            day = dayNumber,
                            isToday = date == today,
                            hasEntry = entryDates.contains(dateString),
                            isFuture = date.isAfter(today),
                            onClick = { onDateClick(dateString) },
                            modifier = Modifier.weight(1f)
                        )
                    }
                }
            }
            if (row < rows - 1) Spacer(modifier = Modifier.height(2.dp))
        }
    }
}

@Composable
fun DayCell(
    day: Int,
    isToday: Boolean,
    hasEntry: Boolean,
    isFuture: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val bgColor = when {
        isToday -> AccentRed.copy(alpha = 0.9f)
        hasEntry -> GlassRedTint
        else -> Color.Transparent
    }

    val textColor = when {
        isToday -> PrimaryText
        hasEntry -> AccentRedBright
        isFuture -> SubtleGray
        else -> MutedText
    }

    Box(
        modifier = modifier
            .aspectRatio(1f)
            .padding(3.dp)
            .clip(CircleShape)
            .background(bgColor)
            .then(
                if (hasEntry && !isToday) Modifier.border(0.5.dp, AccentRedDim, CircleShape)
                else Modifier
            )
            .clickable(enabled = !isFuture) { onClick() },
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = day.toString(),
            style = MaterialTheme.typography.bodyMedium,
            color = textColor,
            fontWeight = if (isToday) FontWeight.SemiBold else FontWeight.Normal
        )
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(7.dp)
                .clip(CircleShape)
                .background(color)
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(
            text = label,
            style = MaterialTheme.typography.labelSmall,
            color = MutedText
        )
    }
}
