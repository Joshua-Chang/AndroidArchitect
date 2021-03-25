package org.devio.`as`.proj.biz_home.goodslist

import android.os.Bundle
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_goods_list.*
import org.devio.`as`.proj.biz_home.R
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.library.util.HiDataBus
import org.devio.hi.library.util.HiStatusBar

/*本activity也可以定义一个AbsListFragment的基类，去继承。而非目前的往activity里添加*/
@Route(path = "/goods/list")
class GoodsListActivity : HiBaseActivity() {
    @JvmField
    @Autowired
    var categoryTitle: String = ""

    @JvmField
    @Autowired
    var categoryId: String = ""

    @JvmField
    @Autowired
    var subcategoryId: String = ""
    private val FRAGMENT_TAG = "GOODS_LIST_FRAGMENT"
    override fun onCreate(savedInstanceState: Bundle?) {
        HiStatusBar.setStatusBar(this, true, enableTranslucent = false)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_goods_list)
        ARouter.getInstance().inject(this)
        action_back.setOnClickListener { onBackPressed() }
        category_title.text = categoryTitle
        //防止 activity 重启时fragment重叠
        var fragment = supportFragmentManager.findFragmentByTag(FRAGMENT_TAG)
        if (fragment==null) {
            fragment = GoodsListFragment.newInstance(categoryId, subcategoryId)
        }
        val ft = supportFragmentManager.beginTransaction()
        if (!fragment.isAdded) {
            ft.add(R.id.container, fragment, FRAGMENT_TAG)
        }
        ft.show(fragment).commitNowAllowingStateLoss()
        /**
         * 黏性事件：就是在发送事件之后再订阅该事件也能收到该事件。
         * Android中就有这样的实例，也就是Sticky Broadcast，即粘性广播。
         * 正常情况下如果发送者发送了某个广播，而接收者在这个广播发送后才注册自己的Receiver，这时接收者便无法接收到刚才的广播，
         * 为此Android引入了StickyBroadcast，在广播发送结束后会保存刚刚发送的广播（Intent），这样当接收者注册完 Receiver后就可以接收到刚才已经发布的广播
         */
        HiDataBus.with<String>("stickyData").observerSticky(this,false, Observer {
            showToast("data from dataBus:"+it)
        })
    }
}