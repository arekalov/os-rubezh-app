package com.arekalov.osrubezh.presentation.theme

import androidx.compose.runtime.Composable
import androidx.wear.compose.material.Colors
import androidx.wear.compose.material.MaterialTheme

private val DarkColorPalette = Colors(
    primary = Primary,
    primaryVariant = PrimaryVariant,
    secondary = Secondary,
    secondaryVariant = Secondary,
    background = Background,
    surface = Surface,
    error = Error,
    onPrimary = OnPrimary,
    onSecondary = OnSecondary,
    onBackground = OnBackground,
    onSurface = OnSurface,
    onSurfaceVariant = OnSurface.copy(alpha = 0.6f),
    onError = OnPrimary
)

@Composable
fun OsrubezhTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = WearTypography,
        content = content
    )
}
