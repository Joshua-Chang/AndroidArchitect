package org.devio.`as`.proj.common.ui.view

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import org.devio.`as`.proj.common.R
import org.devio.hi.library.log.HiLog
import org.devio.hi.ui.item.HiAdapter

class HiRecyclerView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RecyclerView(context, attrs, defStyleAttr) {
    private var loadMoreScrollListener: OnScrollListener? = null
    private var footerView: View? = null
    private var isLoadingMore: Boolean = false

    inner class LoadMoreScrollListener(val prefetchSize: Int, val callback: () -> Unit) :
        OnScrollListener() {
        //咱们这里的强转，因为前面 会有前置检查
        val hiAdapter = adapter as HiAdapter
        override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
            //需要根据当前的滑动状态  已决定要不要添加footer view ，要不执行上拉加载分页的动作
            if (isLoadingMore) return
            //咱们需要判断当前类表上 已经显示的 item的个数
            val totalItemCount = hiAdapter.itemCount
            //如果列表上已显示的item的数量小于0
            if (totalItemCount <= 0) return
            //此时，咱们需要在滑动状态为 拖动状态时，就要判断要不要添加footer 目的就是为了防止列表滑动到底部了但是 footerview 还没显示出来，

            //判断RecyclerView ，是否可以继续向下滑动
            val canScrollVertically = recyclerView.canScrollVertically(1)//正数向下 负数向上
            //特殊情况：canScrollVertical可以继续向下滑动。列表已经滑动到底部了，但是分页失败了。
            val lastVisibleItem = findLastVisibleItem(recyclerView)
            val firstVisibleItem = findFirstVisibleItem(recyclerView)
            if (lastVisibleItem <= 0) return
            val isArriveBottom = lastVisibleItem > totalItemCount - 1
            if (newState == SCROLL_STATE_DRAGGING && (canScrollVertically || isArriveBottom)) {
                addFooterView()
            }

            if (newState != SCROLL_STATE_IDLE) {//不能在 滑动停止了，才去添加footer view
                return
            }

            //预加载,就是 不需要等待 滑动到最后一个item的时候，就出发下一页的加载动作
            val isArrivePrefetchPosition = totalItemCount - lastVisibleItem <= prefetchSize
            if (!isArrivePrefetchPosition) return //未到达预加载位置
            isLoadingMore = true
            callback()
        }

        private fun addFooterView() {
            val footerView = getFooterView()
            //坑: 在一些边界场景下。会出现多次添加的情况，
            //添加之前先 remove --》hiAdapter。.removeFooterView()
            //notifyItemRemoved 存在延迟，在边界情况下会发生remove尚未完成，又notifyItemInserted。造成重复添加
            //并报错：add to parent must call remove view first

            //主要是为了避免 removeFooterView 不及时，在边界场景下可能会出现，footerView还没从recyclervIEW上移除掉，但我们又调用了addFooterView，

            if (footerView.parent != null) {
                footerView.post {
                    addFooterView()
                }
            } else {
                hiAdapter.addFooterView(footerView)
            }
        }

        private fun getFooterView(): View {
            if (footerView == null) {
                footerView = LayoutInflater.from(context)
                    .inflate(R.layout.layout_footer_loading, this@HiRecyclerView, false)
            }
            return footerView!!
        }
    }


    private fun findLastVisibleItem(recyclerView: RecyclerView): Int {
        when (val layoutManager = recyclerView.layoutManager) {
            is LinearLayoutManager -> {//GridLayoutManager是LinearLayoutManager子类
                return layoutManager.findLastVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                return layoutManager.findLastVisibleItemPositions(null)[0]//返回最后一个span的[0]
            }
        }
        return -1
    }

    private fun findFirstVisibleItem(recyclerView: RecyclerView): Int {
        when (val layoutManager = recyclerView.layoutManager) {
            //layoutManager is GridLayoutManager
            is LinearLayoutManager -> {
                return layoutManager.findFirstVisibleItemPosition()
            }
            is StaggeredGridLayoutManager -> {
                return layoutManager.findFirstVisibleItemPositions(null)[0]
            }
        }
        return -1
    }


    /*外部暴露方法*/
    fun enableLoadMore(callback: () -> Unit, prefetchSize: Int) {
        if (adapter !is HiAdapter) {
            HiLog.e("enableLoadMore must use hiadapter")
            return
        }
        loadMoreScrollListener = LoadMoreScrollListener(prefetchSize, callback)
        addOnScrollListener(loadMoreScrollListener!!)
    }

    fun disableLoadMore() {
        if (adapter !is HiAdapter) {
            HiLog.e("disableLoadMore must use hiadapter")
            return
        }
        val hiAdapter = adapter as HiAdapter
        footerView?.let {
            if (footerView!!.parent!=null) {
                hiAdapter.removeFooterView(footerView!!)
            }
        }
        loadMoreScrollListener?.let {
            removeOnScrollListener(loadMoreScrollListener!!)
            loadMoreScrollListener = null
            footerView = null
            isLoadingMore = false
        }
    }
    fun loadFinished(success: Boolean) {
        if (adapter !is HiAdapter) {
            HiLog.e("loadFinished must use hiadapter")
            return
        }

        isLoadingMore = false
        val hiAdapter = adapter as HiAdapter
        if (!success) {//加载失败
            footerView?.let {
                if (footerView!!.parent != null) {
                    hiAdapter.removeFooterView(footerView!!)
                }
            }
        } else {
            //nothing to do .
        }
    }
    fun isLoading(): Boolean {
        return isLoadingMore
    }

}