package org.devio.hi.library.app.demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.devio.hi.library.app.MApplication
import org.devio.hi.library.app.R
import org.devio.hi.library.log.*

class HiLogDemoActivity : AppCompatActivity() {
    var viewPrinter:HiViewPrinter?=null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_hi_log_demo)
        MApplication.shortCut("1-1-log", this)
        viewPrinter=HiViewPrinter(this)
        findViewById<View>(R.id.btn_log).setOnClickListener {
            printLog()
        }
        viewPrinter!!.printerProvider.showFloatingView()
    }
    private fun printLog(){
        HiLogManager.getInstance().addPrinter(viewPrinter)
        HiLog.log(object :HiLogConfig(){
            override fun includeThread(): Boolean {
                return true
            }

            override fun stackTraceDepth(): Int {
                return 0
            }
        },HiLogType.E,"---","5566")
        HiLog.a("9900")
    }
}