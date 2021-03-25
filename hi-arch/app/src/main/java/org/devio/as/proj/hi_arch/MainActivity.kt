package org.devio.`as`.proj.hi_arch

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import org.devio.`as`.proj.hi_arch.scene1.Scene1Activity
import org.devio.`as`.proj.hi_arch.scene2.Scene2Activity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun goScene1(view: View) {startActivity(Intent(this,Scene1Activity::class.java))}
    fun goScene2(view: View) {startActivity(Intent(this, Scene2Activity::class.java))}
    fun goScene3(view: View) {startActivity(Intent(this,Scene1Activity::class.java))}
}