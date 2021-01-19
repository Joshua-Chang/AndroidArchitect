package org.devio.hi.library.app

import androidx.annotation.StringDef
import okhttp3.ResponseBody
import org.devio.hi.library.app.data.Banner
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface Api {
    companion object{
        const val GanHuo:String = "GanHuo"
        const val Article:String = "Article"
        const val Girl:String = "Girl"
    }
    @StringDef(value = [GanHuo,Article,Girl])
    @Retention(AnnotationRetention.SOURCE)
    annotation class Categories

    @GET("banners")
    fun banner():Call<ResponseBody>
    @GET("banners")
    fun banner2():Call<Banner>
//    fun bannerRx():Call<Banner>
//    fun banner3():Banner
    //Call<T>  <T> Successful response body type.
    //Call addCallAdapterFactory:添加一个构建Call适配器的工厂
    //T    addConverterFactory：添加一个response body转换的工厂 若不转换默认是okhttp的ResponseBody


    @GET("categories/{category}")
    fun categories(@Path("category")@Categories category:String):Call<ResponseBody>
}