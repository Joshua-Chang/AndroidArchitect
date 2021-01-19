package org.devio.`as`.proj.main.http.api

import com.alibaba.android.arouter.launcher.ARouter
import org.devio.hi.library.restful.HiInterceptor
import org.devio.hi.library.restful.HiResponse

/**
 * 根据response 的 code 自动路由到相关页面
 */
class HttpCodeInterceptor :HiInterceptor{
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        val response = chain.response()
        if (chain.isRequestPeriod&&response!=null) {
            when (response.code) {
                HiResponse.RC_NEED_LOGIN -> {
                    ARouter.getInstance().build("account/login").navigation()
                }
                //or a|b
                // ,
                (HiResponse.RC_AUTH_TOKEN_EXPIRED) , (HiResponse.RC_AUTH_TOKEN_INVALID), (HiResponse.RC_USER_FORBID)->{
                    var helpUrl:String?=null
                    if (response.errorData != null) {
                        helpUrl = response.errorData!!.get("helpUrl")
                    }
                    ARouter.getInstance().build("/degrade/global/activity")
                        .withString("degrade_title","非法访问")
                        .withString("degrade_desc",response.msg)
                        .withString("degrade_action",helpUrl)
                        .navigation()
                }
                else -> {
                }
            }
    }
        return false
    }
}