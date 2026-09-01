package com.nastech.nia.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val AmoledDarkColors = darkColorScheme(
    primary = NeonCyan,
    onPrimary = AmoledBlack,
    primaryContainer = CyanContainer,
    onPrimaryContainer = NeonCyan,
    secondary = NeonPurple,
    onSecondary = Color.White,
    secondaryContainer = PurpleContainer,
    onSecondaryContainer = NeonPurple,
    tertiary = NeonBlue,
    background = AmoledBlack,
    onBackground = Color.White,
    surface = SurfaceDark,
    onSurface = Color.White,
    surfaceVariant = SurfaceElevated,
    onSurfaceVariant = TextSecondary,
    surfaceContainer = SurfaceDark,
    surfaceContainerHigh = SurfaceElevated,
    surfaceContainerHighest = SurfaceHighest,
    outline = Outline,
    outlineVariant = Outline,
    error = NeonRed,
    onError = Color.White,
    errorContainer = RedContainer,
    onErrorContainer = NeonRed
)

@Composable
fun CyberGuardTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = AmoledDarkColors,
        typography = AppTypography,
        content = content
    )
}