package org.devio.`as`.proj.main.fragment.home

import android.content.Context
import android.os.Bundle
import android.text.TextUtils
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.view.get
import androidx.databinding.DataBindingUtil
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.fragment_profile_page.view.*
import kotlinx.android.synthetic.main.layout_home_goods_list_item1.view.*
import org.devio.`as`.proj.common.ui.view.loadUrl
import org.devio.`as`.proj.main.BR
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.model.GoodsModel
import org.devio.`as`.proj.main.model.Subcategory
import org.devio.`as`.proj.main.model.selectPrice
import org.devio.`as`.proj.main.route.HiRoute
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

open class GoodsItem(val goodsModel: GoodsModel, val hotTab: Boolean/*是热门还是女装*/) :
    HiDataItem<GoodsModel, GoodsItem.GoodsItemHolder>(goodsModel) {
    private val MAX_TAG_SIZE = 3
    override fun onBindData(holder: GoodsItem.GoodsItemHolder, position: Int) {
        val context = holder.itemView.context
//        holder.itemView.item_image.loadUrl(goodsModel.sliderImage!!)
//        holder.itemView.item_title.text = goodsModel.goodsName
//        holder.itemView.item_price.text = selectPrice(goodsModel.groupPrice, goodsModel.marketPrice)
//        holder.itemView.item_sale_desc.text = goodsModel.completedNumText

        holder.binding.setVariable(BR.goodsModel,goodsModel)

        val itemLabelContainer = holder.itemView.item_label_container
        if (itemLabelContainer != null) {
            if (!TextUtils.isEmpty(goodsModel.tags)) {
                itemLabelContainer.visibility = View.VISIBLE
                val split = goodsModel.tags!!.split(" ")
                for (index in split.indices) {
                    val childCount = itemLabelContainer.childCount
                    if (index > MAX_TAG_SIZE - 1) {
                        for (index in childCount - 1 downTo MAX_TAG_SIZE) {
                            itemLabelContainer.removeViewAt(index)
                        }
                        break
                    }

                    val labelView: TextView = if (index > itemLabelContainer.childCount - 1) {
                        val createLabelView = createLabelView(context, index != 0)
                        itemLabelContainer.addView(createLabelView)
                        createLabelView
                    } else {
                        itemLabelContainer.getChildAt(index) as TextView
                    }
                    labelView.text = split[index]
                }
            } else {
                itemLabelContainer.visibility = View.GONE
            }
        }

        if (!hotTab) {
            val margin = HiDisplayUtil.dp2px(2f)
            /*处理grid的分割线，通过left 与recyclerview的left是否相等来判断 是左侧还是右侧的grid*/
            val params = holder.itemView.layoutParams as RecyclerView.LayoutParams
            val parentLeft = hiAdapter?.getAttachRecyclerView()?.left ?: 0
            val parentPaddingLeft = hiAdapter?.getAttachRecyclerView()?.paddingLeft ?: 0
            val itemLeft = holder.itemView.left
            if (itemLeft == (parentLeft + parentPaddingLeft)) {
                params.rightMargin = margin
            } else {
                params.leftMargin = margin
            }
            holder.itemView.layoutParams = params
        }
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putString("goodsId", goodsModel.goodsId)
            bundle.putParcelable("goodsModel", goodsModel)
            HiRoute.startActivity(context, bundle, HiRoute.Destination.DETAIL_MAIN)
        }
    }


    /**
     * 创建商品文字标签
     */
    private fun createLabelView(context: Context, withLeftMargin: Boolean): TextView {
        val labelView = TextView(context)
        labelView.setTextColor(ContextCompat.getColor(context, R.color.color_eed))
        labelView.textSize = 11f
        labelView.gravity = Gravity.CENTER
        val params = LinearLayout.LayoutParams(
            LinearLayout.LayoutParams.WRAP_CONTENT,
            HiDisplayUtil.dp2px(16f)
        )
        params.leftMargin = if (withLeftMargin) HiDisplayUtil.dp2px(5f) else 0
        labelView.layoutParams = params
        labelView.setBackgroundResource(R.drawable.shape_goods_label)
        return labelView
    }


    override fun getItemLayoutRes(): Int {
        return if (hotTab) R.layout.layout_home_goods_list_item1 else R.layout.layout_home_goods_list_item2
    }

//    override fun getItemView(parent: ViewGroup): View? {
//        val inflater = LayoutInflater.from(parent.context)
//        binding =
//            DataBindingUtil.inflate<ViewDataBinding>(inflater, getItemLayoutRes(), parent, false)
//        return binding!!.root
//    }

    override fun getSpanSize(): Int {
        return if (hotTab) super.getSpanSize() else 1
    }

    override fun onCreateViewHolder(parent: ViewGroup): GoodsItemHolder? {
        val inflater = LayoutInflater.from(parent.context)
        var binding =
            DataBindingUtil.inflate<ViewDataBinding>(inflater, getItemLayoutRes(), parent, false)
        return GoodsItemHolder(binding)
    }

    /*binding和viewHolder相关联*/
    class GoodsItemHolder(val binding: ViewDataBinding) : HiViewHolder(binding.root) {

    }
}