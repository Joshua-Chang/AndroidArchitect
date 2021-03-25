package org.devio.`as`.proj.main.degrade

import org.devio.`as`.proj.common.utils.SPUtil
import org.devio.hi.library.log.HiLog
import org.devio.hi.library.restful.HiInterceptor

class BizInterceptor :HiInterceptor{
    override fun intercept(chain: HiInterceptor.Chain): Boolean {
        if (chain.isRequestPeriod) {
            val request = chain.request()
            val boardingPass = SPUtil.getString("boarding-pass")?:""

            request.addHeader("boarding-pass", boardingPass)// TODO: 2021/2/3 在accountManager中获取
            request.addHeader("auth-token","fd82d1e882462e23b8e88aa82198f197")
        }else if (chain.response()!=null){
            HiLog.dt("BizInterceptor",chain.request().endPointUrl())
            HiLog.dt("BizInterceptor",chain.response()!!.rawData)
        }
        return false //不拦截
    }
}