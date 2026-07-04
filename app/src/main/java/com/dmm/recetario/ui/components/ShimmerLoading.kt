package com.dmm.recetario.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.Shape

@Composable
fun Modifier.shimmerLoading (
    isLoading: Boolean,
    shape: Shape = RectangleShape,
    shimmerColors: List<Color> = listOf (
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f),
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.2f),
        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
    )
): Modifier = composed {
    if (!isLoading) return@composed this

    val transition = rememberInfiniteTransition(label = "Shimmer")
    val translateAnim = transition.animateFloat (
        initialValue = 0f,
        targetValue = 1000f,
        animationSpec = infiniteRepeatable (
            animation = tween(durationMillis = 1200, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "ShimmerTranslation"
    )

    val brush = Brush.linearGradient (
        colors = shimmerColors,
        start = Offset.Zero,
        end = Offset(x = translateAnim.value, y = translateAnim.value)
    )

    return@composed this.background(brush, shape)
}