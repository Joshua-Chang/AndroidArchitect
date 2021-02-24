package org.devio.`as`.proj.common.rn

import com.alibaba.android.arouter.launcher.ARouter
import com.facebook.react.bridge.*
import org.devio.`as`.proj.common.core.IHiBridge
import org.devio.`as`.proj.common.info.HiLocalConfig


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/17 11:40 PM
 * @创建人：常守达
 * @备注：
 */
class HiRNBridge(reactContext: ReactApplicationContext?) :
    ReactContextBaseJavaModule(reactContext), IHiBridge<ReadableMap, Promise> {
    override fun getName(): String {
        return "HiRNBridge"
    }

    @ReactMethod/*暴露给RN的方法，名称必须对应*/
    override fun onBack(p: ReadableMap?) {
        currentActivity?.run {
            runOnUiThread {
                onBackPressed()
            }
        }
    }

    @ReactMethod
    override fun goToNative(p: ReadableMap) {
        ARouter.getInstance().build("/detail/main").withString("goodsId", p.getString("goodsId"))
            .navigation()
    }

    @ReactMethod
    override fun getHeaderParams(callBack: Promise) {
        val params = Arguments.createMap()
        params.putString("boarding-pass", HiLocalConfig.instance!!.boardingPass())
        params.putString("auth-token", HiLocalConfig.instance!!.authToken())
        callBack.resolve(params)
    }
}