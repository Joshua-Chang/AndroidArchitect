package org.devio.`as`.proj.common.flutter

import android.content.Context
import android.os.Looper
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.view.FlutterMain


/**
 * @版本号：
 * @需求编号：
 * @功能描述：优化flutter 加载速度
 * @创建时间：2021/2/12 12:35 AM
 * @创建人：常守达
 * @备注：1、预加载 2、flutter 不同dart入口
 */
class HiFlutterCacheManager private constructor() {
    companion object {
        const val MODULE_NAME_FAVORITE = "main"
        const val MODULE_NAME_RECOMMEND = "recommend"

        @JvmStatic
        @get:Synchronized
        var instance: HiFlutterCacheManager? = null
            get() {
                if (field == null) {
                    field = HiFlutterCacheManager()
                }
                return field
            }
            private set
    }

    private fun initFlutterEngine(context: Context, moduleName: String): FlutterEngine {
        val flutterEngine = FlutterEngine(context)
        flutterEngine.dartExecutor.executeDartEntrypoint(
            DartExecutor.DartEntrypoint(
                FlutterMain.findAppBundlePath(),
                moduleName
            )
        )
        FlutterEngineCache.getInstance().put(moduleName, flutterEngine)
        return flutterEngine
    }

    fun preLoad(context: Context) {
        Looper.myQueue().addIdleHandler {
            initFlutterEngine(context, MODULE_NAME_FAVORITE)
            initFlutterEngine(context, MODULE_NAME_RECOMMEND)
            // TODO: 2021/2/12
            false
        }
    }
    fun getCachedFlutterEngine(moduleName: String,context: Context?):FlutterEngine{
        var flutterEngine = FlutterEngineCache.getInstance()[moduleName]
        if (flutterEngine == null&&context!=null) {
            flutterEngine=initFlutterEngine(context, moduleName)
        }
        return flutterEngine!! 
    }
}