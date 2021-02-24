package org.devio.`as`.proj.main.fragment.detail

import android.content.Context
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.style.BackgroundColorSpan
import android.text.style.ForegroundColorSpan
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_detail_item_shop.*
import kotlinx.android.synthetic.main.layout_detail_item_shop_goods_item.*
import org.devio.`as`.proj.common.ui.view.loadUrl
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.fragment.home.GoodsItem
import org.devio.`as`.proj.main.model.DetailModel
import org.devio.`as`.proj.main.model.GoodsModel
import org.devio.`as`.proj.main.model.Shop
import org.devio.hi.ui.item.HiAdapter
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

class ShopItem(val detailModel: DetailModel) : HiDataItem<DetailModel, HiViewHolder>() {
    private val SHOP_GOODS_ITEM_SPAN_COUNT = 3
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context ?: return
        val shop: Shop? = detailModel.shop
        shop?.let {
            holder.shop_logo.loadUrl(it.logo)
            holder.shop_title.text = it.name
            holder.shop_desc.text =
                String.format("商品数量：%s 已拼:%s ", it.goodsNum, it.completedNum)

            val evaluation: String? = shop.evaluation/*评价*/
            evaluation?.let {
                val tagContainer = holder.tag_container
                tagContainer.visibility = View.VISIBLE
                //6个元素 每2个组成一个标签  "evaluation":"描述相符 高 服务态度 高 物流服务 高"
                val serviceTags = evaluation.split(" ")
                var index = 0
                for (tagIndex in 0..(serviceTags.size / 2 - 1)) {/*in 包括末尾，所以要-1*/
                    val tagView: TextView = if (tagIndex < tagContainer.childCount) {/*防止反复创建*/
                        tagContainer.getChildAt(tagIndex) as TextView
                    } else {
                        val tagView = TextView(context)
                        val params =
                            LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.WRAP_CONTENT)
                        params.weight = 1f
                        tagView.layoutParams = params
                        tagView.gravity = Gravity.CENTER
                        tagView.textSize = 14f
                        tagView.setTextColor(ContextCompat.getColor(context, R.color.color_666))
                        tagContainer.addView(tagView)
                        tagView
                    }
                    /*服务态度*/
                    val serviceName =
                        if (index >= serviceTags.size) continue else serviceTags[index]
                    /*高*/
                    val serviceTag = serviceTags[index + 1]
                    index += 2
                    val spanTag = spanServiceTag(context, serviceName, serviceTag)
                    tagView.text = spanTag
                }
            }
        }

        val flowGoods: List<GoodsModel>? = detailModel.flowGoods
        flowGoods?.let {
            val flowRecyclerView: RecyclerView = holder.flow_recycler_view
            flowRecyclerView.visibility = View.VISIBLE
            if (flowRecyclerView.layoutManager == null) {
                flowRecyclerView.layoutManager =
                    GridLayoutManager(context, SHOP_GOODS_ITEM_SPAN_COUNT)
            }
            if (flowRecyclerView.adapter == null) {
                flowRecyclerView.adapter = HiAdapter(context)
            }
            val dataItems = mutableListOf<GoodsItem>()
            it.forEach {
                dataItems.add(ShopGoodsItem(it))
            }
            val adapter = flowRecyclerView.adapter as HiAdapter
            adapter.clearItems()
            adapter.addItems(dataItems, true)
        }
    }

    private inner class ShopGoodsItem(goodsModel: GoodsModel) : GoodsItem(goodsModel, false) {
        override fun getItemLayoutRes(): Int {
            return R.layout.layout_detail_item_shop_goods_item
        }
        /*运行时动态改变每个图片的宽高onViewAttachedToWindow*/
        override fun onViewAttachedToWindow(holder: GoodsItemHolder) {
            super.onViewAttachedToWindow(holder)
            val viewParent = holder.itemView.parent as ViewGroup
            val availableWidth = viewParent.measuredWidth - viewParent.paddingLeft - viewParent.paddingRight
            val itemWidth = availableWidth / SHOP_GOODS_ITEM_SPAN_COUNT
            val itemImage = holder.item_image
            val params = itemImage.layoutParams
            params.width=itemWidth
            params.height=itemWidth
            itemImage.layoutParams=params
        }
    }


    private fun spanServiceTag(
        context: Context,
        serviceName: String,
        serviceTag: String
    ): CharSequence? {
        val ss = SpannableString(serviceTag)
        val ssb = SpannableStringBuilder()
        ss.setSpan(
            ForegroundColorSpan(ContextCompat.getColor(context, R.color.color_c61)),
            0,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ss.setSpan(
            BackgroundColorSpan(ContextCompat.getColor(context, R.color.color_f8e)),
            0,
            ss.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        ssb.append(serviceName)
        ssb.append(ss)
        return ssb
    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_shop
    }
}