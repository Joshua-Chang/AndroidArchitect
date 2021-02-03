package org.devio.hi.library.restful

import org.devio.hi.library.cache.HiStorage
import org.devio.hi.library.executor.HiExecutor
import org.devio.hi.library.log.HiLog
import org.devio.hi.library.restful.annotation.CacheStrategy
import org.devio.hi.library.util.MainHandler

/**
 * 代理CallFactory创建出来的call对象，从而实现拦截器的派发动作
 */
class Scheduler(val callFactory: HiCall.Factory, val interceptors: MutableList<HiInterceptor>) {
    fun newCall(request: HiRequest): HiCall<*> {
        val newCall: HiCall<*> = callFactory.newCall(request)
        return ProxyCall(newCall, request)
    }

    internal inner class ProxyCall<T>(val delegate: HiCall<T>, val request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                val cacheResponse = readCache<T>()
                if (cacheResponse.data != null) {
                    return cacheResponse
                }
            }
            val response = delegate.execute()
            saveCacheIfNeed(response)
            dispatchInterceptor(request, response)
            return response
        }

        private fun dispatchInterceptor(request: HiRequest, response: HiResponse<T>?) {
            InterceptorChain(request, response).dispatch()
        }


        override fun enqueue(callback: HiCallback<T>) {
            dispatchInterceptor(request, null)
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST) {
                HiExecutor.execute(runnable = Runnable {
                    val cacheResponse = readCache<T>()
                    if (cacheResponse.data != null) {
                        MainHandler.sendAtFrontOfQueue {
                            callback.onSuccess(cacheResponse)/*自线程,handler切主线程*/
                            HiLog.d("enqueue ,cache : " + request.getCacheKey())
                        }
                    }
                })
            }
            delegate.enqueue(object : HiCallback<T> {
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)
                    saveCacheIfNeed(response)
                    callback.onSuccess(response)/*主线程*/
                }

                override fun onFailed(throwable: Throwable) {
                    callback.onFailed(throwable)
                }
            })
        }

        private fun saveCacheIfNeed(response: HiResponse<T>) {
            if (request.cacheStrategy == CacheStrategy.CACHE_FIRST || request.cacheStrategy == CacheStrategy.NET_CACHE) {
                if (response.data != null) {
                    HiExecutor.execute {
                        HiStorage.saveCache(request.getCacheKey(), response.data)
                    }
                }
            }
        }

        private fun <T> readCache(): HiResponse<T> {
            /*histroage查询缓存*/
            val cacheKey = request.getCacheKey()
            val cache = HiStorage.getCache<T>(cacheKey)
            val cacheResponse = HiResponse<T>()
            cacheResponse.data = cache
            cacheResponse.code = HiResponse.CACHE_SUCCESS
            cacheResponse.msg = "缓存获取成功"
            return cacheResponse
        }

        internal inner class InterceptorChain(
            val request: HiRequest,
            val response: HiResponse<T>?
        ) : HiInterceptor.Chain {
            //代表的是 分发的第几个拦截器
            var callIndex: Int = 0

            override val isRequestPeriod: Boolean
                get() = response == null

            override fun request(): HiRequest {
                return request
            }

            override fun response(): HiResponse<*>? {
                return response
            }

            fun dispatch() {
                val interceptor = interceptors[callIndex]
                val intercept = interceptor.intercept(this)
                callIndex++
                if (!intercept && callIndex < interceptors.size) {
                    dispatch()
                }
            }
        }
    }

}