package org.devio.`as`.proj.main.http

import android.content.Context
import android.os.Handler
import android.os.Looper
import android.widget.Toast
import androidx.lifecycle.Observer
import com.alibaba.android.arouter.facade.Postcard
import com.alibaba.android.arouter.facade.annotation.Interceptor
import com.alibaba.android.arouter.facade.callback.InterceptorCallback
import com.alibaba.android.arouter.facade.template.IInterceptor
import com.alibaba.android.arouter.launcher.ARouter
import org.devio.`as`.proj.main.biz.account.AccountManager
import org.devio.hi.library.util.MainHandler
import java.lang.RuntimeException

@Interceptor(name = "biz_interceptor", priority = 9)
class BizInterceptor : IInterceptor {
    private var context: Context? = null
    override fun init(context: Context?) {
        this.context = context;
    }

    override fun process(postcard: Postcard?, callback: InterceptorCallback?) {
        val flag = postcard!!.extra
        if ((flag and (RouteFlag.FLAG_LOGIN) != 0)) {
            callback!!.onInterrupt(RuntimeException("need login"))
            loginIntercept(postcard, callback)
        } else {
            callback!!.onContinue(postcard)
        }
    }

    private fun loginIntercept(postcard: Postcard?, callback: InterceptorCallback?) {
        MainHandler.post {
            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
            if (AccountManager.isLogin()) {
                callback?.onContinue(postcard)
            }else{
                 AccountManager.login(context, Observer {success->
                     callback?.onContinue(postcard)
                 })
            }
        }
//        Handler(Looper.getMainLooper()).post {
//            Toast.makeText(context, "请先登录", Toast.LENGTH_SHORT).show()
//            ARouter.getInstance().build("/account/login").navigation()
//        }
    }

}