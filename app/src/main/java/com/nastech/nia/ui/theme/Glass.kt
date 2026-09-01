package com.nastech.nia.ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/** Shared glass-surface constants for the glass-on-dark design system. */
object GlassTheme {
    const val BorderAlpha = 0.12f
    const val BaseAlpha = 0.55f
    val Border = Color.White.copy(alpha = BorderAlpha)
    val Highlight = Color.White.copy(alpha = 0.06f)
    val Base = Color.White.copy(alpha = 0.03f)
    val Shape = RoundedCornerShape(20.dp)
}

/**
 * Apply the recessed glass surface: translucent fill + hairline border +
 * a soft top highlight for edge light.
 */
fun Modifier.glass(
    shape: RoundedCornerShape = GlassTheme.Shape,
    borderAlpha: Float = GlassTheme.BorderAlpha,
    baseAlpha: Float = GlassTheme.BaseAlpha,
    elevation: Dp = 4.dp,
): Modifier = this
    .shadow(elevation = elevation, shape = shape)
    .clip(shape)
    .background(color = Color.White.copy(alpha = baseAlpha))
    .border(
        width = 1.dp,
        color = Color.White.copy(alpha = borderAlpha),
        shape = shape,
    )

/**
 * Ambient gradient blobs (cyan -> purple) placed behind content so glass
 * panels "catch" color on the pure-black AMOLED base.
 */
@Composable
fun AmbientMesh(
    modifier: Modifier = Modifier,
) {
    Box(modifier = modifier.fillMaxSize()) {
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = 0.16f),
                            Color.Transparent,
                        ),
                        radius = 780f,
                    )
                ),
        )
        Box(
            Modifier
                .fillMaxSize()
                .background(
                    Brush.radialGradient(
                        colors = listOf(
                            NeonPurple.copy(alpha = 0.12f),
                            Color.Transparent,
                        ),
                        radius = 560f,
                    )
                ),
        )
    }
}