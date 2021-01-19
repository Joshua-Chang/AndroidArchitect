package org.devio.hi.library.restful

import java.lang.reflect.Type

interface HiConvert {
    /**
     * dataType
     * @see HiResponse.data 返回数据类型
     */
    fun <T> convert(rawData:String,dataType:Type):HiResponse<T>
}