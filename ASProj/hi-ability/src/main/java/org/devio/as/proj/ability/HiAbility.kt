package org.devio.`as`.proj.ability

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import org.devio.`as`.proj.ability.push.IPushMessageHandler
import org.devio.`as`.proj.ability.push.PushInitialization
import org.devio.`as`.proj.ability.share.ShareBundle
import org.devio.`as`.proj.ability.share.ShareManager
import org.devio.hi.library.util.HiViewUtil


/**
 * @版本号：
 * @需求编号：
 * @功能描述：
 * @创建时间：2021/2/21 10:41 PM
 * @创建人：常守达
 * @备注：
 */
object HiAbility {
    private val scanResultLiveData = MutableLiveData<String>()
    fun init(
        application: Application, channel: String,
        iPushMessageHandler: IPushMessageHandler? = null
    ) {
//        if (HiViewUtil.inMainProcess(application)) {
            PushInitialization.init(application, channel, iPushMessageHandler)
//            AnalyseUtil.init(application, channel)
//        }
    }


    /**
     * 唤起分享面板
     */
    fun share(context: Context, shareBundle: ShareBundle) {
        ShareManager.share(context, shareBundle)
    }
}