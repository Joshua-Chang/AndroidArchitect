package org.devio.`as`.proj.common.flutter

import android.content.Context
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import io.flutter.embedding.android.FlutterTextureView
import io.flutter.embedding.android.FlutterView
import io.flutter.embedding.engine.FlutterEngine
import io.flutter.embedding.engine.FlutterEngineCache
import io.flutter.embedding.engine.dart.DartExecutor
import io.flutter.plugin.common.MethodChannel
import kotlinx.android.synthetic.main.fragment_flutter.*
import org.devio.`as`.proj.common.R
import org.devio.`as`.proj.common.ui.component.HiBaseFragment
import org.devio.`as`.proj.common.utils.AppGlobals


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/11 11:57 PM
 * @创建人：常守达
 * @备注：
 */
abstract class HiFlutterFragment(moduleName: String) : HiBaseFragment() {
    protected var flutterEngine: FlutterEngine?
    protected var flutterView: FlutterView? = null
    private val cached: Boolean = HiFlutterCacheManager.instance!!.hasCached(moduleName)

    init {
        flutterEngine = HiFlutterCacheManager.instance!!.getCachedFlutterEngine(
            moduleName,
            AppGlobals.application
        )
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (!cached) {/*注册flutter/platform_views 使插件能够处理native View*/
            flutterEngine?.platformViewsController?.attach(
                activity,
                flutterEngine!!.renderer,
                flutterEngine!!.dartExecutor
            )
        }
    }
    override fun getPageName(): String {
        return "HiFlutterFragment"
    }
    override fun getLayoutId(): Int {
        return R.layout.fragment_flutter
    }

    fun setTitle(titleStr: String) {
        rl_title.visibility=View.VISIBLE
        title_line.visibility=View.VISIBLE
        title.text = titleStr
        title.setOnClickListener {
            HiFlutterBridge.instance!!.fire("onRefresh", "so easy", object : MethodChannel.Result {
                override fun success(result: Any?) {
                    Toast.makeText(context, result as String?, Toast.LENGTH_SHORT).show()
                }

                override fun error(errorCode: String?, errorMessage: String?, errorDetails: Any?) {
                    Toast.makeText(context, errorMessage, Toast.LENGTH_SHORT).show()
                }

                override fun notImplemented() {
                }
            })
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (layoutView as ViewGroup).addView(createFlutterView(activity!!))
    }

    private fun createFlutterView(context: Context): FlutterView {
        val flutterTextureView = FlutterTextureView(activity!!)
        flutterView = FlutterView(context, flutterTextureView)
        return flutterView!!
    }

    override fun onStart() {
        flutterView!!.attachToFlutterEngine(flutterEngine!!)
        super.onStart()
    }

    override fun onResume() {
        super.onResume()
        flutterEngine!!.lifecycleChannel.appIsResumed()/*flutter>=1.17*/
    }

    override fun onPause() {
        super.onPause()
        flutterEngine!!.lifecycleChannel.appIsInactive()
    }

    override fun onStop() {
        super.onStop()
        flutterEngine!!.lifecycleChannel.appIsPaused()
    }

    override fun onDetach() {
        super.onDetach()
        flutterEngine!!.lifecycleChannel.appIsDetached()
    }

    override fun onDestroy() {
        super.onDestroy()
        flutterView?.detachFromFlutterEngine()
    }
}