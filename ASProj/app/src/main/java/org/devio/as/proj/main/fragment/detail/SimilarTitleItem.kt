package org.devio.`as`.proj.main.fragment.detail

import org.devio.`as`.proj.main.R
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

class SimilarTitleItem : HiDataItem<Any, HiViewHolder>() {
    override fun onBindData(holder: HiViewHolder, position: Int) {

    }

    override fun getItemLayoutRes(): Int {
        return R.layout.layout_detail_item_similar_title
    }
}