package com.example.spendwise.ui.theme

import android.app.Activity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val LightColorScheme = lightColorScheme(
    primary = DarkPine,
    onPrimary = CardLight,
    primaryContainer = PaleGreen,
    onPrimaryContainer = DarkPine,
    secondary = Terracotta,
    onSecondary = CardLight,
    background = CreamBg,
    onBackground = TextPrimary,
    surface = CardLight,
    onSurface = TextPrimary,
    surfaceVariant = PillBg,
    onSurfaceVariant = TextSecondary,
    outline = CardBorder
)

private val DarkColorScheme = darkColorScheme(
    primary = MintGreen,
    onPrimary = DarkBg,
    primaryContainer = DarkPineElevated,
    onPrimaryContainer = MintGreen,
    secondary = Terracotta,
    onSecondary = CardLight,
    background = DarkBg,
    onBackground = DarkTextPrimary,
    surface = DarkCard,
    onSurface = DarkTextPrimary,
    surfaceVariant = DarkPillBg,
    onSurfaceVariant = DarkTextSecondary,
    outline = DarkCardBorder
)

@Composable
fun SpendWiseTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = colorScheme.background.toArgb()
            window.navigationBarColor = colorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).apply {
                isAppearanceLightStatusBars = !darkTheme
                isAppearanceLightNavigationBars = !darkTheme
            }
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = SpendWiseTypography,
        content = content
    )
}
