package org.devio.hi.ui.title

import android.content.Context
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.Typeface
import android.text.TextUtils
import android.util.AttributeSet
import android.util.TypedValue
import android.view.Gravity
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.RelativeLayout
import androidx.annotation.StringRes
import org.devio.hi.library.util.HiDisplayUtil
import org.devio.hi.library.util.HiRes
import org.devio.hi.ui.R
import org.devio.hi.ui.icfont.IconFontButton
import org.devio.hi.ui.icfont.IconFontTextView
import java.lang.IllegalStateException
import java.util.ArrayList

class HiNavigationBar @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    //主副标题
    private var titleView: IconFontTextView? = null
    private var subtitleView: IconFontTextView? = null
    private var titleContainer: LinearLayout? = null

    //左右按钮
    private var mLeftLastViewId = View.NO_ID/*记录上次添加的view以便判断后续的位置设置*/
    private var mRightLastViewId = View.NO_ID
    private val mLeftViewList = ArrayList<View>()
    private val mRightViewList = ArrayList<View>()

    //属性解析获得对象
    private var navAttrs: AttrsParse.Attrs = AttrsParse.parseNavAttrs(context, attrs, defStyleAttr)

    init {
        if (!TextUtils.isEmpty(navAttrs.navTitle)) {
            setTitle(navAttrs.navTitle!!)
        }

        if (!TextUtils.isEmpty(navAttrs.navSubtitle)) {
            setSubtitle(navAttrs.navSubtitle!!)
        }

        if (navAttrs.lineHeight > 0) {
            addLineView()
        }
    }

    fun setNavListener(listener: OnClickListener) {
        if (!TextUtils.isEmpty(navAttrs.navIconStr)) {
            val navBackView =
                addLeftTextButton(navAttrs.navIconStr!!, R.id.id_nav_left_back_view)
            navBackView.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.navIconSize)
            navBackView.setTextColor(navAttrs.navIconColor)
            navBackView.setOnClickListener(listener)
        }
    }

    fun addLeftTextButton(@StringRes stringRes: Int, viewId: Int): Button {
        return addLeftTextButton(HiRes.getString(stringRes), viewId)
    }

    fun addLeftTextButton(buttonText: String, viewId: Int): Button {
        val button = generateTextButton()
        button.text = buttonText
        button.id = viewId
        if (mLeftViewList.isEmpty()) {
            button.setPadding(navAttrs.horPadding * 2, 0, navAttrs.horPadding, 0)
        } else {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding, 0)
        }

        addLeftView(button, generateTextButtonLayoutParams())
        return button
    }

    fun addLeftView(
        view: View,
        params: LayoutParams
    ) {
        val viewId = view.id
        if (viewId == View.NO_ID) {
            throw IllegalStateException("left view must has an unique id.")
        }
        if (mLeftLastViewId == View.NO_ID) {/*没添加过*/
            params.addRule(ALIGN_PARENT_LEFT, viewId)
        } else {
            params.addRule(RIGHT_OF, mLeftLastViewId)
        }
        mLeftLastViewId = viewId
        params.alignWithParent = true  //alignParentIfMissing
        mLeftViewList.add(view)
        addView(view, params)
    }


    fun addRightTextButton(@StringRes stringRes: Int, viewId: Int): Button {
        return addRightTextButton(HiRes.getString(stringRes), viewId)
    }

    fun addRightTextButton(buttonText: String, viewId: Int): Button {
        val button = generateTextButton()
        button.text = buttonText
        button.id = viewId
        if (mRightViewList.isEmpty()) {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding * 2, 0)
        } else {
            button.setPadding(navAttrs.horPadding, 0, navAttrs.horPadding, 0)
        }

        addRightView(button, generateTextButtonLayoutParams())
        return button
    }

    fun addRightView(
        view: View,
        params: LayoutParams
    ) {
        val viewId = view.id
        if (viewId == View.NO_ID) {
            throw IllegalStateException("right view must has an unique id.")
        }
        if (mRightLastViewId == View.NO_ID) {
            params.addRule(ALIGN_PARENT_RIGHT, viewId)
        } else {
            params.addRule(LEFT_OF, mRightLastViewId)
        }
        mRightLastViewId = viewId
        params.alignWithParent = true  //alignParentIfMissing
        mRightViewList.add(view)
        addView(view, params)
    }

    private fun addLineView() {
        val view = View(context)
        val params = LayoutParams(LayoutParams.MATCH_PARENT, navAttrs.lineHeight)
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM)
        view.layoutParams = params
        view.setBackgroundColor(navAttrs.lineColor)
        addView(view)
    }

    fun setTitle(title: String) {
        ensureTitleView()
        titleView?.text = title
        titleView?.visibility = if (TextUtils.isEmpty(title)) View.GONE else View.VISIBLE
    }

    fun setSubtitle(subtitle: String) {
        ensureSubtitleView()
        updateTitleViewStyle()
        subtitleView?.text = subtitle
        subtitleView?.visibility = if (TextUtils.isEmpty(subtitle)) View.GONE else View.VISIBLE
    }

    fun setCenterView(view: View) {/*自定义中间view，可以为searchView等*/
        var params = view.layoutParams
        if (params == null) {
            params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        } else if (params !is LayoutParams) {
            params = LayoutParams(params)
        }

        val centerViewParams = params as LayoutParams
        centerViewParams.addRule(RIGHT_OF, mLeftLastViewId)
        centerViewParams.addRule(LEFT_OF, mRightLastViewId)
        params.addRule(CENTER_VERTICAL)
        addView(view, centerViewParams)
    }


    private fun ensureTitleView() {
        if (titleView == null) {
            titleView = IconFontTextView(context, null)
            titleView?.apply {
                gravity = Gravity.CENTER
                maxLines = 1
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttrs.titleTextColor)

                updateTitleViewStyle()
                ensureTitleContainer()
                titleContainer?.addView(titleView, 0)
            }
        }
    }

    private fun ensureSubtitleView() {
        if (subtitleView == null) {
            subtitleView = IconFontTextView(context, null)
            subtitleView?.apply {
                gravity = Gravity.CENTER
                /*isSingleLine = true废弃*/
                maxLines=1
                ellipsize = TextUtils.TruncateAt.END
                setTextColor(navAttrs.subTitleTextColor)
                textSize = navAttrs.subTitleSize

                //添加到titlecontainer
                ensureTitleContainer()
                titleContainer?.addView(subtitleView)
            }
        }
    }

    private fun ensureTitleContainer() {
        if (titleContainer == null) {
            titleContainer = LinearLayout(context)
            titleContainer?.apply {
                orientation = LinearLayout.VERTICAL
                gravity = Gravity.CENTER

                val params = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
                params.addRule(CENTER_IN_PARENT)
                this@HiNavigationBar.addView(titleContainer, params)
            }
        }
    }

    private fun updateTitleViewStyle() {

        if (titleView != null) {
            if (subtitleView == null || TextUtils.isEmpty(subtitleView!!.text)) {
                titleView?.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.titleTextSize)
                titleView?.typeface = Typeface.DEFAULT_BOLD
            } else {/*有副标题时，主标题不再粗体*/
                titleView?.setTextSize(
                    TypedValue.COMPLEX_UNIT_PX,
                    navAttrs.titleTextSizeWithSubTitle
                )
                titleView?.typeface = Typeface.DEFAULT
            }
        }
    }


    private fun generateTextButtonLayoutParams(): LayoutParams {
        return LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.MATCH_PARENT)
    }


    private fun generateTextButton(): Button {
        val button = IconFontButton(context)
        button.setBackgroundResource(0)
        button.minWidth = 0
        button.minimumWidth = 0
        button.minHeight = 0
        button.minHeight = 0
        button.setTextSize(TypedValue.COMPLEX_UNIT_PX, navAttrs.btnTextSize)
        button.setTextColor(navAttrs.btnTextColor)
        button.gravity = Gravity.CENTER
        button.includeFontPadding = false/**/
        return button
    }
    /*左右侧数量不等时的标题居中，或空间不足策略*/
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        if (titleContainer != null) {
            //计算出标题栏左侧已占用的空间
            var leftUseSpace = paddingLeft
            for (view in mLeftViewList) {
                leftUseSpace += view.measuredWidth
            }


            //计算出标题栏右侧已占用的空间
            var rightUseSpace = paddingRight
            for (view in mRightViewList) {
                rightUseSpace += view.measuredWidth
            }

            //这里只是他想要的宽度 500，300
            val titleContainerWidth = titleContainer!!.measuredWidth
            //为了让标题居中，左右空余距离一样
            val remainingSpace = measuredWidth - Math.max(leftUseSpace, rightUseSpace) * 2
            if (remainingSpace < titleContainerWidth) {
                val size =
                    MeasureSpec.makeMeasureSpec(remainingSpace, MeasureSpec.EXACTLY)
                titleContainer!!.measure(size, heightMeasureSpec)
            }
        }
    }

}