package org.devio.`as`.proj.main.fragment.home

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.synthetic.main.layout_home_op_grid_item.*
import org.devio.`as`.proj.common.ui.view.loadUrl
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.model.Subcategory
import org.devio.`as`.proj.main.route.HiRoute
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

class GridItem(val list: List<Subcategory>) :
    HiDataItem<List<Subcategory>, HiViewHolder>(list) {
    override fun onBindData(holder: HiViewHolder, position: Int) {
        val context = holder.itemView.context
        val gridView = holder.itemView as RecyclerView
        gridView.adapter=GridAdapter(context,list)
    }

    override fun getItemView(parent: ViewGroup): View? {
        val gridView = RecyclerView(parent.context)
        val params = RecyclerView.LayoutParams(
            RecyclerView.LayoutParams.MATCH_PARENT,
            RecyclerView.LayoutParams.WRAP_CONTENT
        )
        params.bottomMargin = HiDisplayUtil.dp2px(10f)
        gridView.layoutManager = GridLayoutManager(parent.context, 5)
        gridView.layoutParams = params
        gridView.setBackgroundColor(Color.WHITE)
        return gridView
    }

    inner class GridAdapter(val context: Context, val list: List<Subcategory>) :
        RecyclerView.Adapter<HiViewHolder>() {
        private lateinit var inflater: LayoutInflater

        init {
            inflater = LayoutInflater.from(context)
        }


        override fun onCreateViewHolder(
            parent: ViewGroup,
            viewType: Int
        ): HiViewHolder {
            val view = inflater.inflate(R.layout.layout_home_op_grid_item, parent, false)
            return object : HiViewHolder(view) {

            }
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            val subcategory = list[position]
//            holder.itemView.item_image.loadUrl(subcategory.subcategoryIcon)
//            holder.itemView.item_title.text = subcategory.subcategoryName

            holder.item_image.loadUrl(subcategory.subcategoryIcon)
            holder.item_title.text = subcategory.subcategoryName

            holder.itemView.setOnClickListener {
                //跳转商品列表
                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle", subcategory.subcategoryName)
                HiRoute.startActivity(context, bundle, HiRoute.Destination.GOODS_LIST)
            }
        }

        override fun getItemCount(): Int {
            return list.size
        }
    }
}