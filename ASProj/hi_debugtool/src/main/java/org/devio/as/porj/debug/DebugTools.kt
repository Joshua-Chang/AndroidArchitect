package org.devio.`as`.porj.debug

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.os.Build
import android.os.Process
import android.text.TextUtils
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatDelegate
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.SimpleTarget
import com.bumptech.glide.request.transition.Transition
import org.devio.`as`.proj.ability.HiAbility
import org.devio.`as`.proj.ability.pay.alipay.AliPayOrderInfoUtil
import org.devio.`as`.proj.ability.share.ShareBundle
import org.devio.`as`.proj.common.ext.showToast
import org.devio.`as`.proj.common.utils.SPUtil
import org.devio.hi.library.fps.FpsMonitor
import org.devio.hi.library.log.HiLog
import org.devio.hi.library.util.ActivityManager
import org.devio.hi.library.util.AppGlobals
import org.devio.hi.library.util.HiViewUtil
import org.devio.hi.library.util.MainHandler
import java.io.ByteArrayOutputStream
import java.text.SimpleDateFormat
import java.util.*

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
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bos = ByteArrayOutputStream()
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    val byteArray = bos.toByteArray()
                    shareBundle.thumbData = byteArray

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
            .into(object : SimpleTarget<Bitmap>() {
                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                    val bos = ByteArrayOutputStream()
                    resource.compress(Bitmap.CompressFormat.JPEG, 100, bos)
                    val byteArray = bos.toByteArray()
                    shareBundle.thumbData = byteArray
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

    @HiDebug(name = "支付宝支付", desc = "订单信息正常情况下需要服务端拼接处理好返回orderInfo字符串即可")
    fun aliPay(context: Activity) {
        /** 最好由服务端处理
         * app_id=2021002112624068
         * &biz_content={"timeout_express":"30m","seller_id":"","product_code":"QUICK_MSECURITY_PAY","total_amount":"0.02","subject":"1","body":"我是测试数据","out_trade_no":"314VYGIAGG7ZOYY"}
         * &charset=utf-8
         * &method=alipay.trade.app.pay
         * &sign_type=RSA2
         * &timestamp=2016-08-15 12:12:15
         * &version=1.0
         * &sign=MsbylYkCzlfYLy9PeRwUUIg9nZPeN9SfXPNavUCroGKR5Kqvx0nEnd3eRmKxJuthNUx4ERCXe552EV9PfwexqW+1wbKOdYtDIb4+7PL3Pc94RZL0zKaWcaY3tSL89/uAVUsQuFqEJdhIukuKygrXucvejOUgTCfoUdwTi7z+ZzQ=
         */
        //1.月份的错误2021-08-15 12:12:15
        val df = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
        val timeStamp = df.format(Date())
        //2 .Hashmap --顺序会散乱--TreeMap
        val parameters = TreeMap<String, String>()
        parameters.put("app_id", "2021002112624068")
        parameters.put(
            "biz_content",
            "{\"timeout_express\":\"30m\",\"product_code\":\"QUICK_MSECURITY_PAY\",\"total_amount\":\"0.01\",\"subject\":\"1\",\"body\":\"我是测试数据\",\"out_trade_no\":\"" + AliPayOrderInfoUtil.getOutTradeNo() + "\"}"
        )
        parameters.put("charset", "utf-8")
        parameters.put("method", "alipay.trade.app.pay")
        parameters.put("sign_type", "RSA2")
        parameters.put("timestamp", timeStamp)
        parameters.put("version", "1.0")
        //1 .sign字段不要拼接进去
        //2. key首字符升序排序

        //3.把map 中的key转换成string orderInfo,这里的value 需要encode
        val orderInfoUnsign = AliPayOrderInfoUtil.buildOrderParam(parameters)
        //4. 用应用的私钥对map 中的key-value进行加签，value 是不需要encode
        val sign = AliPayOrderInfoUtil.getSign(
            parameters,
            "MIIEvQIBADANBgkqhkiG9w0BAQEFAASCBKcwggSjAgEAAoIBAQCtnXVvSfoIiIVYeAeeggwll7M9dT6IvI4Y0y/oxvIRpVdj3oKEBt9fw5pAM58wB7i71uKIq7azcytOdSWbPk7GrFLM4UeUn74rj2ZZQ6dL0Bf7W7xsB7sM4eK+CGvCyw2KUHk0Qz9d0IgsGnYQ4yR19nk/j5hLcko8NWAOJdoIeXkKQX2A+FVmIw/s5Dxk63V1kCKBoR85vbqipJ28ldfhTPKYbMMIs+uK6ge/FmnNUezBMzzyHIAm9wr5z2A86sORAP6YFSP+KQXFR7HmI6qpmlT+pUvYc6MKsWPUKty4u06FuAQKakclHCbSMbaITE9z7Re6rv5hK+cT8t1L+kb7AgMBAAECggEBAJ+7JrhlT21abZSfmiLzc0mC8J2yqP4UvIeKhaGcqEoj3Os9ZMcjq4/n3psC44rFD+mCIpmrokQnycg9b4QNJ10SXnuxKLJM5iIdpQSrgdf2dJoGJK0fz7BigsJj7QEi4CpJL7otxLcDjMVG8ylGUd1WDuezrWJ8nGAYm29o09jGpewDJrgKG6a0hZgb7G7iFYsfVRF/ByOO0+MkGJv+K4RcBKYkxED8W44E7HXR1rC+LeT7S9Y45JDIjmWbrmPHYQxnxxNaO30Lfu2y1WTt1/cv44Ogpil8EDux6ROkck50VQHpL981xlzKxjuFvagYkNEJLduvC86XH8WT4rNxboECgYEA1MaDuUoiSxmqhqxoBOzvI4pQVsUL9B6Hv07t/YzmVOG2TIuPA67dMVBOse5R7a4WYyBgg4rHsqLe/2u7OnSPrxMzbsVFZ0CkITbPoKmER881KEM0lOL7yt+QRZQR4jhCL7r/+RC4TtWIrwABoH3oStdK35TxkoeMRYjqcPi/tmkCgYEA0OJkDSAKozWLEC2gtelNPdtmhBltfmEe/UEzvVzMHOunDMDpqMXzAG0T7+gTRCtfI7etUw7up+ilExG4oHgjWw5kSYBE4983q/boAWA0eZ9uZuaPI/Ip/NGNct6XpUT4i47G6Y0mjQCIapTlj2jStWWZxDxObF6e/h0jW1hXDcMCgYBtXqkg2hlsjwHXfdFuDrgRhawsHdc6IWNXk+PDO+S9ZtQE3ouhsgWirov06/KTALQjOeZCV1i02wFTO3Ye4wFkVbNPUfmNzG1RYVIyXFBRdRH24yypB0+0BVysVyMmLBLhY/bleJG0fBBpG9C6RV3c8nNbH71Yu3H/4BPaBBLPWQKBgGP4BVQ2woCLzosmmySUjHAPQcf0928lghRIkT+OWu14ZpF2/hzU59CLY6BMnHPiofzRGjWHDvbzI5W1xHugI6OfhqWaqt235cf6Odz2swaGI+tBar4UVr31eFHCcCcblCwOX+pEsG+JEsCXlCoHS9ie0HGXPx0kR8lsHpDH/ZnLAoGAIIJ9vEXjBlRpCI/WzqSl29BtgKP7KNKekn+E3PkuXeBU/piIR/d3SXWsI43jgMDImFZdq8LJ6diXT5E4/0A8MLxBYu71CI/DQZhVRTUYhc1zlSwDPjSW0M/KEKwWD12ZvAZjaqFDwvxEIq+Jj7c3peC4a1SABTCoVTzHnlP9rF8="
        )
        val orderInfo = "$orderInfoUnsign&sign=$sign"
        HiAbility.aliPay(context, orderInfo) {
            HiLog.e(it)

            MainHandler.post(Runnable {
                if (TextUtils.equals(it.resultStatus, "9000")) {
                    it.resultInfo?.apply { showToast(this) }
                } else {
                    it.memo?.apply {
                        showToast(this)
                    }
                }
            })
        }
    }

    @HiDebug(name = "WeChat支付", desc = "wechat")
    fun wxPay(activity: Activity) {
        /*请求接口得到一下数据*/
        val map = HashMap<String, String>()
        map["appId"] = "dklafdfadsfas"
        map["partnerid"] = "dklafdfadsfas"
        map["package"] = "dklafdfadsfas"
        map["noncestr"] = "dklafdfadsfas"
        map["timestamp"] = "dklafdfadsfas"
        map["prepayid"] = "dklafdfadsfas"
        map["partnerid"] = "dklafdfadsfas"
        map["sign"] = "dklafdfadsfas"
        HiAbility.wxPay(activity, map, Observer { success ->
            if (success == null) return@Observer
            /*0成功 -1错误 -2用户取消*/
            showToast(if (success == 0) "支付成功" else "支付失败")
            val builder = AlertDialog.Builder(activity)
            builder.setTitle("支付结果")
            builder.setMessage(String.format("微信支付结果：%s", success))
            builder.show()
        })
    }
}