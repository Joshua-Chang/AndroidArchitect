package org.devio.hi.library.restful

interface HiCallback<T> {
    fun onSuccess(response: HiResponse<T>)
    fun onFailed(throwable: Throwable){}/*添加方法体后，实现类不绝对要求实现，想复写就复写*/
}