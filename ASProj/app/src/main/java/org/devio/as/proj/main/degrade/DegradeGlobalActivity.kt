package org.devio.`as`.proj.main.degrade

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.alibaba.android.arouter.facade.annotation.Autowired
import com.alibaba.android.arouter.facade.annotation.Route
import com.alibaba.android.arouter.launcher.ARouter
import kotlinx.android.synthetic.main.layout_global_degrade.*
import org.devio.`as`.proj.main.R

@Route(path = "/degrade/global/activity")
class DegradeGlobalActivity : AppCompatActivity() {
    @JvmField//不能private 所以要jvmfield
    @Autowired
    var degrade_title: String? = null

    @JvmField
    @Autowired
    var degrade_desc: String? = null

    @JvmField
    @Autowired
    var degrade_action: String? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ARouter.getInstance().inject(this)
        setContentView(R.layout.layout_global_degrade)
        empty_view.setIcon(R.string.if_unexpected1)
        if (degrade_title != null) {
            empty_view.setTitle(degrade_title!!)
        }

        if (degrade_desc != null) {
            empty_view.setDesc(degrade_desc!!)
        }
        if (degrade_action != null) {
            empty_view.setHelpAction(listener = View.OnClickListener {
                var intent: Intent = Intent(Intent.ACTION_VIEW, Uri.parse(degrade_action))
                startActivity(intent)
            })
        }

        action_back.setOnClickListener { onBackPressed() }
    }
}