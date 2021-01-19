package org.devio.hi.ui.app

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.annotation.RequiresApi
import org.devio.hi.ui.app.demo.tab.HiTabBottomDemoActivity
import org.devio.hi.ui.app.demo.banner.HiBannerDemoActivity
import org.devio.hi.ui.app.demo.refresh.HiRefreshDemoActivity
import org.devio.hi.ui.app.demo.tab.HiTabTopDemoActivity

class MainActivity : AppCompatActivity() ,View.OnClickListener{
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.tv_tab_bottom -> {
                startActivity(Intent(this, HiTabBottomDemoActivity::class.java))
            }
            R.id.tv_hi_refresh -> {
                startActivity(Intent(this, HiRefreshDemoActivity::class.java))
            }
            R.id.tv_hi_banner -> {
                startActivity(Intent(this, HiBannerDemoActivity::class.java))
            }
            R.id.tv_hi_taptop -> {
                startActivity(Intent(this, HiTabTopDemoActivity::class.java))
            }
        }
    }

}