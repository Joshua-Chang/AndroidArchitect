package org.devio.`as`.proj.common.flutter

import android.content.Context
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.bumptech.glide.Glide


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/14 12:10 AM
 * @创建人：常守达
 * @备注：
 */
class HiImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    fun setUrl(url: String) {
        Glide.with(this).load(url).into(this)
    }
}