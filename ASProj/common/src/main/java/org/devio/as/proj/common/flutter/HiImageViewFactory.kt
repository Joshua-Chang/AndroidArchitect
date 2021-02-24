package org.devio.`as`.proj.common.flutter

import android.content.Context
import io.flutter.plugin.common.BinaryMessenger
import io.flutter.plugin.common.StandardMessageCodec
import io.flutter.plugin.platform.PlatformView
import io.flutter.plugin.platform.PlatformViewFactory


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/14 12:16 AM
 * @创建人：常守达
 * @备注：
 */
class HiImageViewFactory(private val messenger: BinaryMessenger):PlatformViewFactory(StandardMessageCodec.INSTANCE) {
    override fun create(context: Context?, viewId: Int, args: Any?): PlatformView {
        return HiImageViewController(context,messenger,viewId,args)
    }
}