package org.devio.hi.library.app

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.content.pm.ShortcutInfo
import android.content.pm.ShortcutManager
import android.graphics.drawable.Icon
import androidx.appcompat.app.AppCompatActivity
import com.google.gson.Gson
import org.devio.hi.library.log.HiConsolePrinter
import org.devio.hi.library.log.HiLogConfig
import org.devio.hi.library.log.HiLogManager
import org.devio.hi.library.log.HiLogPrinter

class MApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        HiLogManager.init(object :HiLogConfig(){
            override fun getGlobalTag(): String ="MApplication"
            override fun enable(): Boolean =true

//            override fun includeThread(): Boolean {
//                return super.includeThread()
//            }
//
//            override fun stackTraceDepth(): Int {
//                return super.stackTraceDepth()
//            }

//            override fun printers(): Array<HiLogPrinter> {
//                return super.printers()
//            }

            override fun injectJsonParser(): JsonParser {
                return JsonParser { src -> Gson().toJson(src) }
            }
        },HiConsolePrinter())
    }
    companion object{
        @JvmStatic
        fun shortCut(name: String, activity: Activity) {
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                val shortcutManager =
                    activity.getSystemService(AppCompatActivity.SHORTCUT_SERVICE) as ShortcutManager
                for (info in shortcutManager.pinnedShortcuts) {
                    if (info.shortLabel == name) return
                }
                val info = ShortcutInfo.Builder(activity, name)
                    .setIcon(Icon.createWithResource(activity, R.mipmap.ic_launcher))
                    .setShortLabel(name)
                    .setIntent(Intent(activity, activity::class.java).setAction(Intent.ACTION_VIEW))
                    .build()
                shortcutManager.requestPinShortcut(info, null)
            }
        }

    }
}