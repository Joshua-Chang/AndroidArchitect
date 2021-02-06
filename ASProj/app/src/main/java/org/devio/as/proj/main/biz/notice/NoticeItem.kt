package org.devio.`as`.proj.main.biz.notice

import android.content.Intent
import android.net.Uri
import androidx.core.content.ContextCompat
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.layout_notice_item.view.*
import org.devio.`as`.proj.common.utils.DateUtil
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.model.Notice
import org.devio.hi.library.util.HiRes
import org.devio.hi.ui.item.HiDataItem
import org.devio.hi.ui.item.HiViewHolder

internal class NoticeItem(itemData: Notice):
HiDataItem<Notice,HiViewHolder>(itemData){
    override fun onBindData(holder: HiViewHolder, position: Int) {
        mData?.apply {
            holder.itemView.tv_title.text=title
            if ("goods"==type) {
                holder.itemView.icon.text=HiRes.getString(R.string.if_recommend)
                holder.itemView.setOnClickListener {
                    ARouter.getInstance().build("/detail/main").withString("goodsId", id)
                        .navigation(holder.itemView.context)
                }
            }else{
                holder.itemView.icon.text = HiRes.getString(R.string.if_notice_msg)
                holder.itemView.setOnClickListener {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                        ContextCompat.startActivity(holder.itemView.context, intent, null)
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            holder.itemView.tv_sub_title.text = subtitle
            holder.itemView.tv_publish_date.text = DateUtil.getMDDate(createTime)
        }
    }
    override fun getItemLayoutRes(): Int {
        return R.layout.layout_notice_item
    }
}