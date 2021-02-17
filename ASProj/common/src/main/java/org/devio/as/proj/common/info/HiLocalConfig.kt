package org.devio.`as`.proj.common.info

import org.devio.`as`.proj.common.utils.SPUtil


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/13 6:31 PM
 * @创建人：常守达
 * @备注：
 */
class HiLocalConfig :LocalConfig{
    override fun authToken(): String {
        return "dsfasdfasd"
    }

    override fun boardingPass(): String? {
        return SPUtil.getString("boarding-pass")
    }

    companion object {
        @get:Synchronized
        var instance: HiLocalConfig? = null
            get() {
                if (field == null) {
                    field = HiLocalConfig()
                }
                return field
            }
            private set
    }
}
internal interface LocalConfig{
    fun authToken():String
    fun boardingPass():String?
}