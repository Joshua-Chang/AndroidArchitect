package org.devio.`as`.porj.debug

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Process
import androidx.appcompat.app.AppCompatDelegate
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import org.devio.`as`.proj.ability.HiAbility
import org.devio.`as`.proj.ability.share.ShareBundle
import org.devio.`as`.proj.common.utils.SPUtil
import org.devio.hi.library.fps.FpsMonitor
import org.devio.hi.library.util.ActivityManager
import org.devio.hi.library.util.AppGlobals
import org.devio.hi.library.util.HiViewUtil
import java.io.ByteArrayOutputStream

class DebugTools {
    fun buildVersion(): String {
        return "构建版本:" + BuildConfig.VERSION_CODE + "." + BuildConfig.VERSION_CODE
    }

    fun buildTime(): String {
        //new date() 当前你在运行时拿到的时间，这个包，被打出来的时间
        return "构建时间：" + BuildConfig.BUILD_TIME
    }

    fun buildEnvironment(): String {
        return "构建环境: " + if (BuildConfig.DEBUG) "测试环境" else "正式环境"
    }

    fun buildDevice(): String {
        // 构建版本 ： 品牌-sdk-abi
        return "设备信息:" + Build.BRAND + "-" + Build.VERSION.SDK_INT + "-" + Build.CPU_ABI
    }

    @HiDebug(name = "一键开启Https降级", desc = "将继承Http,可以使用抓包工具明文抓包")
    fun degrade2Http() {
        SPUtil.putBoolean("degrade_http", true)
        val context = AppGlobals.get()?.applicationContext ?: return
        val intent =
            context.packageManager.getLaunchIntentForPackage(context.packageName)
        intent?.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)//applicationContext启动activity要加NEW_TASK
        context.startActivity(intent)

        //杀掉当前进程,并主动启动新的 启动页，以完成重启的动作
        Process.killProcess(Process.myPid())
    }

    @HiDebug(name = "查看Crash日志", desc = "可一键分享")
    fun crashLog() {
        val context = AppGlobals.get()?.applicationContext ?: return
        val intent = Intent(context, CrashLogActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(intent)
    }

    @HiDebug(name = "FPS", "实时FPS")
    fun toggleFps() {
        FpsMonitor.toggle()
    }

    @HiDebug(name = "暗黑模式", "")
    fun toggleTheme() {
        if (HiViewUtil.lightMode()) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    @HiDebug(name = "分享到QQ好友", desc = "微信分享请查看feature/wx_share分支代码")
    fun share2QQFriend(context: Context) {

        val shareBundle = ShareBundle()
        shareBundle.title = "测试分享title"
        shareBundle.summary = "测试分享summary"
        shareBundle.appName = "好物App"
        shareBundle.targetUrl = "https://class.imooc.com/sale/mobilearchitect"
        shareBundle.thumbUrl = "https://img2.sycdn.imooc.com/5ece134c0001d50a02400180.jpg"
        shareBundle.channels = listOf("发送给朋友", "添加到微信收藏", "发送给好友", "发送到朋友圈")
        Glide.with(AppGlobals.get()!!).asBitmap().load(shareBundle.thumbUrl).override(100)
            .into(object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bos = ByteArrayOutputStream()
                    resource.compress(Bitmap.CompressFormat.JPEG,100,bos)
                    val byteArray = bos.toByteArray()
                    shareBundle.thumbData=byteArray

                }
            })
        // 微信分享请查看feature/wx_share分支代码
        // 微信分享请查看feature/wx_share分支代码
        // 微信分享请查看feature/wx_share分支代码
        val topActivity = ActivityManager.instance.getTopActivity(true)
        topActivity?.apply {
            HiAbility.share(topActivity, shareBundle)
        }
    }
    @HiDebug(name = "WX", desc = "微信分享请查看feature/wx_share分支代码")
    fun share2WX(context: Context) {

        val shareBundle = ShareBundle()
        shareBundle.title = "测试分享title"
        shareBundle.summary = "测试分享summary"
        shareBundle.appName = "好物App"
        shareBundle.targetUrl = "https://class.imooc.com/sale/mobilearchitect"
        shareBundle.thumbUrl = "https://img2.sycdn.imooc.com/5ece134c0001d50a02400180.jpg"
        shareBundle.channels = listOf("发送给朋友", "添加到微信收藏", "发送给好友", "发送到朋友圈")
        Glide.with(AppGlobals.get()!!).asBitmap().load(shareBundle.thumbUrl).override(100)
            .into(object: SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bos = ByteArrayOutputStream()
                    resource.compress(Bitmap.CompressFormat.JPEG,100,bos)
                    val byteArray = bos.toByteArray()
                    shareBundle.thumbData=byteArray
                    val topActivity = ActivityManager.instance.getTopActivity(true)
                    topActivity?.apply {
                        HiAbility.share(topActivity, shareBundle)
                    }
                }
            })
        // 微信分享请查看feature/wx_share分支代码
        // 微信分享请查看feature/wx_share分支代码
        // 微信分享请查看feature/wx_share分支代码

    }
}