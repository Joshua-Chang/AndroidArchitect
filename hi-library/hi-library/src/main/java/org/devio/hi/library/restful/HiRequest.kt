package org.devio.hi.library.restful

import androidx.annotation.IntDef
import java.lang.IllegalStateException
import java.lang.reflect.Type

open class HiRequest {
    /**
     * 得到请求的完整路径
     */
    fun endPointUrl(): String {
        if (relativeUrl == null) {
            throw IllegalStateException("relative url must be null")
        }
        return if (!relativeUrl!!.startsWith("/")){
            domainUrl+relativeUrl
        }else{
            val indexOf = domainUrl!!.indexOf("/")
            domainUrl!!.substring(0,indexOf)+relativeUrl
        }

    }

    fun addHeader(name: String, value: String) {
        if (headers == null) {
            headers= mutableMapOf()
        }
        headers!![name]=value
    }

    @METHOD
    var httpMethod:Int=0
    var headers:MutableMap<String,String>?=null
    var parameters:MutableMap<String,String>?=null
    var domainUrl:String?=null
    var relativeUrl:String?=null
    var returnType:Type?=null
    var formPost:Boolean=true

    @IntDef(value = [METHOD.GET,METHOD.POST])
    annotation class METHOD{
        companion object{
            const val GET=0
            const val POST=1
        }
    }
}