package org.devio.hi.library.restful

interface HiCallback<T> {
    fun onSuccess(response: HiResponse<T>)
    fun onFailed(throwable: Throwable)
}