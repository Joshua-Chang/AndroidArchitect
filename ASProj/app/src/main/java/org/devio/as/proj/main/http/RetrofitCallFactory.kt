package org.devio.`as`.proj.main.http

import okhttp3.FormBody
import okhttp3.MediaType
import okhttp3.RequestBody
import okhttp3.ResponseBody
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.HiCallback
import org.devio.hi.library.restful.HiRequest
import org.devio.hi.library.restful.HiResponse
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import java.lang.IllegalStateException


class RetrofitCallFactory(val baseUrl: String) : HiCall.Factory {
    private var hiConvert: GsonConvert
    private var apiService: ApiService

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(baseUrl)
            .build()
        apiService = retrofit.create(ApiService::class.java)
        hiConvert = GsonConvert()
    }

    override fun newCall(request: HiRequest): HiCall<Any> {
        return RetrofitCall(request)
    }

    internal inner class RetrofitCall<T>(var request: HiRequest) : HiCall<T> {
        override fun execute(): HiResponse<T> {
            val realCall = createRealCall(request)
            val response = realCall.execute()
            return parseResponse(response)
        }

        override fun enqueue(callback: HiCallback<T>) {
            val realCall = createRealCall(request)
            realCall.enqueue(object :Callback<ResponseBody>{
                override fun onResponse(
                    call: Call<ResponseBody>,
                    response: Response<ResponseBody>
                ) {
                    callback.onSuccess(parseResponse(response))
                }

                override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                    callback.onFailed(throwable = t)
                }
            })
        }

        private fun parseResponse(response: Response<ResponseBody>?): HiResponse<T> {
            var rawData: String? = null
            if (response!!.isSuccessful) {
                val body = response.body()
                if (body != null) {
                    rawData = body.string()
                }
            } else {
                val body = response.errorBody()
                if (body != null) {
                    rawData = body.string()
                }
            }
            return hiConvert.convert(rawData!!, request.returnType!!)
        }

        private fun createRealCall(request: HiRequest): Call<ResponseBody> {
            if (request.httpMethod == HiRequest.METHOD.GET) {
                return apiService.get(request.headers, request.endPointUrl(), request.parameters)
            } else if (request.httpMethod == HiRequest.METHOD.POST) {
                val parameters = request.parameters
                var builder = FormBody.Builder()
                var jsonObject = JSONObject()
                var requestBody: RequestBody? = null

                for ((key, value) in parameters!!) {
                    if (request.formPost) {//表单
                        builder.add(key, value)
                    } else {//json
                        jsonObject.put(key, value)
                    }
                }

                if (request.formPost) {
                    requestBody = builder.build();
                } else {
                    requestBody = RequestBody.create(
                        MediaType.parse("application/json;utf-8"),
                        jsonObject.toString()
                    )
                }
                return apiService.post(request.headers, request.endPointUrl(), requestBody)
            } else {
                throw IllegalStateException("hirestful only support GET POST for new ,url" + request.endPointUrl())
            }
        }
    }


    interface ApiService {
        @GET
        fun get(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @QueryMap(encoded = true) params: MutableMap<String, String>?
        ): Call<ResponseBody>

        @POST
        fun post(
            @HeaderMap headers: MutableMap<String, String>?,
            @Url url: String,
            @Body body: RequestBody?
        ): Call<ResponseBody>
    }
}