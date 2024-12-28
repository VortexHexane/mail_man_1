package com.vortex.mail_man_1.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = BlueGray,
    onPrimary = Black,
    primaryContainer = DarkBlue,
    onPrimaryContainer = LightBlueGray,
    
    secondary = LightBlueGray,
    onSecondary = Black,
    secondaryContainer = DarkBlue,
    onSecondaryContainer = LightBlueGray,
    
    tertiary = White,
    onTertiary = Black,
    tertiaryContainer = DarkBlue,
    onTertiaryContainer = White,
    
    background = Black,
    onBackground = White,
    
    surface = Black,
    onSurface = White,
    surfaceVariant = DarkBlue,
    onSurfaceVariant = White,
    
    error = Color(0xFFFF5252),
    onError = Black,
    
    outline = BlueGray
)

// We'll keep the light scheme but make it similar to dark for consistency
private val LightColorScheme = DarkColorScheme

@Composable
fun Mail_man_1Theme(
    darkTheme: Boolean = true, // Force dark theme by default
    dynamicColor: Boolean = false, // Disable dynamic color to use our custom colors
    content: @Composable () -> Unit
) {
    val colorScheme = DarkColorScheme // Always use dark color scheme

    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}