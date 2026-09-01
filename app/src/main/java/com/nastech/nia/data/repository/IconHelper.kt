package com.nastech.nia.data.repository

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

object IconHelper {

    fun load(context: Context, packageName: String): ImageBitmap? {
        val drawable: Drawable = try {
            context.packageManager.getApplicationIcon(packageName)
        } catch (e: Exception) {
            return null
        }
        return drawable.toImageBitmap()
    }

    fun Drawable.toImageBitmap(): ImageBitmap? {
        val bitmap: Bitmap = when (this) {
            is BitmapDrawable -> bitmap.copy(Bitmap.Config.ARGB_8888, false)
            else -> {
                val bmp = Bitmap.createBitmap(
                    intrinsicWidth.coerceAtLeast(1),
                    intrinsicHeight.coerceAtLeast(1),
                    Bitmap.Config.ARGB_8888
                )
                val canvas = Canvas(bmp)
                setBounds(0, 0, canvas.width, canvas.height)
                draw(canvas)
                bmp
            }
        }
        return bitmap.asImageBitmap()
    }
}