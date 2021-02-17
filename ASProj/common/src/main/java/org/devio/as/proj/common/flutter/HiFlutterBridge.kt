package org.devio.`as`.proj.common.flutter

import com.alibaba.android.arouter.launcher.ARouter
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import org.devio.`as`.proj.common.core.IHiBridge
import org.devio.`as`.proj.common.info.HiLocalConfig
import org.devio.hi.library.util.ActivityManager


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/12 1:33 AM
 * @创建人：常守达
 * @备注：
 */
class HiFlutterBridge : IHiBridge<Any?, MethodChannel.Result?>, MethodChannel.MethodCallHandler {

    private var methodChannels = mutableListOf<MethodChannel>()

    companion object {
        @JvmStatic
        var instance: HiFlutterBridge? = null
            private set

        @JvmStatic
        fun init(flutterEngine: FlutterEngine): HiFlutterBridge? {
            val methodChannel = MethodChannel(flutterEngine.dartExecutor, "HiFlutterBridge")
            if (instance == null) {
                HiFlutterBridge().also {
                    instance = it
                }
            }
            methodChannel.setMethodCallHandler(instance)
            /*每个engine单独注册channel*/
            instance!!.apply { methodChannels.add(methodChannel) }
            return instance
        }
    }

    fun fire(method: String, arguments: Any?) {
        methodChannels.forEach {
            it.invokeMethod(method, arguments)
        }
    }

    fun fire(method: String, arguments: Any?, callBack: MethodChannel.Result) {
        methodChannels.forEach {
            it.invokeMethod(method, arguments, callBack)
        }
    }

    override fun onBack(p: Any?) {
        if (ActivityManager.instance.getTopActivity(true) is HiFlutterActivity) {
            (ActivityManager.instance.getTopActivity(true) as HiFlutterActivity).onBackPressed()
        }
    }

    override fun goToNative(p: Any?) {
        if (p is Map<*, *>) {
            val action = p["action"]
            if (action == "goToDetail") {
                val goodsId = p["goodsId"]
                ARouter.getInstance().build("/detail/main")
                    .withString("goodsId", goodsId as String?).navigation()
            }else if (action=="onBack"){

            }else if (action=="goToLogin"){
                ARouter.getInstance().build("/account/login").navigation()
            }
        }
    }

    override fun getHeaderParams(callBack: MethodChannel.Result?) {
        callBack!!.success(
            mapOf(
                "boarding-pass" to HiLocalConfig.instance!!.boardingPass(),
                "auth-token" to HiLocalConfig.instance!!.authToken(),
            )
        )
    }

    override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
        when (call.method) {
            "onBack" -> onBack(call.arguments)
            "getHeaderParams" -> getHeaderParams(result)
            "goToNative" -> goToNative(call.arguments)
            else -> result.notImplemented()
        }
    }
}