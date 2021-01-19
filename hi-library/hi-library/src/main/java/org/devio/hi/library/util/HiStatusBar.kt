package org.devio.hi.library.util

import android.app.Activity
import android.graphics.Color
import android.os.Build
import android.view.View
import android.view.WindowManager

object HiStatusBar {
    /**
     *darkContent true:意味着 白底黑字， false:黑底白字
     *
     * statusBarColor  状态栏的背景色
     *
     * translucent  沉浸式效果，也就是页面的布局延伸到状态栏之下
     */
    fun setStatusBar(
        activity: Activity,
        isDarkContent: Boolean,
        statusBarColor: Int = Color.WHITE,
        enableTranslucent: Boolean
    ) {
        val window = activity.window
        val decorView = window.decorView
        var visibility = decorView.systemUiVisibility

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            //这俩flag互斥，不能同时出现
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
            window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
            window.statusBarColor = statusBarColor//设置状态栏颜色前 需先添加兼容flag：1、绘制状态栏背景2、非半透明
        }
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            visibility=if (isDarkContent){
                //白底黑字--浅色主题
                visibility or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            }else{
                //黑底白字--深色主题
                // java  visibility &= ~ View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
                visibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            }
        }
        if (enableTranslucent) {
            //FULLSCREEN此时能够使得页面的布局延伸到状态栏之下，但是状图兰的图标 也看不见了
            //搭配LAYOUT_STABLE使得状图兰的图标 恢复可见性
            visibility =
                visibility or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
        }

        decorView.systemUiVisibility = visibility
    }
}