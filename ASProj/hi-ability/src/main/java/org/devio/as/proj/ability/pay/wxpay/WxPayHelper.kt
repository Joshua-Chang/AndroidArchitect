package org.devio.`as`.proj.ability.pay.wxpay

import com.tencent.mm.opensdk.modelpay.PayReq
import com.tencent.mm.opensdk.openapi.WXAPIFactory
import org.devio.`as`.proj.ability.BuildConfig
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.util.AppGlobals

object WxPayHelper {
    private val api = WXAPIFactory.createWXAPI(AppGlobals.get(), BuildConfig.WX_PAY_APP_KEY, true)
    init {
        api.registerApp(BuildConfig.WX_PAY_APP_ID)
    }
    fun pay(parameters: HashMap<String, String>) {
        val req = PayReq()
        req.appId=parameters["appId"]
        req.partnerId=parameters["partnerid"]
        req.prepayId=parameters["prepayid"]
        req.nonceStr=parameters["noncestr"]
        req.timeStamp=parameters["timestamp"]
        req.packageValue=parameters["package"]
        req.sign=parameters["package"]
        req.signType=parameters["sign"]
        req.extData=parameters["extData"]
        HiExecutor.execute(runnable = Runnable {
            api.sendReq(req)
        })
    }
}