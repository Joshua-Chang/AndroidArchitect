package org.devio.hi.ui.app.demo.tab

import android.graphics.BitmapFactory
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import org.devio.hi.ui.app.R
import org.devio.hi.ui.app.MApplication
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.tab.bottom.HiTabBottomInfo
import org.devio.hi.ui.tab.bottom.HiTabBottomLayout
import java.util.*

class HiTabBottomDemoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_tab_bottom_demo)
        MApplication.shortCut("1-1-2:bottom", this)
        initTabBottom()
    }

    private fun initTabBottom() {
        val hiTabBottomLayout: HiTabBottomLayout = findViewById(R.id.hitablayout)
        hiTabBottomLayout.setTabAlpha(0.85f)
        val bottomInfoList: MutableList<HiTabBottomInfo<*>> = ArrayList()
        val homeInfo = HiTabBottomInfo(
            "首页",
            "fonts/iconfont.ttf",
            getString(R.string.if_home),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoRecommend = HiTabBottomInfo(
            "收藏",
            "fonts/iconfont.ttf",
            getString(R.string.if_favorite),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val bitmap = BitmapFactory.decodeResource(resources, R.drawable.fire, null)

        val infoCategory = HiTabBottomInfo<String>(
            "分类",
            bitmap,
            bitmap
        )
        val infoChat = HiTabBottomInfo(
            "推荐",
            "fonts/iconfont.ttf",
            getString(R.string.if_recommend),
            null,
            "#ff656667",
            "#ffd44949"
        )
        val infoProfile = HiTabBottomInfo(
            "我的",
            "fonts/iconfont.ttf",
            getString(R.string.if_profile),
            null,
            "#ff656667",
            "#ffd44949"
        )
        bottomInfoList.add(homeInfo)
        bottomInfoList.add(infoRecommend)
        bottomInfoList.add(infoCategory)
        bottomInfoList.add(infoChat)
        bottomInfoList.add(infoProfile)
        hiTabBottomLayout.inflateInfo(bottomInfoList)
        hiTabBottomLayout.addTabSelectedChangeListener { _, _, currentInfo ->
            Toast.makeText(this@HiTabBottomDemoActivity, currentInfo.name, Toast.LENGTH_SHORT).show()
        }
        hiTabBottomLayout.defaultSelected(homeInfo)
//                改变某个tab的高度
        val tabBottom = hiTabBottomLayout.findTab(bottomInfoList[2])
        tabBottom?.apply { resetHeight(HiDisplayUtil.dp2px(66f, resources)) }
        tabBottom.resetHeight(HiDisplayUtil.dp2px(66f, resources))
    }
}