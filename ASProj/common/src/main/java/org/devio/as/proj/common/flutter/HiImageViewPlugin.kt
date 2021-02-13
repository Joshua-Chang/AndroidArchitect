package org.devio.`as`.proj.common.flutter

import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.plugins.shim.ShimPluginRegistry
import io.flutter.plugin.common.PluginRegistry


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/14 12:20 AM
 * @创建人：常守达
 * @备注：
 */
object HiImageViewPlugin {
    fun registerWith(registrar: PluginRegistry.Registrar) {
        val viewFactory = HiImageViewFactory(registrar.messenger())
        registrar.platformViewRegistry().registerViewFactory("HiImageView", viewFactory)
    }

    fun registerWith(flutterEngine: FlutterEngine) {
        val shimPluginRegistry = ShimPluginRegistry(flutterEngine)
        registerWith(shimPluginRegistry.registrarFor("HiFlutter"))
    }
}