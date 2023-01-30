package com.devdaniel.marvelapp.ui.theme

import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = black_1C1B1F,
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
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colors = DarkColorPalette,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}
