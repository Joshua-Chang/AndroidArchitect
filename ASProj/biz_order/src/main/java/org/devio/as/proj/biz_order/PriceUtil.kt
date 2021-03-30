package org.devio.`as`.proj.biz_order

import android.text.TextUtils
import java.math.BigDecimal
import java.text.DecimalFormat
import java.time.temporal.TemporalAmount

internal /*相同包名才能访问*/object PriceUtil {
    private fun subPrice(goodsPrice: String?): String? {
        if (goodsPrice != null) {
            if (goodsPrice.startsWith("¥") && goodsPrice.length > 1) {
                return goodsPrice.substring(1, goodsPrice.length)
            }
            return goodsPrice
        }
        return null
    }

    /*求总价*/
    fun calculate(goodsPrice: String?, amount: Int): String {
        val price = subPrice(goodsPrice)
        if (TextUtils.isEmpty(price)) return ""
        //在做金额的加减乘除的时候 不能够直接使用基本数据类型 +-x/
        val bigDecimal = BigDecimal(price)
        val multiply = bigDecimal.multiply(BigDecimal(amount))
        //金额数值的格式化
        // 100。x3=  300
        // 100.3  x3=300.9
        // 100 x 100 =10,000
        val decimalFormat = DecimalFormat("###,###.##")
        return decimalFormat.format(multiply)
    }
}