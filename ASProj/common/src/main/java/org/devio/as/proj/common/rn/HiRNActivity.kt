package org.devio.`as`.proj.common.rn

import android.graphics.Color
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.bridge.WritableMap
import com.facebook.react.bridge.WritableNativeMap
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.modules.core.DeviceEventManagerModule
import com.facebook.react.shell.MainReactPackage
import org.devio.`as`.proj.common.BuildConfig
import org.devio.`as`.proj.common.flutter.HiFlutterCacheManager
import org.devio.hi.library.log.HiConsolePrinter
import org.devio.hi.library.util.HiStatusBar


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/17 3:19 PM
 * @创建人：常守达
 * @备注：
 */
@Route(path = "/rn/main")
class HiRNActivity : AppCompatActivity(), DefaultHardwareBackBtnHandler {
    private var mReactRootView: ReactRootView? = null
    private var mReactInstanceManager: ReactInstanceManager? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        //预加载后，RN对StatusBar调用会失效
        HiStatusBar.setStatusBar(
            this,
            true,
            statusBarColor = Color.TRANSPARENT,
            enableTranslucent = true
        )
        super.onCreate(savedInstanceState)
        initRN()
        fireEvent()
        setContentView(mReactRootView)
    }
    /*向js层发送事件*/
    private fun fireEvent() {
        val event: WritableMap = WritableNativeMap()/*向js层传递的数据*/
        event.putString("method", "onStart")
        mReactRootView?.fireEvent("HI_RN_EVENT",event)
    }

    private fun initRN() {
        val routeTo = intent.getStringExtra(HI_RN_BUNDLE)
        val params = Bundle()
        params.putString("routeTo", routeTo)
//        mReactRootView = ReactRootView(this)
//        mReactInstanceManager = ReactInstanceManager.builder()
//            .setApplication(application)
//            .setCurrentActivity(this)
//            .setBundleAssetName("index.android.bundle")/*jsBundle*/
//            .setJSMainModulePath("index")
//            .addPackage(MainReactPackage())/*注册*/
//            .addPackage(HiReactPackage())
//            .setUseDeveloperSupport(BuildConfig.DEBUG)
//            .setInitialLifecycleState(LifecycleState.RESUMED)
//            .build()
////        mReactRootView?.startReactApplication(mReactInstanceManager, routeTo)
//        mReactRootView?.startReactApplication(mReactInstanceManager, "rn_module",params)
        mReactRootView = HiRNCacheManager.instance?.getCachedReactRootView(
            this,
            routeTo!!,
            params
        )
        mReactInstanceManager = mReactRootView!!.reactInstanceManager
    }

    override fun onPause() {
        super.onPause()
        mReactInstanceManager?.onHostPause(this)
    }

    override fun onResume() {
        super.onResume()
        mReactInstanceManager?.onHostResume(this, this)
    }

    override fun onDestroy() {
        super.onDestroy()
        mReactInstanceManager?.onHostDestroy(this)
        //为了提高下次打开时的体验，不去detachRootView，防止当第二次获取RN缓存时不出现空白页
//        mReactRootView?.unmountReactApplication()/*卸载*/
//        HiRNCacheManager.instance?.destroy(HiRNCacheManager.MODULE_NAME)
    }

    override fun onBackPressed() {
        if (mReactInstanceManager != null) {/*透传到RN处理*/
            mReactInstanceManager?.onBackPressed()
        } else {/*Android处理*/
            super.onBackPressed()
        }
    }

    override fun invokeDefaultOnBackPressed() {/*onResume*/
        super.onBackPressed()
    }

    override fun onKeyUp(keyCode: Int, event: KeyEvent?): Boolean {
        /*RN作为插件时也能cmd+M调试*/
        if (keyCode == KeyEvent.KEYCODE_MENU && mReactInstanceManager != null) {
            mReactInstanceManager!!.showDevOptionsDialog()
            return true
        }
        return super.onKeyUp(keyCode, event)
    }

    companion object {
        const val HI_RN_BUNDLE = "HI_RN_BUNDLE"
    }
}