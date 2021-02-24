package org.devio.`as`.proj.common.rn

import com.facebook.react.ReactRootView
import com.facebook.react.bridge.WritableMap
import com.facebook.react.modules.core.DeviceEventManagerModule


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/18 2:22 AM
 * @创建人：常守达
 * @备注：
 */
fun ReactRootView.fireEvent(eventName:String,params:WritableMap?){
    reactInstanceManager?.currentReactContext?.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter::class.java)
        ?.emit(eventName,params)
}