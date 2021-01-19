package org.devio.hi.ui.banner.indicator

import android.content.Context
import android.util.AttributeSet
import android.view.Gravity
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.annotation.DrawableRes
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.ui.R

class HiCircleIndicator @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : FrameLayout(context, attrs, defStyleAttr), HiIndicator<FrameLayout?> {

    companion object {
        private const val VWC = ViewGroup.LayoutParams.WRAP_CONTENT
    }

    /**
     * 正常状态下的指示点
     */
    @DrawableRes
    private val mPointNormal = R.drawable.shape_point_normal

    /**
     * 选中状态下的指示点
     */
    @DrawableRes
    private val mPointSelected = R.drawable.shape_point_select

    /**
     * 指示点左右内间距
     */
    private var mPointLeftRightPadding = 0

    /**
     * 指示点上下内间距
     */
    private var mPointTopBottomPadding = 0

    init {
        mPointLeftRightPadding = HiDisplayUtil.dp2px(5f, context.resources)
        mPointTopBottomPadding = HiDisplayUtil.dp2px(15f, context.resources)
    }

    override fun get(): FrameLayout {
        return this
    }

    override fun onInflate(count: Int) {
        removeAllViews()
        if (count <= 0) return
        val linearLayout = LinearLayout(context)
        linearLayout.orientation = LinearLayout.HORIZONTAL
        var imageView: ImageView
        val imageParams = LinearLayout.LayoutParams(VWC, VWC)
        imageParams.apply {
            gravity = Gravity.CENTER_VERTICAL
            setMargins(
                mPointLeftRightPadding,
                mPointTopBottomPadding,
                mPointLeftRightPadding,
                mPointTopBottomPadding
            )
        }
        for (i in 0 until count) {
            imageView = ImageView(context)
            imageView.layoutParams = imageParams
            if (i == 0) {
                imageView.setImageResource(mPointSelected)
            } else {
                imageView.setImageResource(mPointNormal)
            }
            linearLayout.addView(imageView)
        }
        val layoutParams = LayoutParams(VWC, VWC)
        layoutParams.gravity = Gravity.CENTER or Gravity.BOTTOM
        addView(linearLayout, layoutParams)
    }

    override fun onPointChange(current: Int, count: Int) {
        val viewGroup = getChildAt(0) as ViewGroup //父布局linearLayout
        for (i in 0 until viewGroup.childCount) {
            val imageView = viewGroup.getChildAt(i) as ImageView
            if (i == current) {
                imageView.setImageResource(mPointSelected)
            } else {
                imageView.setImageResource(mPointNormal)
            }
            imageView.requestLayout()
        }
    }
}