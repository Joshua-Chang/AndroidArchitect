package hi.kotlin_dmeo

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
//        val v = find<TextView>(R.id.test)
        R.id.test.onClick(this){
            test.text="Kotlin extension"
        }
    }
    fun test(string: String?){
        if (string.isNullOrEmpty()) {
            println("isNullOrEmpty")
        }else if(string.isNullOrBlank()){
            println("isNullOrEmpty")
        }
    }
}