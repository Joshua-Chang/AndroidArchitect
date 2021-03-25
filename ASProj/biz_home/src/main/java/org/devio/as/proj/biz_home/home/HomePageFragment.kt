package org.devio.`as`.proj.biz_home.home

import android.os.Build
import android.os.Bundle
import android.util.SparseArray
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import org.devio.`as`.proj.ability.HiAbility
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.biz_home.model.TabCategory
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout
import org.devio.hi.ui.tab.common.IHiTabLayout
import org.devio.hi.ui.tab.top.HiTabTopInfo

class HomePageFragment : HiBaseFragment() {
    override fun getLayoutId(): Int {
        return R.layout.fragment_home
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        HiTabBottomLayout.clipBottomPadding(view_pager)
//        queryTabList()
        /*mvvm改造*/
        val viewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        viewModel.queryCategoryTab()/*对返回的liveData*/.observe(viewLifecycleOwner, Observer {
            it?.let { updateUI(it) }
        })
//        navgation_bar.setNavListener({activity?.finish()})
//        navgation_bar.addRightTextButton("111",View.generateViewId())
//        navgation_bar.addRightTextButton("222",View.generateViewId())
    }

    override fun getPageName(): String {
        return "HomePageFragment"
    }

    private val topTabs = mutableListOf<HiTabTopInfo<Int>>()
    private var topTabSelectIndex: Int = 0
    private val DEFAULT_SELECT_INDEX: Int = 0
    private val onTabSelectedListener =
        IHiTabLayout.OnTabSelectedListener<HiTabTopInfo<*>> { index, lastSelectedInfo, selectedInfo ->
            if (view_pager.currentItem != index) {
                view_pager.setCurrentItem(index, false)
                HiAbility.traceEvent(
                    "home_page_top_tab_switch",
                    mapOf<String, Any>(Pair("current_index", index))
                )
            }
        }

    private fun updateUI(data: List<TabCategory>) {
        if (!isAlive) return
        //topTabs需要提到全局，
        //因为addOnPageChangeListener  在第一次设置的时候，他就绑定了一次的局部变量topTabs
        //而再次刷新时，并没有 重新设置PageChangeListener，但是第一次的topTabs已经被释放了。
        topTabs.clear()
        data.forEachIndexed { index, tabCategory ->
            val defaultColor = ContextCompat.getColor(context!!, R.color.color_333)
            val selectColor = ContextCompat.getColor(context!!, R.color.color_dd2)
            val tabTopInfo = HiTabTopInfo(tabCategory.categoryName, defaultColor, selectColor)
            topTabs.add(tabTopInfo)
        }
        val topTabLayout = top_tab_layout
        topTabLayout.inflateInfo(topTabs as List<HiTabTopInfo<*>>)
        topTabLayout.defaultSelected(topTabs[DEFAULT_SELECT_INDEX])
        topTabLayout.addTabSelectedChangeListener(onTabSelectedListener)/*改造否则缓存和网络一共调两侧*/
        if (view_pager.adapter == null) {
            view_pager.adapter = HomePagerAdapter(
                childFragmentManager/**/,
                FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            )
            view_pager.addOnPageChangeListener(object : ViewPager.SimpleOnPageChangeListener() {
                //这个方法被触发有两种可能，第一种切换顶部tab  第二种 手动滑动翻页
                override fun onPageSelected(position: Int) {
                    if (position != topTabSelectIndex) {//手动滑动翻页:去通知topTabLayout进行切换
                        topTabLayout.defaultSelected(topTabs[position])
                        topTabSelectIndex = position
                    }
                }
            })
        }
        (view_pager.adapter as HomePagerAdapter).update(data)
    }

    inner class HomePagerAdapter(fm: FragmentManager, behavior: Int) :
        FragmentPagerAdapter(fm, behavior) {
        val tabs = mutableListOf<TabCategory>()
        private val fragments = SparseArray<Fragment>(tabs.size)
        override fun getItem(position: Int): Fragment {
            val categoryId = tabs[position].categoryId
            val categoryIdKey = categoryId.toInt()
            var fragment = fragments.get(
                categoryIdKey,
                null
            )/*根据position来复用不合理，position0 缓存中是热门，请求后position0 变为最新*/
            if (fragment == null) {
                fragment = HomeTabFragment.newInstance(tabs[position].categoryId)
                fragments.put(categoryIdKey, fragment)
            }
            return fragment
        }

        override fun getItemPosition(`object`: Any): Int {
            /*
             * 判断刷新前后fragment在viewpager中的位置是否改变
             * 改变了：none
             * 未改变：
             * 避免缓存的tab和新请求的tab一样，而fragment 先detach再通过getItem attach 重复生命周期浪费性能
             * 同时兼顾缓存的tab和新请求的tab不一样的情况
             * */
            val indexOfValue = fragments.indexOfValue(`object` as Fragment)
            val fragment = getItem(indexOfValue)/*刷新前的*fragment对象*/

            return if (fragment == `object`) PagerAdapter.POSITION_UNCHANGED else PagerAdapter.POSITION_NONE
        }

        /*位置对应的item，若会发生变化，都应该复写。默认返回position*/
        override fun getItemId(position: Int): Long {
            return tabs[position].categoryId.toLong()
        }

        override fun getCount(): Int {
            return tabs.size
        }

        fun update(list: List<TabCategory>) {
            tabs.clear()
            tabs.addAll(list)
            notifyDataSetChanged()
        }
    }
}