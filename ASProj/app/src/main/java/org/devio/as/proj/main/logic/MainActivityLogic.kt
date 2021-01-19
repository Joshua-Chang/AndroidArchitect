package org.devio.`as`.proj.main.logic

import android.content.res.Resources
import android.os.Bundle
import android.view.View
import androidx.annotation.IdRes
import androidx.annotation.StringRes
import androidx.fragment.app.FragmentManager
import org.devio.`as`.proj.common.tab.FragmentTabView
import org.devio.`as`.proj.common.tab.HiTabViewAdapter
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.fragment.category.CategoryFragment
import org.devio.`as`.proj.main.fragment.FavoriteFragment
import org.devio.`as`.proj.main.fragment.HomePageFragment
import org.devio.`as`.proj.main.fragment.ProfileFragment
import org.devio.hi.ui.tab.bottom.HiTabBottomInfo
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout
import kotlin.collections.ArrayList

class MainActivityLogic(val activityProvider: ActivityProvider,saveInstanceState: Bundle?) {
    init {
        if (saveInstanceState!=null){
            saveInstanceState.getInt(SAVED_CURRENT_ID).let { currentItemIndex=it }
        }
        initTabBottom()
    }

    var fragmentTabView: FragmentTabView? = null
    var hiTabBottomLayout: HiTabBottomLayout? = null
    var infoList: ArrayList<HiTabBottomInfo<*>>? = null
    var currentItemIndex = 0
    private fun initTabBottom() {
        hiTabBottomLayout = activityProvider.findViewById<HiTabBottomLayout>(R.id.tab_bottom_layout)
        hiTabBottomLayout!!.setTabAlpha(0.85f)
        infoList = ArrayList<HiTabBottomInfo<*>>()
        val defaultColor = activityProvider.getResources().getColor(R.color.tabBottomDefaultColor)
        val tintColor = activityProvider.getResources().getColor(R.color.tabBottomTintColor)
        val homeInfo = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            activityProvider.getString(R.string.if_home),
            null,
            defaultColor,
            tintColor
        )
        homeInfo.fragment = HomePageFragment::class.java

        val infoRecommend = HiTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            activityProvider.getString(R.string.if_favorite),
            null,
            defaultColor,
            tintColor
        )
        infoRecommend.fragment = HomePageFragment::class.java

        val infoCategory = HiTabBottomInfo(
            "分类",
            "fonts/iconfont.ttf",
            activityProvider.getString(R.string.if_category),
            null,
            defaultColor,
            tintColor
        )
        infoCategory.fragment = CategoryFragment::class.java

        val infoChat = HiTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            activityProvider.getString(R.string.if_recommend),
            null,
            defaultColor,
            tintColor
        )
        infoChat.fragment = FavoriteFragment::class.java

        val infoProfile = HiTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            activityProvider.getString(R.string.if_profile),
            null,
            defaultColor,
            tintColor
        )
        infoProfile.fragment = ProfileFragment::class.java

        infoList!!.add(homeInfo)
        infoList!!.add(infoRecommend)
        infoList!!.add(infoCategory)
        infoList!!.add(infoChat)
        infoList!!.add(infoProfile)
        hiTabBottomLayout!!.inflateInfo(infoList!!)
        initFragmentTabView()
        hiTabBottomLayout!!.addTabSelectedChangeListener { index, _, _ ->
            fragmentTabView?.setCurrentItem(index)
            currentItemIndex = index
        }
        hiTabBottomLayout!!.defaultSelected(infoList!!.get(currentItemIndex))
    }

    private fun initFragmentTabView() {
        val hiTabViewAdapter = HiTabViewAdapter(activityProvider.getSupportFragmentManager(), infoList)
        fragmentTabView = activityProvider.findViewById(R.id.fragment_tab_view)
        fragmentTabView!!.adapter = hiTabViewAdapter
    }

    interface ActivityProvider {
        fun <T : View?> findViewById(@IdRes id: Int): T
        fun getResources(): Resources
        fun getSupportFragmentManager(): FragmentManager
        fun getString(@StringRes resId: Int): String?
    }

    companion object {
        private const val SAVED_CURRENT_ID = "SAVED_CURRENT_ID"
    }
    fun onSaveInstanceState(outState: Bundle) {
        outState.putInt(SAVED_CURRENT_ID,currentItemIndex)
    }
}