package org.devio.`as`.proj.common.flutter

import android.content.Context
import io.flutter.embedding.engine.loader.FlutterLoader
import java.io.File


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/14 3:17 PM
 * @创建人：常守达
 * @备注：
 */
class HiFlutterLoader : FlutterLoader() {
    companion object {
        const val FIX_SO = "libappfix.so"
        private var instance: HiFlutterLoader? = null
            get() {
                if (field == null) {
                    field = HiFlutterLoader()
                }
                return field
            }
        fun get():HiFlutterLoader{
            return instance!!
        }
    }

    override fun ensureInitializationComplete(
        applicationContext: Context,
        args: Array<out String>?
    ) {
        val path = applicationContext.filesDir
        val soFile = File(path, FIX_SO)
        if (soFile.exists()) {
            val field = FlutterLoader::class.java.getDeclaredField("aotSharedLibraryName")
            field.isAccessible=true
            field[this]=soFile.absolutePath
        }
        super.ensureInitializationComplete(applicationContext, args)
    }
}