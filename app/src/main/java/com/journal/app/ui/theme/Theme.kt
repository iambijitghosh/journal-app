package com.journal.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

// Core palette
val Black = Color(0xFF0B0B0D)
val DarkGray = Color(0xFF141417)
val SurfaceGray = Color(0xFF1C1C21)
val CardGray = Color(0xFF232329)
val SubtleGray = Color(0xFF35353D)
val MutedText = Color(0xFF7A7A85)
val SoftText = Color(0xFFB8B8C2)
val PrimaryText = Color(0xFFEDEDF2)

// Accent - Muted crimson / wine red
val AccentRed = Color(0xFFB84A4A)
val AccentRedBright = Color(0xFFC95B58)
val AccentRedDim = Color(0xFF6E2E2E)
val AccentRedSurface = Color(0xFF1E1214)

// Glass surfaces - translucent layers
val GlassSurface = Color(0x14FFFFFF)      // 8% white
val GlassSurfaceStrong = Color(0x1FFFFFFF) // 12% white
val GlassBorder = Color(0x1AFFFFFF)        // 10% white
val GlassRedTint = Color(0x14B84A4A)       // 8% muted red

private val DarkColorScheme = darkColorScheme(
    primary = AccentRed,
    onPrimary = PrimaryText,
    primaryContainer = AccentRedSurface,
    onPrimaryContainer = AccentRed,
    secondary = SoftText,
    onSecondary = Black,
    background = Black,
    onBackground = PrimaryText,
    surface = DarkGray,
    onSurface = PrimaryText,
    surfaceVariant = SurfaceGray,
    onSurfaceVariant = SoftText,
    outline = SubtleGray,
    error = AccentRedBright,
)

@Composable
fun JournalTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = DarkColorScheme,
        typography = JournalTypography,
        content = content
    )
}
