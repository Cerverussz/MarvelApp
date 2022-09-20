package com.devdaniel.marvelapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.drawscope.scale
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import com.devdaniel.marvelapp.ui.theme.red_8C1D18

class MarvelSurfaceWitOutCornersShape(private val cornerRadius: Float) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = drawTicketPath(size = size, cornerRadius = cornerRadius)
        )
    }
}

fun drawTicketPath(size: Size, cornerRadius: Float): Path {
    return Path().apply {
        reset()
        // Top left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = -cornerRadius,
                right = cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 90.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width - cornerRadius, y = 0f)
        // Top right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = -cornerRadius,
                right = size.width + cornerRadius,
                bottom = cornerRadius
            ),
            startAngleDegrees = 180.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = size.width, y = size.height - cornerRadius)
        // Bottom right arc
        arcTo(
            rect = Rect(
                left = size.width - cornerRadius,
                top = size.height - cornerRadius,
                right = size.width + cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 270.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = cornerRadius, y = size.height)
        // Bottom left arc
        arcTo(
            rect = Rect(
                left = -cornerRadius,
                top = size.height - cornerRadius,
                right = cornerRadius,
                bottom = size.height + cornerRadius
            ),
            startAngleDegrees = 0.0f,
            sweepAngleDegrees = -90.0f,
            forceMoveTo = false
        )
        lineTo(x = 0f, y = cornerRadius)
        close()
    }
}

fun Modifier.backgroundWithOutCorners(
    backgroundColor: Color,
    verticalPadding: Dp,
    horizontalPadding: Dp
): Modifier {
    return this.wrapContentSize()
        .graphicsLayer {
            shadowElevation = 8.dp.toPx()
            shape = MarvelSurfaceWitOutCornersShape(24.dp.toPx())
            clip = true
        }
        .background(color = backgroundColor)
        .drawBehind {
            scale(scale = 0.9f) {
                drawPath(
                    path = drawTicketPath(
                        size = size,
                        cornerRadius = 24.dp.toPx()
                    ),
                    color = red_8C1D18,
                    style = Stroke(
                        width = 2.dp.toPx(),
                        pathEffect = PathEffect.dashPathEffect(
                            floatArrayOf(
                                10f,
                                10f
                            )
                        )
                    )
                )
            }
        }
        .padding(horizontal = horizontalPadding, vertical = verticalPadding)
}
