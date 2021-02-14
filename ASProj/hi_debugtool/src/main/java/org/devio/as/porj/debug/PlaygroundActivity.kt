package org.devio.`as`.porj.debug

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.activity_playground.*
import org.devio.hi.library.util.HiStatusBar

@Route(path = "/playground/main")
class PlaygroundActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        HiStatusBar.setStatusBar(this, true, enableTranslucent = false)
        setContentView(R.layout.activity_playground)
        initUI()
    }

    private fun initUI() {
        action_back.setOnClickListener {
            onBackPressed()
        }
        native_widget.setOnClickListener {
            ARouter.getInstance().build("/flutter/main").withString("moduleName", "nativeView")
                .navigation()

        }
    }
}