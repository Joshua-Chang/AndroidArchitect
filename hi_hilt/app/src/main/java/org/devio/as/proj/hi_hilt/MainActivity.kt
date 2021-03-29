package org.devio.`as`.proj.hi_hilt

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject
/*1、编译时生成Hilt_MainActivity入口类，负责创建ActivityComponet组件对象*/
/*2、编译时把父类从AppCompatActivity替换成Hilt_MainActivity*/
@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    @Inject lateinit var iLoginService:ILoginService
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)/*3、执行父类Hilt_MainActivity.onCreate()*/
        /*4、创建与Activity生命周期关联的HiltComponents_ActivityC组件*/
        /*5、调用injectHiMainActivity为MainActivity注入对象*/
        setContentView(R.layout.activity_main)
        /**
         * 问题：
         * 1、iLoginService何时创建
         * 1、iLoginServiceImpl(context)如何传递
         * 3、Singleton如何全局单例
         * */
        iLoginService.login()
    }
}