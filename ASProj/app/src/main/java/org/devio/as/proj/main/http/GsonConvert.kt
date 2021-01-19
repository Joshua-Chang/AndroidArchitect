package org.devio.`as`.proj.main.http

import com.google.gson.Gson
import com.google.gson.JsonArray
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import org.devio.hi.library.restful.HiConvert
import org.devio.hi.library.restful.HiResponse
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import java.lang.reflect.Type

class GsonConvert : HiConvert {
    private var gson: Gson

    init {
        gson = Gson()
    }

    override fun <T> convert(rawData: String, dataType: Type): HiResponse<T> {
        var response: HiResponse<T> = HiResponse<T>()
        try {
            val jsonObject = JSONObject(rawData)
            response.code = jsonObject.optInt("code")
            response.msg = jsonObject.optString("msg")
//            val data = jsonObject.optString("data")
            val data = jsonObject.opt("data")//解析称对象，而不是string。兼容返回的data其他类型不报错
            if ((data is JSONObject) or (data is JSONArray)){
                if (response.code == HiResponse.SUCCESS) {
                    response.data = gson.fromJson(data.toString() , dataType)
                } else {//gson转map的标准写法
                    response.errorData = gson.fromJson<MutableMap<String, String>>(data.toString(),
                        object : TypeToken<MutableMap<String, String>>() {}.type
                    )
                }
            }else{//不兼容返回的data
                response.data= data as T?
            }
        } catch (e: JSONException) {
            e.printStackTrace()
            response.code=-1
            response.msg=e.message
        }
        response.rawData=rawData
        return response
    }
}