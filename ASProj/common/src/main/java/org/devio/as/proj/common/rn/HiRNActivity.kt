package org.devio.`as`.proj.common.rn

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.alibaba.android.arouter.facade.annotation.Route
import com.facebook.react.ReactInstanceManager
import com.facebook.react.ReactRootView
import com.facebook.react.common.LifecycleState
import com.facebook.react.modules.core.DefaultHardwareBackBtnHandler
import com.facebook.react.shell.MainReactPackage
import org.devio.`as`.proj.common.BuildConfig


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
        super.onCreate(savedInstanceState)
        initRN()
        setContentView(mReactRootView)
    }

    private fun initRN() {
        mReactRootView = ReactRootView(this)
        mReactInstanceManager = ReactInstanceManager.builder()
            .setApplication(application)
            .setCurrentActivity(this)
            .setBundleAssetName("index.android.bundle")/*jsBundle*/
            .setJSMainModulePath("index")
            .addPackage(MainReactPackage())/*注册*/
            .addPackage(HiReactPackage())
            .setUseDeveloperSupport(BuildConfig.DEBUG)
            .setInitialLifecycleState(LifecycleState.RESUMED)
            .build()
        mReactRootView?.startReactApplication(mReactInstanceManager, "rn_module")
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
}