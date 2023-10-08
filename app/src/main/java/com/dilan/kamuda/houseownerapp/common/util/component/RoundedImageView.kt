package com.dilan.kamuda.houseownerapp.common.util.component

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.PorterDuff
import android.graphics.PorterDuffXfermode
import android.graphics.Rect
import android.graphics.drawable.BitmapDrawable
import android.util.AttributeSet
import android.widget.ImageView
import androidx.core.graphics.drawable.DrawableCompat

class RoundedImageView : ImageView {
    constructor(context: Context?) : super(context) {}

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {}
    constructor(context: Context?, attrs: AttributeSet?, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        var drawable = drawable ?: return
        if (width == 0 || height == 0) {
            return
        }
        var b: Bitmap?
        try {
            b = (drawable as BitmapDrawable).bitmap
        } catch (e: ClassCastException) {
            drawable = DrawableCompat.wrap(drawable).mutate()
            b = Bitmap.createBitmap(
                drawable.intrinsicWidth,
                drawable.intrinsicHeight,
                Bitmap.Config.ARGB_8888
            )
        }
        if (b != null) {
            val bitmap = b.copy(Bitmap.Config.ARGB_8888, true)
            val w = width
            val roundBitmap = getCroppedBitmap(bitmap, w)
            canvas.drawBitmap(roundBitmap, 0f, 0f, null)
        }
    }

    companion object {
        fun getCroppedBitmap(bmp: Bitmap, radius: Int): Bitmap {
            val sbmp: Bitmap
            sbmp = if (bmp.width != radius || bmp.height != radius) {
                Bitmap.createScaledBitmap(bmp, radius, radius, false)
            } else {
                bmp
            }
            val output = Bitmap.createBitmap(
                sbmp.width,
                sbmp.height, Bitmap.Config.ARGB_8888
            )
            val canvas = Canvas(output)
            val paint = Paint()
            val rect = Rect(0, 0, sbmp.width, sbmp.height)
            paint.isAntiAlias = true
            paint.isFilterBitmap = true
            paint.isDither = true
            canvas.drawARGB(0, 0, 0, 0)
            paint.color = Color.parseColor("#BAB399")
            canvas.drawCircle(
                sbmp.width / 2 + 0.7f, sbmp.height / 2 + 0.7f,
                sbmp.width / 2 + 0.1f, paint
            )
            paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
            canvas.drawBitmap(sbmp, rect, rect, paint)
            return output
        }
    }
}