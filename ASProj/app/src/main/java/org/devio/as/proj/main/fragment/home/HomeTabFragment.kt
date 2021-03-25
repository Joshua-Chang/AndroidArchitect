package org.devio.`as`.proj.main.fragment.home

import android.content.res.Configuration
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.devio.`as`.proj.common.ui.component.HiAbsListFragment
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.HomeApi
import org.devio.`as`.proj.main.model.HomeModel
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.restful.annotation.CacheStrategy
import org.devio.hi.library.util.FoldableDeviceUtil
import org.devio.hi.ui.item.HiDataItem

class HomeTabFragment : HiAbsListFragment() {
    private lateinit var viewModel: HomeViewModel
    val DEFAULT_HOT_TAB_CATEGORY_ID = "1"
    private var categoryId: String? = null

    companion object {
        fun newInstance(categoryId: String): HomeTabFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment =
                HomeTabFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun getPageName(): String {
        return "HomeTabFragment"
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        categoryId = arguments?.getString("categoryId", DEFAULT_HOT_TAB_CATEGORY_ID)
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        queryTabCategoryList(CacheStrategy.CACHE_FIRST)
        enableLoadMore { queryTabCategoryList(CacheStrategy.NET_ONLY) }
    }

    /*mvvm改造*/
    private fun queryTabCategoryList(cacheStrategy: Int) {
        viewModel.queryTabCategoryList(categoryId, pageIndex, cacheStrategy)
            .observe(viewLifecycleOwner, Observer {
                if (it != null) {
                    updateUI(it)
                } else {
                    finishRefresh(null)
                }
            })
    }

//    private fun queryTabCategoryList(cacheStrategy: Int) {
//        ApiFactory.create(HomeApi::class.java)
//            .queryTabCategoryList(cacheStrategy, categoryId!!, pageIndex, 10)
//            .enqueue(object : HiCallback<HomeModel> {
//                override fun onSuccess(response: HiResponse<HomeModel>) {
//                    if (response.successful() && response.data != null) {
//                        updateUI(response.data!!)
//                    } else {
//                        finishRefresh(null)
//                    }
//                }
//
//                override fun onFailed(throwable: Throwable) {
//                    finishRefresh(null)
//                }
//            })
//    }

    override fun createLayoutManager(): RecyclerView.LayoutManager {
        val isHotTab = TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
        return if (isHotTab) super.createLayoutManager() else GridLayoutManager(context, 2)
    }

    private fun updateUI(data: HomeModel) {
        if (!isAlive) return
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        data.bannerList?.let {
            dataItems.add(BannerItem(data.bannerList))
        }
        data.subcategoryList?.let {
            dataItems.add(GridItem(it))
        }
        data.goodsList?.forEachIndexed { _, goodsModel ->
            dataItems.add(
                GoodsItem(
                    goodsModel,
                    TextUtils.equals(categoryId, DEFAULT_HOT_TAB_CATEGORY_ID)
                )
            )
        }
        finishRefresh(dataItems)
    }

    override fun onRefresh() {
        super.onRefresh()
        queryTabCategoryList(CacheStrategy.NET_CACHE)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        /*折叠屏展开时为grid*/
        if (FoldableDeviceUtil.isFold()) {
            recyclerView?.layoutManager=LinearLayoutManager(context)
        }else{
            val manager = GridLayoutManager(context, 2)
            manager.spanSizeLookup=object :GridLayoutManager.SpanSizeLookup(){
                override fun getSpanSize(position: Int): Int {
                    return if (position<=1)2 else 1
                }
            }
            recyclerView?.layoutManager= manager
        }
    }
}