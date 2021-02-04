package org.devio.`as`.proj.main.fragment.detail

import android.graphics.Color
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.view.ViewCompat
import androidx.recyclerview.widget.RecyclerView
import org.devio.`as`.proj.common.ui.view.loadUrl
import org.devio.`as`.proj.main.model.SliderImage
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

class GalleryItem(val sliderImage: SliderImage) : HiDataItem<SliderImage, HiViewHolder>() {
    private var parentWidth: Int = 0
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val imageView = holder.itemView as ImageView
        if (TextUtils.isEmpty(sliderImage.url)) {
            imageView.loadUrl(sliderImage.url) {
                // loadUrl 占位图需要拿到图片加载后的回调，
                // 根据图片的宽高的值，等比计算imageview高度值
                val intrinsicWidth = it.intrinsicWidth
                val intrinsicHeight = it.intrinsicHeight
                val params = imageView.layoutParams /*为空则后边的*/?: RecyclerView.LayoutParams(
                    parentWidth, RecyclerView.LayoutParams.WRAP_CONTENT
                )
                params.width = parentWidth
                params.height = (intrinsicHeight / (intrinsicWidth * 1.0f / parentWidth)).toInt()
                imageView.layoutParams=params
                ViewCompat.setBackground(imageView,it)
            }/*真实环境加载大图要占位图*/
        }
    }

    override fun getItemView(parent: ViewGroup): View? {
        val imageView = ImageView(parent.context)
        imageView.scaleType = ImageView.ScaleType.CENTER_CROP
        imageView.setBackgroundColor(Color.WHITE)
        return imageView
    }

    override fun onViewAttachedToWindow(holder: HiViewHolder) {
        //提前给imageview 预设一个高度值  等于parent的宽度
        parentWidth = (holder.itemView.parent as ViewGroup).measuredWidth
        val params = holder.itemView.layoutParams
        if (params.width != parentWidth) {
            params.width = parentWidth
            params.height = parentWidth
            holder.itemView.layoutParams = params
        }
    }
}