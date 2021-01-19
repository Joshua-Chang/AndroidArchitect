package org.devio.`as`.proj.main.http.api

import org.devio.`as`.proj.main.model.Subcategory
import org.devio.`as`.proj.main.model.TabCategory
import org.devio.hi.library.restful.HiCall
import org.devio.hi.library.restful.annotation.GET
import org.devio.hi.library.restful.annotation.Path

interface CategoryApi {
    @GET("category/categories")
    fun queryCategoryList(): HiCall<List<TabCategory>>


    @GET("category/subcategories/{categoryId}")
    fun querySubcategoryList(@Path("categoryId") categoryId: String): HiCall<List<Subcategory>>
}