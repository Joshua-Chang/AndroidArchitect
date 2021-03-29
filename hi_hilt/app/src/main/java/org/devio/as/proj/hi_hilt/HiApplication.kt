package org.devio.`as`.proj.hi_hilt

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
/*1、编译时生成Hilt_HiApplication入口类，负责创建applicationComponet组件对象*/
/*2、编译时把父类从Application替换成Hilt_HiApplication*/
@HiltAndroidApp
class HiApplication: Application() {
    override fun onCreate() {
        super.onCreate()/*3、执行父类Hilt_HiApplication.onCreate()*/
        /*4、创建与Application生命周期关联的HiltComponents_HiApplicationC组件*/
        /*5、调用injectHiApplication为Application注入对象*/
    }
}