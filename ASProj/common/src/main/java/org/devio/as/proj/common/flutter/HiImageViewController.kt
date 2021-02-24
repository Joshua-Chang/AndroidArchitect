package org.devio.`as`.proj.common.flutter

import android.content.Context
import android.view.View
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.platform.PlatformView


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/14 12:12 AM
 * @创建人：常守达
 * @备注：
 */
class HiImageViewController(context: Context?, messenger: BinaryMessenger, id: Int?, args: Any?) :
    PlatformView, MethodChannel.MethodCallHandler {
    private val imageView: HiImageView = HiImageView(context!!)
    private val methodChannel: MethodChannel

    init {
        methodChannel = MethodChannel(messenger, "HiImageView_$id")
        methodChannel.setMethodCallHandler(this)
        if (args is Map<*, *>) {
            imageView.setUrl(args["url"] as String)
        }
    }

    override fun getView(): View {
        return imageView
    }

    override fun dispose() {
        /*资源释放*/
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "setUrl" -> {
                val url = call.argument<String>("url")
                if (url != null) {
                    imageView.setUrl(url)
                    result.success("setUrl success")
                } else {
                    result.error("-1", "url cannot be null", null)
                }
            }
            else -> result.notImplemented()
        }
    }
}