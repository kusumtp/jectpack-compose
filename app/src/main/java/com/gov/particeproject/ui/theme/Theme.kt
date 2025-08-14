package com.gov.particeproject.ui.theme

import android.app.Activity
import android.os.Build
import android.util.Log
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Shapes
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB6B9),       // Blush Pink (main accent)
    secondary = Color(0xFFFFD6A5),     // Peach Cream (soft contrast)
    tertiary = Color(0xFFFFC0CB),      // Classic Pink (highlight)

    background = Color(0xFF1C1B1F),    // Deep charcoal
    surface = Color(0xFF2C2C2E),       // Slightly lighter surface
    onPrimary = Color.Black,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFFFCE4EC),  // Light rose for text
    onSurface = Color(0xFFF8BBD0)      // Soft pink for surface text
)


private val LightColorScheme = lightColorScheme(
    primary = Color(0xFFFFB6B9),       // Blush Pink (main accent)
    secondary = Color(0xFFFFD6A5),     // Peach Cream (soft contrast)
    tertiary = Color(0xFFFFC0CB),      // Classic Pink (highlight)

    background = Color(0xFFFFF1F3),    // Very light peach-pink tint
    surface = Color(0xFFFFFFFF),       // Clean white for cards
    onPrimary = Color.White,
    onSecondary = Color.Black,
    onTertiary = Color.Black,
    onBackground = Color(0xFF4A4A4A),  // Neutral dark for text
    onSurface = Color(0xFF4A4A4A)
)



@Composable
fun ParticeProjectTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false, // ❌ Disable dynamic color
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    Log.d("ThemeCheck", "Using Rose Luxe Theme")

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}

