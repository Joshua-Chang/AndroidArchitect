package org.devio.`as`.proj.main.fragment.detail

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import kotlinx.android.synthetic.main.activity_detail.*
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.`as`.proj.main.R
import org.devio.`as`.proj.main.biz.account.AccountManager
import org.devio.`as`.proj.main.fragment.home.GoodsItem
import org.devio.`as`.proj.main.model.CommentModel
import org.devio.`as`.proj.main.model.DetailModel
import org.devio.`as`.proj.main.model.GoodsModel
import org.devio.`as`.proj.main.model.selectPrice
import org.devio.`as`.proj.main.route.HiRoute
import org.devio.hi.library.util.HiStatusBar
import org.devio.hi.ui.empty.EmptyView
import org.devio.hi.ui.item.HiAdapter
import org.devio.hi.ui.item.HiDataItem

@Route(path = "/detail/main")
class DetailActivity : HiBaseActivity() {
    private lateinit var viewModel: DetailViewModel
    private var emptyView: EmptyView? = null

    @JvmField
    @Autowired
    var goodsId: String? = null

    @JvmField
    @Autowired
    var goodsModel: GoodsModel? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, statusBarColor = Color.TRANSPARENT, true)
        HiRoute.inject(this)
        assert(!TextUtils.isEmpty(goodsId)) { "goodsId must not be null" }
        setContentView(R.layout.activity_detail)
        initView()
        preBindData()
        viewModel = DetailViewModel.get(goodsId, this)
        viewModel.queryDetailData().observe(this, Observer {
            if (it == null) {
                showEmptyView()
            } else {
                bindData(it)
            }
        })
    }

    /*预渲染数据绑定*/
    private fun preBindData() {
        if (goodsModel == null) return
        val hiAdapter = recycler_view.adapter as HiAdapter
        hiAdapter.addItem(
            0, HeaderItem(
                goodsModel!!.sliderImages,
                selectPrice(goodsModel!!.groupPrice, goodsModel!!.marketPrice)!!,
                goodsModel!!.completedNumText,
                goodsModel!!.goodsName
            ), false
        )
    }

    private fun bindData(detailModel: DetailModel) {
        recycler_view.visibility = View.VISIBLE
        emptyView?.visibility = View.GONE

        val hiAdapter = recycler_view.adapter as HiAdapter
        val dataItems = mutableListOf<HiDataItem<*, *>>()
        dataItems.add(
            HeaderItem(
                detailModel.sliderImages,
                selectPrice(detailModel.groupPrice, detailModel.marketPrice)!!,
                detailModel.completedNumText,
                detailModel.goodsName
            )
        )
        dataItems.add(CommentItem(detailModel))
        dataItems.add(ShopItem(detailModel))
        dataItems.add(GoodsAttrItem(detailModel))
        detailModel.gallery?.forEach {
            dataItems.add(GalleryItem(it))
        }
        detailModel.similarGoods?.let {
            dataItems.add(SimilarTitleItem())
            it.forEach {
                dataItems.add(GoodsItem(it, false))
            }
        }
        hiAdapter.clearItems()
        hiAdapter.addItems(dataItems, true)
        updateFavoriteActionFace(detailModel.isFavorite)
        updateOrderActionFace(detailModel)
    }

    @SuppressLint("SetTextI18n")
    private fun updateOrderActionFace(detailModel: DetailModel) {
        action_order.text = "${
            selectPrice(
                detailModel.groupPrice,
                detailModel.marketPrice
            )
        }"+"\n立即购买"
        action_order.setOnClickListener {
            //点击立即购买跳转 下单页
            val bundle = Bundle()
            bundle.putString("shopName", detailModel.shop.name)
            bundle.putString("shopLogo", detailModel.shop.logo)
            bundle.putString("goodsId", detailModel.goodsId)
            bundle.putString("goodsImage", detailModel.sliderImage)
            bundle.putString("goodsName", detailModel.goodsName)
            bundle.putString(
                "goodsPrice",
                selectPrice(detailModel.groupPrice, detailModel.marketPrice)
            )
        }
    }

    private fun updateFavoriteActionFace(favorite: Boolean) {
        action_favorite.setOnClickListener {
            toggleFavorite()
        }
        action_favorite.setTextColor(
            ContextCompat.getColor(
                this,
                if (favorite) R.color.color_dd2 else R.color.color_999
            )
        )
    }

    private fun toggleFavorite() {
        if (!AccountManager.isLogin()) {
            AccountManager.login(this, Observer { loginSuccess ->
                if (loginSuccess) {
                    toggleFavorite()
                }
            })
        } else {
            action_favorite.isClickable = false/*请求期间禁用*/
            viewModel.toggleFavorite().observe(this, Observer { success ->
                if (success != null) {
                    updateFavoriteActionFace(success)
                    val message =
                        if (success) "收藏成功" else "取消收藏成功"
                    showToast(message)
                } else {
                    //失败
                }
                action_favorite.isClickable = true
            })
        }
    }

    private fun showEmptyView() {
        if (emptyView == null) {
            emptyView = EmptyView(this)
            emptyView!!.setIcon(R.string.if_empty3)
            emptyView!!.setDesc(getString(R.string.list_empty_desc))
            emptyView!!.layoutParams = ConstraintLayout.LayoutParams(-1, -1)
            emptyView!!.setBackgroundColor(Color.WHITE)
            emptyView!!.setButton(getString(R.string.list_empty_action), View.OnClickListener {
                viewModel.queryDetailData()
            })

            root_container.addView(emptyView)
        }

        recycler_view.visibility = View.GONE
        emptyView!!.visibility = View.VISIBLE
    }

    private fun initView() {
        action_back.setOnClickListener { onBackPressed() }
        recycler_view.layoutManager = GridLayoutManager(this, 2)
        recycler_view.adapter = HiAdapter(this)
        recycler_view.addOnScrollListener(TitleScrollListener{
            title_bar.setBackgroundColor(it)
        })
    }
}