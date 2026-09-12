package com.example.bleex.ui.components

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.RoundRect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathMeasure
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun ScanBorder() {

    val infiniteTransition = rememberInfiniteTransition(
        label = "scanning border"
    )

    val progress by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 4000,
                easing = LinearEasing
            ),
            repeatMode = RepeatMode.Restart
        ),
        label = "border progress"
    )

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val path = Path().apply {
            addRoundRect(
                RoundRect(
                    top = 8.dp.toPx(),
                    left = 8.dp.toPx(),
                    right = size.width - 8.dp.toPx(),
                    bottom = size.height - 8.dp.toPx(),
                    cornerRadius = CornerRadius(
                        x = 20.dp.toPx(),
                        y = 20.dp.toPx()
                    )
                )
            )
        }

        val pathMeasure = PathMeasure()
        pathMeasure.setPath(path, false)
        val pathLength = pathMeasure.length
        val lightLength = 800.dp.toPx()
        val start = progress * pathLength
        val end = start + lightLength
        val segment = Path()

        if (end <= pathLength) {
            // Normal case
            val segment = Path()
            pathMeasure.getSegment(
                startDistance = start,
                stopDistance = end,
                destination = segment,
                startWithMoveTo = true
            )
            drawPath(
                path = segment,
                Color(0xFF0D2A4A),
                style = Stroke(width = 12.dp.toPx(),
                    cap = StrokeCap.Round)
            )


        } else {
            // The light reaches the end of the border,
            // so continue from the beginning.

            val firstSegment = Path()

            pathMeasure.getSegment(
                startDistance = start,
                stopDistance = pathLength,
                destination = firstSegment,
                startWithMoveTo = true
            )

            drawPath(
                path = firstSegment,
                color = Color(0xFF0D2A4A),
                style = Stroke(
                    width = 12.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )

            val secondSegment = Path()

            pathMeasure.getSegment(
                startDistance = 0f,
                stopDistance = end - pathLength,
                destination = secondSegment,
                startWithMoveTo = true
            )

            drawPath(
                path = secondSegment,
                color = Color(0xFF0D2A4A),
                style = Stroke(
                    width = 12.dp.toPx(),
                    cap = StrokeCap.Round
                )
            )
        }
    }
}