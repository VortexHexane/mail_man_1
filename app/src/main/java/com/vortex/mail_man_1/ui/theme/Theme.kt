package com.vortex.mail_man_1.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView

private val DarkColorScheme = darkColorScheme(
    primary = EmeraldGreen,
    onPrimary = Black,
    primaryContainer = Charcoal,
    onPrimaryContainer = MintGreen,
    
    secondary = MintGreen,
    onSecondary = Black,
    secondaryContainer = Charcoal,
    onSecondaryContainer = MintGreen,
    
    tertiary = White,
    onTertiary = Black,
    tertiaryContainer = Charcoal,
    onTertiaryContainer = White,
    
    background = Black,
    onBackground = White,
    
    surface = Black,
    onSurface = White,
    surfaceVariant = Charcoal,
    onSurfaceVariant = White,
    
    error = Color(0xFFFF5252),
    onError = Black,
    
    outline = EmeraldGreen
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
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}