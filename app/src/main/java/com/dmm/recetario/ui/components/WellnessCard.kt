package com.dmm.recetario.ui.components

import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage

@Composable
fun WellnessCard (
    modifier: Modifier = Modifier,
    title: String,
    titleColor: Color = MaterialTheme.colorScheme.onSurface,
    description: String? = null,
    image: String? = null,
    imageDescription: String? = null,
    backgroundColor: Color = MaterialTheme.colorScheme.surface,
    onClick: () -> Unit = {}
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()

    val scale by animateFloatAsState (
        targetValue = if (isPressed) 0.95f else 1f,
        label = "scaleAnim"
    )

    Card (
        modifier = modifier
            .graphicsLayer(scaleX = scale, scaleY = scale)
            .fillMaxWidth()
            .clickable (
                interactionSource = interactionSource,
                onClick = onClick
            )
            .animateContentSize(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp),
        colors = CardDefaults.cardColors(containerColor = backgroundColor)
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (!image.isNullOrEmpty()) {
                Box {
                    AsyncImage (
                        model = image,
                        contentScale = ContentScale.Crop,
                        contentDescription = imageDescription,
                        modifier = Modifier
                            .size(80.dp)
                            .clip(RoundedCornerShape(8.dp))
                    )

                    Box (
                        modifier = Modifier
                            .matchParentSize()
                            .background (
                                Brush.verticalGradient (
                                    colors = listOf (
                                        Color.Transparent,
                                        backgroundColor.copy(alpha = 0.7f)
                                    )
                                )
                            )
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))
            }

            Text (
                text = title,
                fontSize = 18.sp,
                color = titleColor,
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )

            if (!description.isNullOrEmpty()) {
                Spacer(modifier = Modifier.height(8.dp))
                Text (
                    fontSize = 16.sp,
                    text = description,
                    color = titleColor,
                    textAlign = TextAlign.Justify
                )
            }
        }
    }
}

@Composable
fun WellnessCardSkeleton (
    modifier: Modifier = Modifier,
    isLoading: Boolean = true,
    showImage: Boolean = true,
    showDescription: Boolean = true
) {
    Card (
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 10.dp)
    ) {
        Column (
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            if (showImage) {
                Box (
                    modifier = Modifier
                        .size(80.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .shimmerLoading(isLoading)
                )

                Spacer(modifier = Modifier.height(12.dp))
            }

            Box (
                modifier = Modifier
                    .fillMaxWidth(0.7f)
                    .height(24.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .shimmerLoading(isLoading)
            )

            if (showDescription) {
                Spacer(modifier = Modifier.height(12.dp))

                repeat(2) { index ->
                    Box (
                        modifier = Modifier
                            .fillMaxWidth (
                                when (index) {
                                    1 -> 0.75f
                                    else -> 1f
                                }
                            )
                            .height(16.dp)
                            .clip(RoundedCornerShape(6.dp))
                            .shimmerLoading(isLoading)
                    )

                    Spacer(modifier = Modifier.height(6.dp))
                }
            }
        }
    }
}