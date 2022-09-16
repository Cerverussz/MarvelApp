package com.devdaniel.marvelapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = white,
    primaryVariant = blue_21005D,
    secondary = purple_625B71,
    secondaryVariant = rose_E8DEF8,
    background = black_1C1B1F,
    surface = rose_FFFBFE,
    error = red_B3261E,
    onPrimary = blue_21005D,
    onSecondary = white,
    onBackground = black_1C1B1F,
    onSurface = purple_49454F,
    onError = rose_F9DEDC

)

private val DarkColorPalette = darkColors(
    primary = black_1C1B1F,
    primaryVariant = purple_4F378B,
    secondary = rose_CCC2DC,
    secondaryVariant = purple_4A4458,
    background = black_1C1B1F,
    surface = black_1C1B1F,
    error = red_601410,
    onPrimary = black_1C1B1F,
    onSecondary = purple_332D41,
    onBackground = black_1C1B1F,
    onSurface = black_1C1B1F,
    onError = red_8C1D18
)

@Composable
fun MarvelTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colors = if (darkTheme) DarkColorPalette else LightColorPalette

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
