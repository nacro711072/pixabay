package com.nacro.compose.pixabay.ui.transformation

import android.graphics.Bitmap
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import androidx.core.graphics.applyCanvas
import androidx.core.graphics.createBitmap
import coil.size.Size
import coil.transform.Transformation
import kotlin.math.min

class CircleStrokeTransformation(
    private val strokeConfig: Stroke? = null
): Transformation {

    override val cacheKey: String = javaClass.name

    override suspend fun transform(input: Bitmap, size: Size): Bitmap {
        val paint = Paint(Paint.ANTI_ALIAS_FLAG or Paint.FILTER_BITMAP_FLAG)

        val minSize = min(input.width, input.height)
        val radius = minSize / 2f
        val output = createBitmap(minSize, minSize, input.config ?: Bitmap.Config.ARGB_8888)
        output.applyCanvas {
            drawCircle(radius, radius, radius, paint)
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            drawBitmap(input, radius - input.width / 2f, radius - input.height / 2f, paint)

            strokeConfig?.let { cnf ->
                Paint(Paint.ANTI_ALIAS_FLAG).apply {
                    style = Paint.Style.STROKE
                    color = cnf.color
                    strokeWidth = cnf.widthPx
                }.let { drawCircle(radius, radius, radius - cnf.widthPx / 2f, it) }
            }
        }
        return output
    }

    override fun equals(other: Any?) = other is CircleStrokeTransformation
}

data class Stroke(val widthPx: Float, val color: Int)