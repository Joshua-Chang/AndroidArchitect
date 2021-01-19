package org.devio.hi.ui.item

import android.util.SparseArray
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import kotlinx.android.extensions.LayoutContainer

/**
 * 模块gradle内androidExtensions{experimental=true}
 * kotlin写法：holder.itemView.menu_item_title. -》holder.menu_item_tilte 可省略holder内的itemView，直接访问
 * 编译Java代码中，省去了onBindViewHolder中findViewById的损耗，变为findCachedViewById
 * 但是无法跨模块使用
 */
open class HiViewHolder (val view: View): RecyclerView.ViewHolder(view) ,LayoutContainer{
    override val containerView: View?
        get() = view

    //无法跨模块使用时，自己实现的缓存处理
    private var viewCache = SparseArray<View>()
    fun <T : View> findViewById(viewId: Int): T? {
        var view = viewCache.get(viewId)
        if (view == null) {
            view = itemView.findViewById<T>(viewId)
            viewCache.put(viewId, view)
        }
        return view as? T
    }
}