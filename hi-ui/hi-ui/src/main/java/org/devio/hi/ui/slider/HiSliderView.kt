package org.devio.hi.ui.slider

import android.content.Context
import android.util.AttributeSet
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.LayoutRes
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import kotlinx.android.synthetic.main.hi_slider_menu_item.view.*
import org.devio.hi.ui.R
import org.devio.hi.ui.item.HiViewHolder

/**
 * HiSliderView为横向LinearLayout，双RecyclerView：左侧MenuView右侧ContentView
 * @see bindMenuView
 * @see bindContentView
 */
class HiSliderView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {
    private val MENU_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_menu_item
    private val CONTENT_ITEM_LAYOUT_RES_ID = R.layout.hi_slider_content_item
    val menuView = RecyclerView(context)
    val contentView = RecyclerView(context)
    private var menuItemAttr: AttrsParse.MenuItemAttr = AttrsParse.parseMenuItemAttr(context, attrs)

    init {
        orientation = HORIZONTAL
        menuView.layoutParams = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
        menuView.overScrollMode = View.OVER_SCROLL_NEVER//禁用滑动到底部的阴影效果
        menuView.itemAnimator = null

        contentView.layoutParams =
            LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        contentView.overScrollMode = View.OVER_SCROLL_NEVER
        contentView.itemAnimator = null
        addView(menuView)
        addView(contentView)
    }

    fun bindMenuView(
        @LayoutRes layoutRes: Int = MENU_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: (HiViewHolder, Int) -> Unit
    ) {
        menuView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        menuView.adapter = MenuAdapter(layoutRes, itemCount, onBindView, onItemClick)
    }

    inner class MenuAdapter(
        val layoutRes: Int,
        val count: Int,
        val onBindView: (HiViewHolder, Int) -> Unit,
        val onItemClick: (HiViewHolder, Int) -> Unit
    ) : RecyclerView.Adapter<HiViewHolder>() {

        private var currentSelectIndex = 0//本次选中的item的位置
        private var lastSelectIndex = 0//上一次选中的item的位置

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            val params = RecyclerView.LayoutParams(menuItemAttr.width, menuItemAttr.height)
            itemView.layoutParams = params
            itemView.setBackgroundColor(menuItemAttr.normalBackgroundColor)
            itemView.findViewById<TextView>(R.id.menu_item_title)
                ?.setTextColor(menuItemAttr.textColor)
            itemView.findViewById<ImageView>(R.id.menu_item_indicator)
                ?.setImageDrawable(menuItemAttr.indicator)
            return HiViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                currentSelectIndex = position

                notifyItemChanged(position)//更新当前选中的
                notifyItemChanged(lastSelectIndex)//恢复之前选中的
            }
            /**
             * notifyItemChanged 重新onBindViewHolder
             */
            if (currentSelectIndex == position) {
                onItemClick(holder, position)
                lastSelectIndex = currentSelectIndex
            }
            applyItemAttr(position, holder)//应用选中的变化
            onBindView(holder, position)
        }

        private fun applyItemAttr(position: Int, holder: HiViewHolder) {
            val isSelected = position == currentSelectIndex
            val titleView: TextView? = holder.itemView.menu_item_title
            val indicatorView: ImageView? = holder.itemView.menu_item_indicator
            indicatorView?.visibility = if (isSelected) View.VISIBLE else View.GONE
            titleView?.setTextSize(
                TypedValue.COMPLEX_UNIT_PX,
                if (isSelected) menuItemAttr.selectTextSize.toFloat() else menuItemAttr.textSize.toFloat()
            )
            holder.itemView.setBackgroundColor(if (isSelected) menuItemAttr.selectBackgroundColor else menuItemAttr.normalBackgroundColor)
            /**
             * @see AttrsParse.generateColorStateList menuItemTextColor
             */
            titleView?.isSelected = isSelected
        }

        override fun getItemCount(): Int {
            return count
        }

    }

    fun bindContentView(
        layoutRes: Int = CONTENT_ITEM_LAYOUT_RES_ID,
        itemCount: Int,
        itemDecoration: RecyclerView.ItemDecoration?,
        layoutManager: RecyclerView.LayoutManager,
        onBindView: (HiViewHolder, Int) -> Unit,
        onItemClick: (HiViewHolder, Int) -> Unit
    ) {
        if (contentView.layoutManager == null) {
            contentView.layoutManager = layoutManager
            contentView.adapter = ContentAdapter(layoutRes)
            itemDecoration?.let {
                contentView.addItemDecoration(it)
            }
        }
        /**
         * itemCount, onBindView, onItemClick 每次调用bindContentView时可能不同
         * 因此不能通过向bindMenuView一样通过构造函数传递，而是通过update
         */
        val contentAdapter = contentView.adapter as ContentAdapter
        contentAdapter.update(itemCount, onBindView, onItemClick)
        contentAdapter.notifyDataSetChanged()
        contentView.scrollToPosition(0)
    }

    inner class ContentAdapter(val layoutRes: Int) : RecyclerView.Adapter<HiViewHolder>() {
        private lateinit var onItemClick: (HiViewHolder, Int) -> Unit
        private lateinit var onBindView: (HiViewHolder, Int) -> Unit
        private var count: Int = 0
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HiViewHolder {
            val itemView = LayoutInflater.from(context).inflate(layoutRes, parent, false)
            //Content宽度
            val remainSpace=width-paddingLeft-paddingRight-menuItemAttr.width
            val layoutManager=(parent as RecyclerView).layoutManager
            var spanCount = 0
            if (layoutManager is GridLayoutManager) {
                spanCount=layoutManager.spanCount
            }else if (layoutManager is StaggeredGridLayoutManager){
                spanCount=layoutManager.spanCount
            }
            if (spanCount>0){
                val itemWidth = remainSpace / spanCount
                //创建content itemview，设置它的layoutparams 的原因，
                // 是防止图片未加载出来前后 未占位 ，列表滑动时 上下闪动的效果

                itemView.layoutParams = RecyclerView.LayoutParams(itemWidth, itemWidth)
            }
            return HiViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: HiViewHolder, position: Int) {
            onBindView(holder,position)
            holder.itemView.setOnClickListener { onItemClick(holder,position) }
        }

        override fun getItemCount(): Int {
            return count
        }
        fun update(
            itemCount: Int,
            onBindView: (HiViewHolder, Int) -> Unit,
            onItemClick: (HiViewHolder, Int) -> Unit
        ) {
            this.count = itemCount
            this.onBindView = onBindView
            this.onItemClick = onItemClick
        }
    }
}