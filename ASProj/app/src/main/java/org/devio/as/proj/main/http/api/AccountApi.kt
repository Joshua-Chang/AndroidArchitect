package org.devio.`as`.proj.main.http.api

import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET
import com.google.gson.JsonObject
import org.devio.`as`.proj.main.model.CourseNotice
import org.devio.`as`.proj.main.model.UserProfile
import org.devio.hi.library.restful.annotation.POST

interface AccountApi {
    @GET("cities")
    fun listCities(@Filed("name") name: String): HiCall<JsonObject>

    @POST("user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ): HiCall<String>

    @POST("user/registration")
    fun register(
        @Filed("userName") userName: String,
        @Filed("password") password: String,
        @Filed("imoocId") imoocId: String,
        @Filed("orderId") orderId: String
    ): HiCall<String>

    @GET("user/profile")
    fun profile(): HiCall<UserProfile>

    @GET("notice")
    fun notice():HiCall<CourseNotice>
}