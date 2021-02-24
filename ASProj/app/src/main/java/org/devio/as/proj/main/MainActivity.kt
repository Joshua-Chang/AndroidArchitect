package org.devio.`as`.proj.main

import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.fragment.app.DialogFragment
import com.google.gson.JsonObject
import org.devio.`as`.proj.common.rn.HiRNCacheManager
import org.devio.`as`.proj.common.ui.component.HiBaseActivity
import org.devio.`as`.proj.main.biz.LoginActivity
import org.devio.`as`.proj.main.http.ApiFactory
import org.devio.`as`.proj.main.http.api.AccountApi
import org.devio.`as`.proj.main.logic.MainActivityLogic
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiResponse
import org.devio.hi.library.util.HiDataBus
import org.devio.hi.library.util.HiStatusBar
import org.devio.hi.library.util.HiViewUtil

class MainActivity : HiBaseActivity(), MainActivityLogic.ActivityProvider {
    private var activityLogic : MainActivityLogic? =null
    override fun onCreate(savedInstanceState: Bundle?) {
        setTheme(R.style.Theme_ASProj)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        activityLogic= MainActivityLogic(this, savedInstanceState)
        HiStatusBar.setStatusBar(this,true,Color.WHITE,false)
        HiDataBus.with<String>("stickyData").setStickyData("stickyData from mainActivity")
        preLoadRN()
    }

    private fun preLoadRN() {/*在原生界面 预见去加载哪个RN界面*/
        val bundle = Bundle()
        bundle.putString("routeTo","/browsing")
        HiRNCacheManager.instance?.preLoad(this,HiRNCacheManager.MODULE_NAME_BROWSING,bundle)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        activityLogic?.onSaveInstanceState(outState)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val fragments = supportFragmentManager.fragments
        fragments.forEach {
            it.onActivityResult(requestCode,resultCode, data)
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode==KeyEvent.KEYCODE_VOLUME_DOWN) {
            if (BuildConfig.DEBUG){
                val aClass = Class.forName("org.devio.as.porj.debug.DebugToolDialogFragment")
                val target:DialogFragment = aClass.getConstructor().newInstance() as DialogFragment
                target.show(supportFragmentManager,"debug_tool")
            }
        }
        return super.onKeyDown(keyCode, event)
    }

    override fun onConfigurationChanged(newConfig: Configuration) {
        super.onConfigurationChanged(newConfig)
        /*折叠屏底部按钮宽度*/
        activityLogic?.hiTabBottomLayout?.resizeHiTabBottomLayout()


        /*浅深色主题 manifest 内设置UImode*/
        /*.getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK*/
        if (HiViewUtil.lightMode()) {
            recreate()
        }else{
            recreate()
        }
    }
}