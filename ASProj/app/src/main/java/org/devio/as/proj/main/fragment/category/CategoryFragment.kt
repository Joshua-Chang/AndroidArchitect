package org.devio.`as`.proj.main.fragment.category

import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.util.SparseIntArray
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.fragment_category.*
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.`as`.proj.common.ui.view.loadUrl
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.CategoryApi
import org.devio.`as`.proj.main.model.Subcategory
import org.devio.`as`.proj.main.model.TabCategory
import org.devio.`as`.proj.main.route.HiRoute
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.ui.empty.EmptyView
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout

/**
 * SpanCount：一行分几个位  SpanSize：一个item占几个位
 * spanSizeLookUp.getSpanSize定制每个item的占位多少（SpanSize）
 */
class CategoryFragment : HiBaseFragment() {
    private var emptyView: EmptyView? = null
    private val SPAN_COUNT = 3
    private val subcategoryListCache = mutableMapOf<String, List<Subcategory>>()

    override fun getLayoutId(): Int {
        return R.layout.fragment_category
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(root_container)
        queryCategoryList()
    }

    private fun queryCategoryList() {
        ApiFactory.create(CategoryApi::class.java).queryCategoryList()
            .enqueue(object : HiCallback<List<TabCategory>> {
                override fun onSuccess(response: HiResponse<List<TabCategory>>) {
                    if (response.successful() && response.data != null) {
                        onQueryCategoryListSuccess(response.data!!)
                    } else {
                        showEmptyView()
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    showEmptyView()
                }
            })
    }

    private fun onQueryCategoryListSuccess(data: List<TabCategory>) {
        if (!isAlive) return
        slider_view.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE
        slider_view.bindMenuView(
            itemCount = data.size,
            // holder.menu_item_tilte    无法直接访问
            //  holder.itemView.menu_item_title.  findviewbyid
            onBindView = { holder, position ->
                val category = data[position]
                holder.findViewById<TextView>(R.id.menu_item_title)?.text = category.categoryName
            },
            onItemClick = { holder, position ->
                val category = data[position]
                val categoryId = category.categoryId
                if (subcategoryListCache.containsKey(categoryId)) {
                    onQuerySubcategoryListSuccess(subcategoryListCache[categoryId]!!)
                }else{
                    querySubcategoryList(categoryId)
                }
            })
    }

    private fun querySubcategoryList(categoryId: String) {
        ApiFactory.create(CategoryApi::class.java).querySubcategoryList(categoryId)
            .enqueue(object : HiCallback<List<Subcategory>> {
                override fun onSuccess(response: HiResponse<List<Subcategory>>) {
                    if (response.successful() && response.data != null) {
                        onQuerySubcategoryListSuccess(response.data!!)
                        if (!subcategoryListCache.containsKey(categoryId)){
                            subcategoryListCache[categoryId]=response.data!!
                        }
                    }
                }

                override fun onFailed(throwable: Throwable) {
                    throwable.printStackTrace()
                }
            })
    }

    private val subcategoryList = mutableListOf<Subcategory>()
    private val decoration = CategoryItemDecoration({ position ->
        subcategoryList[position].groupName
    }, SPAN_COUNT)
    private val layoutManager = GridLayoutManager(context, SPAN_COUNT)
    private val groupSpanSizeOffset = SparseIntArray()//SparseArray不用指定范型为Int
    private val spanSizeLookUp = object : GridLayoutManager.SpanSizeLookup() {
        override fun getSpanSize(position: Int): Int {
            var spanSize = 1
            val groupName = subcategoryList[position].groupName
            val nextGroupName: String? =
                if (position + 1 < subcategoryList.size/*防止边界溢出*/) subcategoryList[position + 1].groupName else null
            if (TextUtils.equals(groupName, nextGroupName)) {
                spanSize = 1
            } else {
                //SparseIntArray中 k，v，index 是相互独立的
                //当前位置和 下一个位置 不再同一个分组
                //1 .要拿到当前组 position （所在组）在 groupSpanSizeOffset 的索引下标
                val indexOfKey = groupSpanSizeOffset.indexOfKey(position)
                val size = groupSpanSizeOffset.size()
                //2 .拿到当前组 前面一组存储的 spansizeoffset 累加的总偏移
                val lastGroupOffset = if (size <= 0) 0
                //有总偏移
                else if (indexOfKey >= 0) {//有总偏移，当前组有偏移量记录 ：这个情况发生在上下滑动过
                    if (indexOfKey == 0) 0 else groupSpanSizeOffset.valueAt(indexOfKey - 1)//当前组索引-1=上一组
                } else {//有总偏移，当前组无偏移量记录：这个情况发生在 第一次布局的时候
                    groupSpanSizeOffset.valueAt(size - 1)//得到所有组的偏移量之和
                }
                //          3       -     (6     +    5             )  % 3  第几列=0  ，1 ，2
                //3 .给当前组最后一个item 分配 spansize
                spanSize = SPAN_COUNT - (position + lastGroupOffset) % SPAN_COUNT

                if (indexOfKey < 0) {//0.之前没存过
                    //得到当前组 和前面所有组的spansize 偏移量之和
                    val groupOffset = lastGroupOffset + spanSize - 1/*减去自己本身应有的spanSize*/
                    groupSpanSizeOffset.put(position, groupOffset)
                }
            }
            return spanSize
        }
    }

    private fun onQuerySubcategoryListSuccess(data: List<Subcategory>) {
        if (!isAlive)return
        decoration.clear()
        groupSpanSizeOffset.clear()
        subcategoryList.clear()
        subcategoryList.addAll(data)//暴露给spanSizeLookUp使用

        if (layoutManager.spanSizeLookup != spanSizeLookUp) {
            layoutManager.spanSizeLookup = spanSizeLookUp
        }
        slider_view.bindContentView(
            itemCount = data.size,
            layoutManager = layoutManager,
            itemDecoration = decoration,
            onBindView = { holder, position ->
                val subcategory = data[position]
                holder.findViewById<ImageView>(R.id.content_item_image)
                    ?.loadUrl(subcategory.subcategoryIcon)
                holder.findViewById<TextView>(R.id.content_item_title)?.text =
                    subcategory.subcategoryName
            },
            onItemClick = { holder, position ->
                val subcategory = data[position]
                //跳转商品列表
                val bundle = Bundle()
                bundle.putString("categoryId", subcategory.categoryId)
                bundle.putString("subcategoryId", subcategory.subcategoryId)
                bundle.putString("categoryTitle", subcategory.subcategoryName)
                HiRoute.startActivity(context!!, bundle, HiRoute.Destination.GOODS_LIST)
            },
        )
    }

    private fun showEmptyView() {
        if (!isAlive) return
        if (emptyView == null) {
            emptyView = EmptyView(context!!)
            emptyView?.setIcon(R.string.if_empty3)
            emptyView?.setDesc(getString(R.string.list_empty_desc))
            emptyView?.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                queryCategoryList()
            })
            emptyView?.setBackgroundColor(Color.WHITE)
            emptyView?.layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT
            )
            root_container.addView(emptyView)

        }

        slider_view.visibility = View.GONE
        emptyView?.visibility = View.VISIBLE
    }
}