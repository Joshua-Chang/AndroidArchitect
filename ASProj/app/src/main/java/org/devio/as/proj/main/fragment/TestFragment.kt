package org.devio.`as`.proj.main.fragment

import android.os.Bundle
import android.util.SparseArray
import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import kotlinx.android.synthetic.main.fragment_home.*
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.fragment.home.HomeTabFragment
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.HomeApi
import org.devio.`as`.proj.main.model.TabCategory
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout
import org.devio.hi.ui.tab.top.HiTabTopInfo

class TestFragment : HiBaseFragment() {
    private var categoryId: String? = null

    companion object {
        fun newInstance(categoryId: String): TestFragment {
            val args = Bundle()
            args.putString("categoryId", categoryId)
            val fragment =
                TestFragment()
            fragment.arguments = args
            return fragment
        }
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_favorite
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        categoryId = arguments?.getString("categoryId", "0")
        layoutView.findViewById<TextView>(R.id.tv_name).setText(categoryId)
    }
}