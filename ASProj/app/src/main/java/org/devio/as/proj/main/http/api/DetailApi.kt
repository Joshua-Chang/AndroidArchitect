package org.devio.`as`.proj.main.http.api

import org.devio.`as`.proj.main.model.DetailModel
import org.devio.`as`.proj.main.model.GoodsModel
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

interface DetailApi {
    @GET("goods/detail/{id}")
    fun queryDetail(@Path("id") goodsId:String):HiCall<DetailModel>
}