package org.devio.`as`.proj.biz_search

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextUtils
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RelativeLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_search.*
import org.devio.`as`.proj.biz_search.view.GoodsSearchView
import org.devio.`as`.proj.biz_search.view.HistorySearchView
import org.devio.`as`.proj.biz_search.view.QuickSearchView
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.library.util.HiRes
import org.devio.hi.library.util.HiStatusBar
import org.devio.hi.ui.empty.EmptyView
import org.devio.hi.ui.search.HiSearchView
import org.devio.hi.ui.search.SimpleTextWatcher

@Route(path = "/search/main")
class SearchActivity : HiBaseActivity() {
    private lateinit var viewModel: SearchViewModel
    private lateinit var searchButton: Button
    private lateinit var searchView: HiSearchView
    private var emptyView: EmptyView? = null
    private var goodsSearchView: GoodsSearchView? = null
    private var quickSearchView: QuickSearchView? = null
    private var historyView: HistorySearchView? = null

    private var status = -1

    companion object {
        const val STATUS_EMPTY = 0
        const val STATUS_HISTORY = 1
        const val STATUS_QUICK_SEARCH = 2
        const val STATUS_GOODS_SEARCH = 3
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, enableTranslucent = false)
        setContentView(R.layout.activity_search)

        viewModel = ViewModelProvider(this)[SearchViewModel::class.java]
        initTopBar()
        updateViewStatus(STATUS_EMPTY)
        queryLocalHistory()
    }

    private fun queryLocalHistory() {
        viewModel.queryLocalHistory().observe(this, Observer { histories ->
            if (histories != null) {
                updateViewStatus(STATUS_HISTORY)
                historyView?.bindData(histories)
            } else {
                searchView.editText?.requestFocus()
            }
        })
    }

    /*右侧搜索键*/
    private val searchClickListener = View.OnClickListener {
        val keyword = searchView.editText?.text?.trim().toString()
        if (TextUtils.isEmpty(keyword)) return@OnClickListener
        doKeyWordSearch(KeyWord(null, keyword))
    }

    /*搜索框内的删除按钮、点击联想词*/
    private val updateHistoryListener = View.OnClickListener {
        if (viewModel.keywords.isNullOrEmpty()) {
            updateViewStatus(STATUS_EMPTY)
        } else {
            updateViewStatus(STATUS_HISTORY)
            historyView?.bindData(viewModel.keywords!!)
        }
    }

    private val debounceTextWatcher = object : SimpleTextWatcher() {
        override fun afterTextChanged(s: Editable?) {
            val hasContent = s != null && s.trim().isNotEmpty()
            searchButton.isEnabled = hasContent/*有内容时右侧搜索按钮高亮*/
            if (hasContent) {
                viewModel.quickSearch(s.toString())
                    .observe(this@SearchActivity, Observer { keywords ->
                        if (keywords.isNullOrEmpty()) {
                            updateViewStatus(STATUS_EMPTY)
                        } else {
                            updateViewStatus(STATUS_QUICK_SEARCH)
                            quickSearchView?.bindData(keywords) {/*联想词点击事件*/
                                doKeyWordSearch(it)
                            }
                        }
                    })
            }
            else if (TextUtils.isEmpty(searchView.getKeyword())) {
                //当输入框内容 被回退删除,且没有高亮的关键词时，回退页面状态
                updateHistoryListener.onClick(null)
            }
        }
    }

    private fun doKeyWordSearch(keyWord: KeyWord) {
        //1.搜索框高亮 搜索词
        searchView.setKeyword(keyWord.keyWord, updateHistoryListener)
        //2.keyword 存储起来
        viewModel.saveHistory(keyWord)
        //3. 发起 goodsSearch
        val kwClearIconView: View? = searchView.findViewById(R.id.id_search_clear_icon)
        kwClearIconView?.isEnabled = false/*搜索请求发出后，直到返回结果。中间不能取消*/
        viewModel.goodsSearch(keyWord.keyWord, true)
        viewModel.goodsSearchLiveData.observe(this, Observer { goodsList ->
            val loadInit = viewModel.pageIndex == SearchViewModel.PAGE_INIT_INDEX;
            if (goodsList == null) {
                if (loadInit) {
                    updateViewStatus(STATUS_EMPTY)
                }
            } else {
                updateViewStatus(STATUS_GOODS_SEARCH)/*搜出结果，切换到商品列表*/
                goodsSearchView?.bindData(goodsList, loadInit)
            }
        })
    }


    private fun initTopBar() {
        nav_bar.setNavListener(View.OnClickListener { onBackPressed() })
        searchButton =
            nav_bar.addRightTextButton(R.string.nav_item_search, R.id.id_nav_item_search)
        searchButton.setTextColor(HiRes.getColorStateList(R.color.color_nav_item_search))
        searchButton.setOnClickListener(searchClickListener)
        searchButton.isEnabled = false

        searchView = HiSearchView(this)
        searchView.layoutParams = RelativeLayout.LayoutParams(
            ViewGroup.LayoutParams.MATCH_PARENT,
            HiDisplayUtil.dp2px(38f)
        )
        searchView.setHintText(HiRes.getString(R.string.search_hint))
        searchView.setClearIconClickListener(updateHistoryListener)/*清理输入框时，将刚刚的输入更新到历史*/
        searchView.setDebounceTextChangedListener(debounceTextWatcher)
        nav_bar.setCenterView(searchView)
    }

    private fun updateViewStatus(newStatus: Int) {
        if (status == newStatus) return
        status = newStatus
        var showView: View? = null
        when (status) {
            STATUS_EMPTY -> {
                if (emptyView == null) {
                    emptyView = EmptyView(this)
                    emptyView?.setDesc(HiRes.getString(R.string.list_empty_desc))
                    emptyView?.setIcon(R.string.list_empty)
                }
                showView = emptyView
            }
            STATUS_QUICK_SEARCH -> {
                if (quickSearchView == null) {
                    quickSearchView = QuickSearchView(this)
                }
                showView = quickSearchView
            }
            STATUS_GOODS_SEARCH -> {
                if (goodsSearchView == null) {
                    goodsSearchView = GoodsSearchView(this)
                    goodsSearchView?.enableLoadMore({
                        val keyword = searchView.getKeyword()
                        if (TextUtils.isEmpty(keyword)) return@enableLoadMore
                        //分页
                        viewModel.goodsSearch(keyword!!, false)
                    }, 5)
                }
                showView = goodsSearchView
            }

            STATUS_HISTORY -> {
                if (historyView == null) {
                    historyView = HistorySearchView(this)
                    historyView!!.setOnCheckedChangedListener {
                        doKeyWordSearch(it)
                    }
                    historyView!!.setOnHistoryClearListener {
                        viewModel.clearHistory()
                        updateViewStatus(STATUS_EMPTY)
                    }
                }
                showView = historyView
            }
        }
        if (showView != null) {
            if (showView.parent == null) {
                container.addView(showView)
            }
            val childCount = container.childCount
            for (index in 0 until childCount) {
                val child = container.getChildAt(index)
                child.visibility = if (child == showView) View.VISIBLE else View.GONE
            }
        }
    }
}