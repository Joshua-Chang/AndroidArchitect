package org.devio.hi.library.restful

/**
 * 代理CallFactory创建出来的call对象，从而实现拦截器的派发动作
 */
class Scheduler(val callFactory: HiCall.Factory, val interceptors: MutableList<HiInterceptor>) {
    fun newCall(request: HiRequest) :HiCall<*>{
        val newCall:HiCall<*> = callFactory.newCall(request)
        return ProxyCall(newCall,request)
    }

    internal inner class ProxyCall<T>(val delegate: HiCall<T>, val request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            dispatchInterceptor(request, null)
            val response = delegate.execute()
            dispatchInterceptor(request, response)
            return response
        }

        private fun dispatchInterceptor(request: HiRequest, response: HiResponse<T>?) {
            InterceptorChain(request, response).dispatch()
        }


        override fun enqueue(callback: HiCallback<T>) {
            delegate.enqueue(object :HiCallback<T>{
                override fun onSuccess(response: HiResponse<T>) {
                    dispatchInterceptor(request, response)
                    callback.onSuccess(response)
                }

                override fun onFailed(throwable: Throwable) {
                    callback.onFailed(throwable)
                }
            })
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
                if (!intercept&&callIndex<interceptors.size){
                    dispatch()
                }
            }
        }
    }

}