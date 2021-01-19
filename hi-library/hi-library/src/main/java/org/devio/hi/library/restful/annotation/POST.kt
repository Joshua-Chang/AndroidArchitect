package org.devio.hi.library.restful.annotation

/**
 * @POST("/cities/{province}")
 *fun test(@Path("province") int provinceId)
 */
annotation class POST(val value:String,val formPost:Boolean=true)
