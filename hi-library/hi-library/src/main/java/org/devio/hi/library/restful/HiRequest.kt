package org.devio.hi.library.restful

import android.text.TextUtils
import androidx.annotation.IntDef
import org.devio.hi.library.restful.annotation.CacheStrategy
import java.lang.Exception
import java.lang.IllegalStateException
import java.lang.StringBuilder
import java.lang.reflect.Type
import java.net.URLEncoder

open class HiRequest {
    /**
     * 得到请求的完整路径
     */
    fun endPointUrl(): String {
        if (relativeUrl == null) {
            throw IllegalStateException("relative url must be null")
        }
        return if (!relativeUrl!!.startsWith("/")) {
            domainUrl + relativeUrl
        } else {
            val indexOf = domainUrl!!.indexOf("/")
            domainUrl!!.substring(0, indexOf) + relativeUrl
        }

    }

    fun addHeader(name: String, value: String) {
        if (headers == null) {
            headers = mutableMapOf()
        }
        headers!![name] = value
    }

    fun getCacheKey(): String {
        if (!TextUtils.isEmpty(cacheStrategyKey)) {
            return cacheStrategyKey
        }
        val builder = StringBuilder()
        val endUrl = endPointUrl()
        builder.append(endUrl)
        if (endUrl.indexOf("?") > 0 || endUrl.indexOf("&") > 0) {
            builder.append("&")
        } else {
            builder.append("?")
        }
        if (parameters != null) {
            for ((key, value) in parameters!!) {
                try {
                    val encodeValue = URLEncoder.encode(value, "UTF-8")
                    builder.append(key).append("=").append(encodeValue).append("&")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
            builder.deleteCharAt(builder.length - 1)/*去掉多余的&*/
            cacheStrategyKey = builder.toString()
        } else {
            cacheStrategyKey = endUrl
        }
        return cacheStrategyKey;
    }

    private var cacheStrategyKey: String = ""

    @METHOD
    var httpMethod: Int = 0

    var headers: MutableMap<String, String>? = null
    var parameters: MutableMap<String, String>? = null
    var domainUrl: String? = null
    var relativeUrl: String? = null
    var returnType: Type? = null
    var formPost: Boolean = true
    var cacheStrategy: Int = CacheStrategy.NET_ONLY

    @IntDef(value = [METHOD.GET, METHOD.POST])
    annotation class METHOD {
        companion object {
            const val GET = 0
            const val POST = 1
        }
    }
}