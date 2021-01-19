package org.devio.`as`.proj.common.ui.view

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners

fun ImageView.loadUrl(url: String) {
    Glide.with(this).load(url).into(this)
}

fun ImageView.loadCircle(url: String) {
    Glide.with(this).load(url).transform(CenterCrop()).into(this)
}
//和imageView的CenterCrop有冲突，加载bitmap后先圆角，然后centerCrop，圆角有可能被裁减掉
//Glide.with(this).load(url).transform(RoundedCorners(corner)).into(this)

fun ImageView.loadCorner(url: String, corner: Int) {
    //加载bitmap后先centerCrop，然后圆角
    Glide.with(this).load(url).transform(CenterCrop(), RoundedCorners(corner)).into(this)
}

fun ImageView.loadCircleBorder(
    url: String,
    borderWidth: Float = 0f,
    borderColor: Int = Color.WHITE
) {
    Glide.with(this).load(url).into(this)
}

class CircleBorderTransform(var borderWidth: Float, var borderColor: Int) : CircleCrop() {
    private var borderPaint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        borderPaint.setColor(borderColor)
        borderPaint.style = Paint.Style.STROKE
        borderPaint.strokeWidth = borderWidth
    }

    override fun transform(
        pool: BitmapPool,
        toTransform: Bitmap,
        outWidth: Int,
        outHeight: Int
    ): Bitmap {
        val circleCropBitmap = super.transform(pool, toTransform, outWidth, outHeight)
        val canvas = Canvas(circleCropBitmap)
        val halfWidth = outWidth / 2.toFloat()
        val halfHeight = outHeight / 2.toFloat()
        canvas.drawCircle(halfWidth,halfHeight,Math.min(halfWidth,halfHeight)-borderWidth/2,borderPaint)
        canvas.setBitmap(null)//清空
        return circleCropBitmap
    }
}