package org.devio.`as`.proj.common.rn

import android.view.View
import com.facebook.react.ReactPackage
import com.facebook.react.bridge.NativeModule
import com.facebook.react.bridge.ReactApplicationContext
import com.facebook.react.uimanager.ReactShadowNode
import com.facebook.react.uimanager.ViewManager


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/17 11:53 PM
 * @创建人：常守达
 * @备注：
 */
class HiReactPackage :ReactPackage{
    override fun createNativeModules(reactContext: ReactApplicationContext): MutableList<NativeModule> {
        val modules: MutableList<NativeModule> = ArrayList()
        modules.add(HiRNBridge(reactContext))
        return modules
    }

    override fun createViewManagers(reactContext: ReactApplicationContext): MutableList<ViewManager<View, ReactShadowNode<*>>> {
        return mutableListOf()
    }
}