package org.devio.hi.library.restful

import org.devio.hi.library.restful.annotation.*
import java.lang.reflect.Method
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class MethodParser(private val baseUrl: String, method: Method) {

    private var domainUrl: String? = null
    private var formPost: Boolean = true
    private var httpMethod: Int = 0
    private lateinit var relativeUrl: String
    private var returnType: Type? = null
    private var headers: MutableMap<String, String> = mutableMapOf()
    private var parameters: MutableMap<String, String> = mutableMapOf()
    private var replaceRelativeUrl: String? = null
    private var cacheStrategy: Int = CacheStrategy.NET_ONLY

    init {
        //parse method annotations such as get,headers,post baseUrl
        parseMethodAnnotations(method)

        //parse method generic return type
        parseMethodReturnType(method)

        //parse method parameters such as path,filed
//        parseMethodParameters(method, args)
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
    private fun parseMethodReturnType(method: Method) {
        if (method.returnType != HiCall::class.java) {
            throw IllegalStateException(
                String.format(
                    "method s% must be type of HiCall.class",
                    method.name
                )
            )
        }
        val genericReturnType = method.genericReturnType
        if (genericReturnType is ParameterizedType) {
            val actualTypeArguments = genericReturnType.actualTypeArguments
            require(actualTypeArguments.size == 1) { "method %s can only has one generic return type" }
            returnType = actualTypeArguments[0]
//            require(){ String.format("method %s generic return type must not be an unknown type. " + method.name) }
        } else {
            throw  IllegalStateException(
                String.format(
                    "method %s must has one gerneric return type",
                    method.name
                )
            )
        }
    }

    private fun parseMethodAnnotations(method: Method) {
        val annotations = method.annotations
        for (annotation in annotations) {
            if (annotation is GET) {
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.GET
            } else if (annotation is POST) {
                relativeUrl = annotation.value
                httpMethod = HiRequest.METHOD.POST
                formPost = annotation.formPost
            } else if (annotation is Headers) {
                val headersArray: Array<out String> = annotation.value
                //@Headers("auth-token:token", "accountId:123456")
                for (header in headersArray) {
                    val colon = header.indexOf(":")
                    check(!(colon == 0 || colon == -1)) {
                        String.format(
                            "@headers value must be in the form [name:value] ,but found [%s]",
                            header
                        )
                    }
                    val name = header.substring(0, colon)
                    val value = header.substring(colon + 1).trim()
                    headers[name] = value
                }
            } else if (annotation is BaseUrl) {
                domainUrl = annotation.value
            } else if (annotation is CacheStrategy) {
                cacheStrategy = annotation.value
            } else {
                throw IllegalStateException("cannot handle method annotation:" + annotation.javaClass.toString())
            }
        }


        require(
            (httpMethod == HiRequest.METHOD.GET)
                    || (httpMethod == HiRequest.METHOD.POST)
        ) {
            String.format("method %s must has one of GET,POST,PUT,DELETE ", method.name)
        }

        if (domainUrl == null) {
            domainUrl = baseUrl
        }
    }

    private fun parseMethodParameters(method: Method, args: Array<Any>) {
        //@Path("province") province: Int,@Filed("page") page: Int
        val parameterAnnotations = method.parameterAnnotations
        val equals = parameterAnnotations.size == args.size
        require(equals) {
            String.format(
                "arguments annotations count %s dont match expect count %s",
                parameterAnnotations.size,
                args.size
            )
        }

        //args

        for (index in args.indices) {
            val annotations = parameterAnnotations[index]
            require(annotations.size <= 1) { "filed can only has one annotation :index =$index" }
            val value = args[index]
            require(isPrimitive(value)) { "8 basic types are supported for now,index=$index" }
            val annotation = annotations[0]
            if (annotation is Filed) {
                val key = annotation.value
                parameters[key] = value.toString()
            } else if (annotation is Path) {
                val replaceName = annotation.value
                val replacement = value.toString()
                if (replaceName != null && replacement != null) {
                    //第一次替换 home/{categroyId}-》home/{1}
                    //relativeUrl = home/{categroyId}
//                    val replace = relativeUrl.replace(replaceName, replacement)
//                    relativeUrl=replace
                    replaceRelativeUrl = relativeUrl.replace("{$replaceName}", replacement)
                }
            } else if (annotation is CacheStrategy) {
                cacheStrategy = value as Int
            } else {
                throw  IllegalStateException("cannot handle parameter annotation :" + annotation.javaClass.toString())
            }
        }
    }

    private fun isPrimitive(value: Any): Boolean {
        //String
        if (value.javaClass == String::class.java) {
            return true
        }
        try {
            //int byte short long boolean char double float
            val field = value.javaClass.getField("TYPE")
            val clazz = field[null] as Class<*>
            if (clazz.isPrimitive) {
                return true
            }
        } catch (e: IllegalAccessException) {
            e.printStackTrace()
        } catch (e: NoSuchFieldException) {
            e.printStackTrace()
        }
        return false
    }

    fun newRequest(method: Method, args: Array<out Any>?): HiRequest {
        val arguments = args as Array<Any>? ?: arrayOf()
        parseMethodParameters(method, arguments)

        var request = HiRequest()
        request.domainUrl = domainUrl
        request.returnType = returnType
        request.relativeUrl = replaceRelativeUrl ?: relativeUrl
        request.parameters = parameters
        request.headers = headers
        request.httpMethod = httpMethod
        request.formPost = formPost
        request.cacheStrategy = cacheStrategy
        return request
    }

    companion object {
        fun parse(baseUrl: String, method: Method): MethodParser {
            return MethodParser(baseUrl, method)
        }
    }
}