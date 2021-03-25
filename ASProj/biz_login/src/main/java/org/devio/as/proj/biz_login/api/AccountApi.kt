package org.devio.`as`.proj.biz_login.api

import org.devio.`as`.proj.service_login.UserProfile
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.Filed
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.POST
/*UserProfile数据模型在多模块都被使用，放到抽成登陆接口在service ISP*/
interface AccountApi {

    @POST("user/login")
    fun login(
        @Filed("userName") userName: String,
        @Filed("password") password: String
    ): HiCall<String>


    @POST("user/registration")
    fun register(
        @Filed("userName") userName: String,
        @Filed("password") password: String,
        @Filed("imoocId") imoocId:
        String, @Filed("orderId") orderId: String
    ): HiCall<String>


    @GET("user/profile")
    fun profile(): HiCall<UserProfile>

/*单纯的登陆模块不需要此方法，拆到home*/
//    @GET("notice")
//    fun notice(): HiCall<CourseNotice>
}