package org.devio.`as`.proj.main.fragment.detail

import android.text.SpannableString
import android.text.Spanned
import android.text.TextUtils
import android.text.style.AbsoluteSizeSpan
import android.widget.ImageView
import kotlinx.android.synthetic.main.layout_detail_item_header.*
import org.devio.`as`.proj.common.ui.view.loadUrl
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.model.DetailModel
import org.devio.`as`.proj.main.model.SliderImage
import org.devio.hi.ui.banner.core.HiBannerMo
import org.devio.hi.ui.banner.indicator.HiNumIndicator
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

class HeaderItem(
    val sliderImages: List<SliderImage>?,
    val price: String,
    val completedNumText: String?,
    val goodsName: String?
) : HiDataItem<DetailModel, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context?:return
        val bannerItems= arrayListOf<HiBannerMo>()
        sliderImages?.forEach {
            val bannerMo=object : HiBannerMo() {}
            bannerMo.url= it.url
            bannerItems.add(bannerMo)
        }
        holder.hi_banner.setHiIndicator(HiNumIndicator(context))
        holder.hi_banner.setBannerData(bannerItems)
        holder.hi_banner.setBindAdapter { viewHolder, mo, position ->
            val imageView = viewHolder?.rootView as ImageView
            mo?.let { imageView?.loadUrl(it.url) }
        }
        holder.price.text = spanPrice(price)
        holder.sale_desc.text = completedNumText
        holder.title.text = goodsName
    }

    private fun spanPrice(price: String): CharSequence? {
        if (TextUtils.isEmpty(price)) return ""
        val ss=SpannableString(price)
        ss.setSpan(AbsoluteSizeSpan(18,true),1,ss.length,Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        return ss
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_header
    }
}