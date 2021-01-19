package org.devio.`as`.hi.hi_concurrent_demo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import kotlinx.android.synthetic.main.activity_main.*
import org.devio.`as`.hi.hi_concurrent_demo.coroutine.CoroutineScene

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        test()
        setContentView(R.layout.activity_main)
        text1.setOnClickListener {
            CoroutineScene.startScene2()
        }
    }
}