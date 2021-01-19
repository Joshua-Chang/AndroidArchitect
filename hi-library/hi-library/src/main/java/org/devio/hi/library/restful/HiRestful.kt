package org.devio.hi.library.restful

import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy
import java.util.concurrent.ConcurrentHashMap


open class HiRestful(val baseUrl: String, var callFactory: HiCall.Factory) {
    private var scheduler: Scheduler
    private var interceptors: MutableList<HiInterceptor> = mutableListOf()
    private var methodService:ConcurrentHashMap<Method,MethodParser> = ConcurrentHashMap()
    fun addInterceptor(interceptor: HiInterceptor) {
        interceptors.add(interceptor)
    }
    init {
        scheduler=Scheduler(callFactory,interceptors)
    }
    /**
     * interface ApiService {
     *  @Headers("auth-token:token", "accountId:123456")
     *  @BaseUrl("https://api.devio.org/as/")
     *  @POST("/cities/{province}")
     *  @GET("/cities")
     * fun listCities(@Path("province") province: Int,@Filed("page") page: Int): HiCall<JsonObject>
     * }
     */
//    fun <T> create(service: Class<T>): T {
//        return Proxy.newProxyInstance(
//            service.classLoader,
//            arrayOf<Class<*>>(service)
//        ) { proxy, method, args ->//args可以为空，为空时报错
//            var methodParser = methodService.get(method)
//            if (methodParser == null) {
//                methodParser=MethodParser.parse(baseUrl, method, args)
//                methodService.put(method,methodParser)
//            }
//            val request = methodParser.newRequest()
//            //callFactory.newCall(request)
//            scheduler.newCall(request)
//        } as T
//    }

    fun <T> create(service: Class<T>): T {
        return Proxy.newProxyInstance(
            service.classLoader,
            arrayOf<Class<*>>(service),
            object :InvocationHandler{
                override fun invoke(proxy: Any?, method: Method, args: Array<out Any>?/*允许为空*/): Any {
                        var methodParser = methodService.get(method)
                        if (methodParser == null) {
                            methodParser=MethodParser.parse(baseUrl, method)
                            methodService.put(method,methodParser)
                        }
                        //每次调用地址相同，仅参数修改的情况
                        val request = methodParser.newRequest(method,args)
                        return scheduler.newCall(request)
                }
            }
        )as T
    }


}
