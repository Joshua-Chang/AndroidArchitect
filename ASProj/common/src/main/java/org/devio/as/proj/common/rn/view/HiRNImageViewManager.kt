package org.devio.`as`.proj.common.rn.view

import androidx.annotation.Nullable
import com.facebook.react.common.MapBuilder
import com.facebook.react.uimanager.SimpleViewManager
import com.facebook.react.uimanager.ThemedReactContext
import com.facebook.react.uimanager.annotations.ReactProp


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/18 2:46 AM
 * @创建人：常守达
 * @备注：
 */
class HiRNImageViewManager() :
    SimpleViewManager<HiRNImageView>() {
    override fun getName(): String {
        return "HiRNImageView"/*在RN中使用时的名字*/
    }

    override fun createViewInstance(reactContext: ThemedReactContext): HiRNImageView {
        return HiRNImageView(context = reactContext)
    }

    @ReactProp(name = "src")/*在RN中使用时的属性为src*/
    fun setUrl(view: HiRNImageView, @Nullable sources: String?) {
        sources?.let {
            view.setUrl(url = it)
        }
    }

    /**
     * 要把事件名topChange映射到 JavaScript 端的onChange回调属性上
     * ，需要在你的ViewManager中覆盖getExportedCustomBubblingEventTypeConstants方法，并在其中进行注册：
     */
    override fun getExportedCustomBubblingEventTypeConstants(): MutableMap<String, Any> {
        //将Native 端的事件名：onNativeClick 映射给JS端的：onJSClick
        return MapBuilder.builder<String, Any>()
            .put(
                "onNativeClick",
                MapBuilder.of(
                    "phasedRegistrationNames",//固定值
                    MapBuilder.of("bubbled"/*固定值*/, "onJSClick")
                )//bubbled 固定值
            )
            .build();
    }
}