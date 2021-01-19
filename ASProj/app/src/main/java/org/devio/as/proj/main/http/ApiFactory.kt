package org.devio.`as`.proj.main.http

import org.devio.`as`.proj.main.http.api.HttpCodeInterceptor
import org.devio.hi.library.restful.HiRestful
import org.devio.`as`.proj.common.http.BizInterceptor
import org.devio.`as`.proj.common.utils.SPUtil

object ApiFactory {
    val KEY_DEGRADE_HTTP = "degrade_http"
//    val HTTPS_BASE_URL = "https://api.devio.org/as/"
//    val HTTPS_BASE_URL = "http://127.0.0.1:8080/as/"
    val HTTPS_BASE_URL = "http://10.0.2.2:8080/as/"
    /*模拟器默认把127.0.0.1和localhost当做本身了，在模拟器上可以用10.0.2.2代替127.0.0.1和localhost，另外如果是在局域网环境可以用 192.168.0.x或者192.168.1.x*/
//    val HTTP_BASE_URL = "http://api.devio.org/as/"
    val HTTP_BASE_URL = "http://127.0.0.1:8080/as/"
    val degrade2Http = SPUtil.getBoolean(KEY_DEGRADE_HTTP)
    val baseUrl = if (degrade2Http) HTTP_BASE_URL else HTTPS_BASE_URL

    private val hiRestful: HiRestful = HiRestful(baseUrl, RetrofitCallFactory(baseUrl))
    init {
        hiRestful.addInterceptor(BizInterceptor())
        hiRestful.addInterceptor(HttpCodeInterceptor())
        SPUtil.putBoolean(KEY_DEGRADE_HTTP, false)//还原成https，降级成http只针对本次有效
    }

    fun <T> create(service: Class<T>): T {
        return hiRestful.create(service)
    }
}