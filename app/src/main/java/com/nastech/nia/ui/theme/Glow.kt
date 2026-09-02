package com.nastech.nia.ui.theme

import android.graphics.Bitmap
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * Paint a soft neon bloom beneath a rounded shape so it reads as a glow on the
 * black AMOLED base. Rendered into an offscreen bitmap with a blurred paint,
 * then composited behind the content.
 */
fun Modifier.glow(
    color: Color,
    blurDp: Dp = 8.dp,
    cornerDp: Dp = 20.dp,
): Modifier = this.drawWithContent {
    val blur = blurDp.toPx()
    val padding = blur * 2f
    val w = (size.width + padding * 2f).toInt()
    val h = (size.height + padding * 2f).toInt()
    if (w > 0 && h > 0) {
        val bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888)
        val canvas = android.graphics.Canvas(bmp)
        val paint = android.graphics.Paint().apply {
            isAntiAlias = true
            this.color = color.toArgb()
            setMaskFilter(
                android.graphics.BlurMaskFilter(blur, android.graphics.BlurMaskFilter.Blur.NORMAL)
            )
        }
        canvas.drawRoundRect(
            padding,
            padding,
            padding + size.width,
            padding + size.height,
            cornerDp.toPx(),
            cornerDp.toPx(),
            paint,
        )
        drawContext.canvas.drawImage(
            bmp.asImageBitmap(),
            topLeftOffset = Offset(-padding, -padding),
            paint = androidx.compose.ui.graphics.Paint(),
        )
    }
    drawContent()
}

/** Convenience glow for the cyan hero accent. */
fun Modifier.cyanGlow(blurDp: Dp = 8.dp): Modifier = glow(NeonCyan, blurDp)

/** Convenience glow for the purple hero accent. */
fun Modifier.purpleGlow(blurDp: Dp = 8.dp): Modifier = glow(NeonPurple, blurDp)

/** Horizontal cyan->purple gradient used for hero accents. */
fun cyberGradient(): Brush = Brush.horizontalGradient(listOf(NeonCyan, NeonPurple))