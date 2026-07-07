package com.journal.app.ui.editor

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.automirrored.filled.FormatListBulleted
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.FormatBold
import androidx.compose.material.icons.filled.FormatItalic
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.journal.app.ui.theme.*
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

@Composable
fun EditorScreen(
    date: String,
    onBack: () -> Unit,
    viewModel: EditorViewModel = viewModel()
) {
    val textValue by viewModel.textValue.collectAsState()
    val isSaved by viewModel.isSaved.collectAsState()
    val isBold by viewModel.isBold.collectAsState()
    val isItalic by viewModel.isItalic.collectAsState()
    val focusRequester = remember { FocusRequester() }
    val scrollState = rememberScrollState()

    LaunchedEffect(date) {
        viewModel.loadEntry(date)
    }

    val parsedDate = remember(date) {
        try {
            LocalDate.parse(date)
        } catch (e: Exception) {
            LocalDate.now()
        }
    }

    val isToday = parsedDate == LocalDate.now()
    val dayLabel = if (isToday) "Today" else parsedDate.dayOfWeek.getDisplayName(
        java.time.format.TextStyle.FULL, Locale.getDefault()
    )
    val dateLabel = parsedDate.format(DateTimeFormatter.ofPattern("d MMMM yyyy"))

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
        Column(modifier = Modifier.fillMaxSize()) {

            // Top bar
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .statusBarsPadding()
                    .padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Glass back button
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(GlassSurface)
                        .border(0.5.dp, GlassBorder, CircleShape)
                        .clickable {
                            viewModel.save()
                            onBack()
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                        contentDescription = "Back",
                        tint = SoftText,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                // Save status pill - glass style
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GlassSurface)
                        .border(0.5.dp, GlassBorder, RoundedCornerShape(20.dp))
                        .clickable { viewModel.save() }
                        .padding(horizontal = 14.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(6.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSaved) AccentRed.copy(alpha = 0.8f) else MutedText
                            )
                    )
                    Spacer(modifier = Modifier.width(7.dp))
                    Text(
                        text = if (isSaved) "Saved" else "Saving",
                        style = MaterialTheme.typography.labelSmall,
                        color = if (isSaved) SoftText else MutedText
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Save now",
                        tint = if (isSaved) AccentRed.copy(alpha = 0.7f) else MutedText,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Date header
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp)
            ) {
                Text(
                    text = dayLabel.uppercase(),
                    style = MaterialTheme.typography.labelSmall,
                    color = AccentRedBright,
                    letterSpacing = 4.sp
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = dateLabel,
                    style = MaterialTheme.typography.headlineMedium,
                    color = PrimaryText,
                    fontWeight = FontWeight.Light
                )
                Spacer(modifier = Modifier.height(16.dp))
                HorizontalDivider(color = GlassBorder, thickness = 0.5.dp)
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Text editor
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(horizontal = 24.dp)
            ) {
                if (textValue.text.isEmpty()) {
                    Text(
                        text = "Start writing...",
                        style = TextStyle(
                            fontSize = 17.sp,
                            lineHeight = 28.sp,
                            color = SubtleGray
                        )
                    )
                }

                BasicTextField(
                    value = textValue,
                    onValueChange = { viewModel.onTextChange(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .focusRequester(focusRequester),
                    textStyle = TextStyle(
                        fontSize = 17.sp,
                        lineHeight = 28.sp,
                        color = PrimaryText,
                        fontWeight = if (isBold) FontWeight.Bold else FontWeight.Normal,
                        fontStyle = if (isItalic) FontStyle.Italic else FontStyle.Normal
                    ),
                    cursorBrush = SolidColor(AccentRed)
                )
            }

            // Glass formatting toolbar
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 10.dp)
                    .navigationBarsPadding()
                    .imePadding()
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(20.dp))
                        .background(GlassSurfaceStrong)
                        .border(0.5.dp, GlassBorder, RoundedCornerShape(20.dp))
                        .padding(horizontal = 8.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    FormatButton(
                        icon = { tint ->
                            Icon(Icons.Default.FormatBold, "Bold", tint = tint, modifier = Modifier.size(20.dp))
                        },
                        isActive = isBold,
                        onClick = { viewModel.toggleBold() }
                    )
                    FormatButton(
                        icon = { tint ->
                            Icon(Icons.Default.FormatItalic, "Italic", tint = tint, modifier = Modifier.size(20.dp))
                        },
                        isActive = isItalic,
                        onClick = { viewModel.toggleItalic() }
                    )
                    FormatButton(
                        icon = { tint ->
                            Icon(Icons.AutoMirrored.Filled.FormatListBulleted, "Bullet", tint = tint, modifier = Modifier.size(20.dp))
                        },
                        isActive = false,
                        onClick = {
                            val current = textValue.text
                            val newText = if (current.isEmpty() || current.endsWith("\n")) {
                                current + "\u2022 "
                            } else {
                                current + "\n\u2022 "
                            }
                            viewModel.onTextChange(
                                textValue.copy(
                                    text = newText,
                                    selection = TextRange(newText.length)
                                )
                            )
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun FormatButton(
    icon: @Composable (Color) -> Unit,
    isActive: Boolean,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape(14.dp))
            .background(if (isActive) GlassRedTint else Color.Transparent)
            .clickable { onClick() },
        contentAlignment = Alignment.Center
    ) {
        icon(if (isActive) AccentRedBright else MutedText)
    }
}
