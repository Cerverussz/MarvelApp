package com.devdaniel.marvelapp.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val LightColorPalette = lightColors(
    primary = black_1C1B1F,
    primaryVariant = black_1C1B1F,
    secondary = purple_625B71,
    secondaryVariant = rose_E8DEF8,
    background = black_1C1B1F,
    surface = rose_FFFBFE,
    error = red_B3261E,
    onSecondary = white,
    onError = rose_F9DEDC

)

private val DarkColorPalette = darkColors(
    primary = white,
    primaryVariant = white,
    secondary = rose_CCC2DC,
    secondaryVariant = purple_4A4458,
    background = black_1C1B1F,
    surface = black_1C1B1F,
    error = red_601410,
    onSecondary = purple_332D41,
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
