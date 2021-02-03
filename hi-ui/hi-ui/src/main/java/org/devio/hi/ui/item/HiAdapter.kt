package org.devio.hi.ui.item

import android.content.Context
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.lang.ref.WeakReference
import java.lang.reflect.ParameterizedType
import java.util.ArrayList
import kotlin.math.log

class HiAdapter(context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private var mContext: Context? = null
    private var mInflater: LayoutInflater? = null
    private var dataSets = ArrayList<HiDataItem<*, out RecyclerView.ViewHolder>>()
    private var typeArrays = SparseArray<HiDataItem<*, out RecyclerView.ViewHolder>>()


    private var headers = SparseArray<View>()
    private var footers = SparseArray<View>()
    private var BASE_ITEM_TYPE_HEADER = 1000000
    private var BASE_ITEM_TYPE_FOOTER = 2000000
    fun addHeaderView(view: View) {
        //没有添加过
        if (headers.indexOfValue(view) < 0) {
            //2
            headers.put(BASE_ITEM_TYPE_HEADER++/*viewType并和普通type区分*/, view)
            notifyItemInserted(headers.size() - 1)
        }
    }
    fun removeHeaderView(view: View) {
        val indexOfValue = headers.indexOfValue(view)
        if (indexOfValue < 0) return
        headers.removeAt(indexOfValue)
        notifyItemRemoved(indexOfValue)
    }

    fun addFooterView(view: View) {
        //说明这个fgooterview 没有添加过
        if (footers.indexOfValue(view) < 0) {
            footers.put(BASE_ITEM_TYPE_FOOTER++, view)
            notifyItemInserted(itemCount)
        }
    }

    fun removeFooterView(view: View) {
        //0 1  2
        val indexOfValue = footers.indexOfValue(view)
        if (indexOfValue < 0) return
        footers.removeAt(indexOfValue)
        //position代表的是在列表中分位置
        notifyItemRemoved(indexOfValue + getHeaderSize() + getOriginalItemSize())
    }
    fun getHeaderSize(): Int {
        return headers.size()
    }

    fun getFooterSize(): Int {
        return footers.size()
    }

    fun getOriginalItemSize(): Int {
        return dataSets.size
    }
    private fun isHeaderPosition(position: Int): Boolean {
        // 5 --> 4 3 2 1
        return position < headers.size()
    }

    private fun isFooterPosition(position: Int): Boolean {
        // 10->  4+ 4.
        return position >= getHeaderSize() + getOriginalItemSize()
    }


    init {
        this.mContext = context
        this.mInflater = LayoutInflater.from(context)
    }

    fun addItem(index: Int, item: HiDataItem<*, out RecyclerView.ViewHolder>, notify: Boolean) {
        if (index > 0) {
            dataSets.add(index, item)
        } else {
            dataSets.add(item)
        }
        val notifyPos = if (index > 0) index else dataSets.size - 1
        if (notify) {
            notifyItemInserted(notifyPos)
        }
    }

    fun addItems(items: List<HiDataItem<*, out RecyclerView.ViewHolder>>, notify: Boolean) {
        val start = dataSets.size
        for (item in items) {
            dataSets.add(item)
        }
        if (notify) {
            notifyItemRangeInserted(start, items.size)
        }
    }

    /**
     * 移除指定item
     */
    fun removeItem(dataItem: HiDataItem<*, out RecyclerView.ViewHolder>) {
        val index: Int = dataSets.indexOf(dataItem);
        removeItemAt(index)
    }

    /**
     * 从指定位置上移除item
     */
    fun removeItemAt(index: Int): HiDataItem<*, out RecyclerView.ViewHolder>? {
        if (index > 0 && index < dataSets.size) {
            val remove = dataSets.removeAt(index)
            notifyItemRemoved(index)
            return remove
        } else {
            return null
        }
    }

    /**
     * 指定刷新 某个item的数据
     */
    fun refreshItem(dataItem: HiDataItem<*, out RecyclerView.ViewHolder>) {
        val indexOf = dataSets.indexOf(dataItem)
        notifyItemChanged(indexOf)
    }

    override fun getItemViewType(position: Int): Int {
        if (isHeaderPosition(position)){
            return headers.keyAt(position)
        }
        if (isFooterPosition(position)) {
            //footer的位置 应该计算一下  position =6 , headercount =1  itemcoun=5 =,footersize=1
            val footerPosition = position - getHeaderSize() - getOriginalItemSize()
            return footers.keyAt(footerPosition)
        }
        //非Header/Footer类型
        val itemPosition = position - getHeaderSize()
//        val dataItem = dataSets[position]//重新计算
        val dataItem = dataSets[itemPosition]//
        val type = dataItem.javaClass.hashCode()
        if (typeArrays.indexOfKey(type) < 0) {
            typeArrays.put(type, dataItem)
        }
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (headers.indexOfKey(viewType) >= 0) {
            val view = headers[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }
        if (footers.indexOfKey(viewType) >= 0) {
            val view = footers[viewType]
            return object : RecyclerView.ViewHolder(view) {}
        }
        //非Header/Footer类型

        val dataItem = typeArrays.get(viewType)
        var view: View? = dataItem.getItemView(parent)
        if (view == null) {
            val layoutRes = dataItem.getItemLayoutRes()
            if (layoutRes < 0) {
                RuntimeException("dataItem:" + dataItem.javaClass.name + " must override getItemView or getItemLayoutRes")
            }
            view = mInflater!!.inflate(layoutRes, parent, false)
        }
        return createViewHolderInternal(dataItem.javaClass, view!!)
    }

    private fun createViewHolderInternal(
        javaClass: Class<HiDataItem<*, out RecyclerView.ViewHolder>>,
        view: View
    ): RecyclerView.ViewHolder {
        //目标：实例化ViewHolder，HiDataItem的具体实现类的具体范型参数
        //GridItem/BannerDataItem:HiDataItem
        //class GirdDataItem(data: ItemData) : HiDataItem<ItemData, GirdDataItem.MyHolder>(data)
        //abstract class HiDataItem <DATA,VH:RecyclerView.ViewHolder>(data:DATA)
        //type的子类常见的有 class，类泛型,ParameterizedType参数泛型 ，TypeVariable字段泛型
        //所以进一步判断它是不是参数泛型
//        javaClass.superclass 加generic的方法实质是1.5范型出来以后对类型新的封装 多加了带范型的表示方法
        //https://qidawu.github.io/2018/11/21/java-reflection-generic-type/
        val superclass = javaClass.genericSuperclass//得到该Item的带范型的父类类型,HiDataItem<ItemData, GirdDataItem.MyHolder>
        if (superclass is ParameterizedType) {
            val actualTypeArguments = superclass.actualTypeArguments//得到具体范型参数,即<ItemData, GirdDataItem.MyHolder>
            for (argument in actualTypeArguments) {//遍历取出其中是RecyclerView.ViewHolder子类的那个参数类型
                if (argument is Class<*> && RecyclerView.ViewHolder::class.java.isAssignableFrom(argument)
                ) {
                    try {
                        //如果是则使用反射 实例化类上标记的实际的泛型对象
                        //这里需要  try-catch 一把，如果咱们直接在HiDataItem子类上标记 RecyclerView.ViewHolder，抽象类是不允许反射的
                        return argument.getConstructor(View::class.java)
                            .newInstance(view) as RecyclerView.ViewHolder
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
        return object : HiViewHolder(view) {}
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (isHeaderPosition(position) || isFooterPosition(position)) return
        //非Header/Footer类型

        val itemPosition = position - getHeaderSize()//重新计算
        val dataItem = getItem(itemPosition)
        dataItem?.onBindData(holder, itemPosition)
    }


    override fun getItemCount(): Int {
        return dataSets.size+getHeaderSize()+getHeaderSize()
    }
    /**
     * 处理grid
     */
    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        recyclerViewRef = WeakReference(recyclerView)
        val layoutManager = recyclerView.layoutManager
        if (layoutManager is GridLayoutManager) {
            val spanCount = layoutManager.spanCount
            layoutManager.spanSizeLookup= object :GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    if (isHeaderPosition(position) || isFooterPosition(position)) {
                        return spanCount
                    }
                    val itemPosition = position - getHeaderSize()//重新计算
                    if (itemPosition<dataSets.size) {
                        val hiDataItem = getItem(itemPosition)
                        if (hiDataItem != null) {
                            val spanSize = hiDataItem.getSpanSize()
                            return if (spanSize<=0/*没复写设置spanSize的item默认占整行*/) spanCount else spanSize
                        }
                    }
                    return spanCount
                }
            }
        }
    }
    private var recyclerViewRef: WeakReference<RecyclerView>? = null

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        super.onDetachedFromRecyclerView(recyclerView)
        recyclerViewRef?.clear()
    }
    open fun getAttachRecyclerView(): RecyclerView? {
        return recyclerViewRef?.get()
    }

    /**
     * 处理瀑布流
     */
    override fun onViewAttachedToWindow(holder: RecyclerView.ViewHolder) {
        val recyclerView = getAttachRecyclerView()
        if (recyclerView != null) {
            //瀑布流的item占比适配
            val position = recyclerView.getChildAdapterPosition(holder.itemView)
            val isHeaderFooter = isHeaderPosition(position) || isFooterPosition(position)
            val itemPosition = position - getHeaderSize()
            val dataItem = getItem(itemPosition) ?: return
            val lp = holder.itemView.layoutParams
            if (lp != null && lp is StaggeredGridLayoutManager.LayoutParams) {
                val manager = recyclerView.layoutManager as StaggeredGridLayoutManager?
                if (isHeaderFooter) {
                    lp.isFullSpan = true
                    return
                }
                val spanSize = dataItem.getSpanSize()
                if (spanSize == manager!!.spanCount) {
                    lp.isFullSpan = true
                }
            }

            dataItem.onViewAttachedToWindow(holder)
        }
    }

    override fun onViewDetachedFromWindow(holder: RecyclerView.ViewHolder) {
        val position = holder.adapterPosition
        if (isHeaderPosition(position) || isFooterPosition(position))
            return
        val itemPosition = position - getHeaderSize()
        val dataItem = getItem(itemPosition) ?: return
        dataItem.onViewDetachedFromWindow(holder)
    }

    fun getItem(position: Int): HiDataItem<*, RecyclerView.ViewHolder>? {
        if (position < 0 || position >= dataSets.size)
            return null
        return dataSets[position] as HiDataItem<*, RecyclerView.ViewHolder>
    }

    fun clearItems() {
        dataSets.clear()
        notifyDataSetChanged()
    }
}