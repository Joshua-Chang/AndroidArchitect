package org.devio.`as`.proj.main.fragment.category

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.text.TextUtils
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import org.devio.hi.library.util.HiDisplayUtil

class CategoryItemDecoration(val getGroupNameByPosition: (Int) -> String, val spanCount: Int) :
    RecyclerView.ItemDecoration() {
    private val groupFirstPositions = mutableMapOf<String, Int>()
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        paint.style = Paint.Style.FILL
        paint.color = Color.BLACK
        paint.isFakeBoldText = true
        paint.textSize = HiDisplayUtil.sp2px(15f).toFloat()
    }

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        //1. 根据 view对象，找到他在列表中处于的位置 adapterposition
        val adapterPosition = parent.getChildAdapterPosition(view)
        if (adapterPosition >= parent.adapter!!.itemCount || adapterPosition < 0) return
        //2.拿到当前位置adapterPosition  对应的 groupname
        val groupName = getGroupNameByPosition(adapterPosition)
        //3.拿到前面一个位置的 groupname
        val preItemGroupName =
            if (adapterPosition > 0) getGroupNameByPosition(adapterPosition - 1) else null
        val isSameGroup = TextUtils.equals(groupName, preItemGroupName)
        if (!isSameGroup && !groupFirstPositions.containsKey(groupName)) {
            //和上一个item不同组
            //即当前位置的item，是当前组的第一个
            //存储起来，以便判断后边的item是第一行
            groupFirstPositions[groupName] = adapterPosition
        }
        val firstRowPosition = groupFirstPositions[groupName] ?: 0
        val isSameRow = adapterPosition - firstRowPosition in 0..spanCount - 1
        if (!isSameGroup || isSameRow) {//新组的首行
            outRect.set(0, HiDisplayUtil.dp2px(40f), 0, 0)
            return
        }
        outRect.set(0, 0, 0, 0)
    }


    override fun onDrawOver(c: Canvas, parent: RecyclerView, state: RecyclerView.State) {
//        decoration.onDraw->recycler.onDraw->decoration.onDrawOver
        val childCount = parent.childCount
        for (index in 0 until childCount) {
            val view = parent.getChildAt(index)
            val adapterPosition = parent.getChildAdapterPosition(view)
            if (adapterPosition >= parent.adapter!!.itemCount || adapterPosition < 0) continue
            val groupName = getGroupNameByPosition(adapterPosition)
            //判断当前位置 是不是分组的第一个位置 如果是，咱们在他的位置上绘制标题
            val groupFirstPosition = groupFirstPositions[groupName]
            if (groupFirstPosition == adapterPosition) {
                val decorationBounds = Rect()
                //为了拿到当前item的 左上右下的坐标信息包含了，margin和扩展空间的
                parent.getDecoratedBoundsWithMargins(view,decorationBounds)
                val textBounds = Rect()
                paint.getTextBounds(groupName, 0, groupName.length, textBounds)
                c.drawText(
                    groupName,
                    HiDisplayUtil.dp2px(16f).toFloat(),
                    (decorationBounds.top+2*textBounds.height()).toFloat(),
                    paint
                )
            }
        }
    }
    fun clear() {
        groupFirstPositions.clear()
    }
}