package org.devio.`as`.proj.breakpad

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.TextView
import org.devio.`as`.proj.libbreakpad.NativeCrashHandler
import java.io.File

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Example of a call to a native method
        findViewById<TextView>(R.id.sample_text).setOnClickListener {
            val crashDir = File(cacheDir, "native_crash")
            if (!crashDir.exists()) {
                crashDir.mkdirs();
            }
            NativeCrashHandler.init(crashDir.absolutePath)
            crash()
        }

    }

    /**
     * A native method that is implemented by the 'native-lib' native library,
     * which is packaged with this application.
     */
    external fun stringFromJNI(): String

    external fun crash()

    companion object {
        // Used to load the 'native-lib' library on application startup.
        init {
            System.loadLibrary("native-lib")
        }
    }
}