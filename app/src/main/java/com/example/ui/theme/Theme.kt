package com.example.ui.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val DarkColorScheme = darkColorScheme(
    primary = AssamTeaLeafGreen,
    onPrimary = Color.White,
    primaryContainer = AssamForestGreenDark,
    onPrimaryContainer = AssamTeaLight,
    secondary = AssamGoldenWarm,
    onSecondary = Color.Black,
    background = Color(0xFF121A15),
    surface = Color(0xFF1B241E),
    onBackground = Color(0xFFE4E9E5),
    onSurface = Color(0xFFE4E9E5),
    surfaceVariant = Color(0xFF28362D),
    onSurfaceVariant = Color(0xFFC0CDC3)
)

private val LightColorScheme = lightColorScheme(
    primary = AssamForestGreen,
    onPrimary = Color.White,
    primaryContainer = AssamTeaLight,
    onPrimaryContainer = AssamForestGreenDark,
    secondary = AssamTeaLeafGreen,
    onSecondary = Color.White,
    tertiary = AssamGoldenPaddy,
    onTertiary = Color.White,
    background = AssamCreamBackground,
    surface = AssamCardSurface,
    onBackground = AssamTextPrimary,
    onSurface = AssamTextPrimary,
    surfaceVariant = AssamSurfaceVariant,
    onSurfaceVariant = AssamTextSecondary,
    outline = AssamBorder
)

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // Keep authentic Assamese brand identity
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
